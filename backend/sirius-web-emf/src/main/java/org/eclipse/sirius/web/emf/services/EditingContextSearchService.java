/*******************************************************************************
 * Copyright (c) 2021 Obeo.
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.web.emf.services;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.sirius.emfjson.resource.JsonResource;
import org.eclipse.sirius.web.core.api.IEditingContext;
import org.eclipse.sirius.web.core.api.IEditingContextSearchService;
import org.eclipse.sirius.web.persistence.entities.DocumentEntity;
import org.eclipse.sirius.web.persistence.repositories.IDocumentRepository;
import org.eclipse.sirius.web.persistence.repositories.IProjectRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;

/**
 * Service used to find and retrieve editing contexts.
 *
 * @author sbegaudeau
 */
@Service
public class EditingContextSearchService implements IEditingContextSearchService {

    private static final String TIMER_NAME = "siriusweb_editingcontext_load"; //$NON-NLS-1$

    private final Logger logger = LoggerFactory.getLogger(EditingContextSearchService.class);

    private final IProjectRepository projectRepository;

    private final IDocumentRepository documentRepository;

    private final ComposedAdapterFactory composedAdapterFactory;

    private final EPackage.Registry ePackageRegistry;

    private final Timer timer;

    public EditingContextSearchService(IProjectRepository projectRepository, IDocumentRepository documentRepository, ComposedAdapterFactory composedAdapterFactory, EPackage.Registry ePackageRegistry,
            MeterRegistry meterRegistry) {
        this.projectRepository = Objects.requireNonNull(projectRepository);
        this.documentRepository = Objects.requireNonNull(documentRepository);
        this.composedAdapterFactory = Objects.requireNonNull(composedAdapterFactory);
        this.ePackageRegistry = Objects.requireNonNull(ePackageRegistry);

        this.timer = Timer.builder(TIMER_NAME).register(meterRegistry);
    }

    @Override
    public boolean existsById(UUID editingContextId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return this.projectRepository.existsByIdAndIsVisibleBy(editingContextId, username);
    }

    @Override
    public Optional<IEditingContext> findById(UUID editingContextId) {
        long start = System.currentTimeMillis();

        this.logger.debug(MessageFormat.format("Loading the editing context \"{0}\"", editingContextId)); //$NON-NLS-1$
        ResourceSet resourceSet = new ResourceSetImpl();
        resourceSet.setPackageRegistry(this.ePackageRegistry);

        List<DocumentEntity> documentEntities = this.documentRepository.findAllByProjectId(editingContextId);
        for (DocumentEntity documentEntity : documentEntities) {
            URI uri = URI.createURI(documentEntity.getId().toString());
            JsonResource resource = new SiriusWebJSONResourceFactoryImpl().createResource(uri);
            try (var inputStream = new ByteArrayInputStream(documentEntity.getContent().getBytes())) {
                resource.load(inputStream, null);

                resource.eAdapters().add(new DocumentMetadataAdapter(documentEntity.getName()));
                resourceSet.getResources().add(resource);
            } catch (IOException exception) {
                this.logger.error(exception.getMessage(), exception);
            }
        }

        AdapterFactoryEditingDomain editingDomain = new AdapterFactoryEditingDomain(this.composedAdapterFactory, new BasicCommandStack(), resourceSet);
        this.logger.debug(MessageFormat.format("{0} documents loaded for the editing context \"{1}\"", documentEntities.size(), editingContextId)); //$NON-NLS-1$

        long end = System.currentTimeMillis();
        this.timer.record(end - start, TimeUnit.MILLISECONDS);

        return Optional.of(new EditingContext(editingContextId, editingDomain));
    }

}
