/*******************************************************************************
 * Copyright (c) 2019, 2021 Obeo.
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
package org.eclipse.sirius.web.services.editingcontext;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.UUID;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.impl.EPackageRegistryImpl;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.ECrossReferenceAdapter;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.sirius.web.core.api.IEditingContext;
import org.eclipse.sirius.web.core.api.IEditingContextSearchService;
import org.eclipse.sirius.web.emf.services.EditingContext;
import org.eclipse.sirius.web.emf.services.IEditingContextEPackageService;
import org.eclipse.sirius.web.persistence.entities.DocumentEntity;
import org.eclipse.sirius.web.persistence.entities.ProjectEntity;
import org.eclipse.sirius.web.persistence.repositories.IDocumentRepository;
import org.eclipse.sirius.web.persistence.repositories.IProjectRepository;
import org.eclipse.sirius.web.services.documents.DocumentMetadataAdapter;
import org.junit.jupiter.api.Test;

import io.micrometer.core.instrument.simple.SimpleMeterRegistry;

/**
 * Unit tests of the editing context search service.
 *
 * @author sbegaudeau
 */
public class EditingContextSearchServiceTests {
    // @formatter:off
    private static final String CONTENT = "{" + System.lineSeparator() //$NON-NLS-1$
    + "    \"json\": {" + System.lineSeparator() //$NON-NLS-1$
    + "      \"version\": \"1.0\"," + System.lineSeparator() //$NON-NLS-1$
    + "    \"encoding\": \"utf-8\"" + System.lineSeparator() //$NON-NLS-1$
    + "  }," + System.lineSeparator() //$NON-NLS-1$
    + "  \"ns\": {" + System.lineSeparator() //$NON-NLS-1$
    + "      \"ecore\": \"http://www.eclipse.org/emf/2002/Ecore\"" + System.lineSeparator() //$NON-NLS-1$
    + "  }," + System.lineSeparator() //$NON-NLS-1$
    + "  \"content\": [" + System.lineSeparator() //$NON-NLS-1$
    + "      {" + System.lineSeparator() //$NON-NLS-1$
    + "        \"eClass\": \"ecore:EPackage\"," + System.lineSeparator() //$NON-NLS-1$
    + "      \"data\": {" + System.lineSeparator() //$NON-NLS-1$
    + "          \"name\": \"ecore\"," + System.lineSeparator() //$NON-NLS-1$
    + "        \"nsURI\": \"http://www.eclipse.org/emf/2002/Ecore\"," + System.lineSeparator() //$NON-NLS-1$
    + "        \"nsPrefix\": \"ecore\"," + System.lineSeparator() //$NON-NLS-1$
    + "        \"eClassifiers\": [" + System.lineSeparator() //$NON-NLS-1$
    + "            {" + System.lineSeparator() //$NON-NLS-1$
    + "              \"eClass\": \"ecore:EClass\"," + System.lineSeparator() //$NON-NLS-1$
    + "            \"data\": {" + System.lineSeparator() //$NON-NLS-1$
    + "                \"name\": \"AClass\"" + System.lineSeparator() //$NON-NLS-1$
    + "            }" + System.lineSeparator() //$NON-NLS-1$
    + "          }" + System.lineSeparator() //$NON-NLS-1$
    + "        ]" + System.lineSeparator() //$NON-NLS-1$
    + "      }" + System.lineSeparator() //$NON-NLS-1$
    + "    }" + System.lineSeparator() //$NON-NLS-1$
    + "  ]" + System.lineSeparator() //$NON-NLS-1$
    + "}" + System.lineSeparator(); //$NON-NLS-1$
    // @formatter:on

    @Test
    public void testEditingContextWithNoDocuments() {
        IProjectRepository projectRepository = new NoOpProjectRepository();
        IDocumentRepository documentRepository = new NoOpDocumentRepository();
        ComposedAdapterFactory composedAdapterFactory = new ComposedAdapterFactory();
        EPackage.Registry ePackageRegistry = new EPackageRegistryImpl();
        ePackageRegistry.put(EcorePackage.eNS_URI, EcorePackage.eINSTANCE);

        UUID projectId = UUID.randomUUID();

        IEditingContextEPackageService editingContextEPackageService = editingContextId -> List.of();
        IEditingContextSearchService editingContextSearchService = new EditingContextSearchService(projectRepository, documentRepository, editingContextEPackageService, composedAdapterFactory,
                ePackageRegistry, new SimpleMeterRegistry());
        IEditingContext editingContext = editingContextSearchService.findById(projectId).get();

        assertThat(editingContext).isInstanceOf(EditingContext.class);
        EditingDomain editingDomain = ((EditingContext) editingContext).getDomain();
        assertThat(editingDomain.getResourceSet().getResources()).hasSize(0);
    }

    @Test
    public void testEditingContextWithDocuments() {
        UUID projectId = UUID.randomUUID();

        ProjectEntity projectEntity = new ProjectEntity();
        projectEntity.setId(projectId);
        projectEntity.setName(""); //$NON-NLS-1$

        DocumentEntity firstDocumentEntity = new DocumentEntity();
        firstDocumentEntity.setId(UUID.randomUUID());
        firstDocumentEntity.setName("First Document"); //$NON-NLS-1$
        firstDocumentEntity.setProject(projectEntity);
        firstDocumentEntity.setContent(CONTENT);

        DocumentEntity secondDocumentEntity = new DocumentEntity();
        secondDocumentEntity.setId(UUID.randomUUID());
        secondDocumentEntity.setName("Second Document"); //$NON-NLS-1$
        secondDocumentEntity.setProject(projectEntity);
        secondDocumentEntity.setContent(CONTENT);

        IProjectRepository projectRepository = new NoOpProjectRepository();
        IDocumentRepository documentRepository = new NoOpDocumentRepository() {
            @Override
            public List<DocumentEntity> findAllByProjectId(UUID projectId) {
                return List.of(firstDocumentEntity, secondDocumentEntity);
            }
        };

        ComposedAdapterFactory composedAdapterFactory = new ComposedAdapterFactory();
        EPackage.Registry ePackageRegistry = new EPackageRegistryImpl();
        ePackageRegistry.put(EcorePackage.eNS_URI, EcorePackage.eINSTANCE);

        IEditingContextEPackageService editingContextEPackageService = editingContextId -> List.of();
        IEditingContextSearchService editingContextSearchService = new EditingContextSearchService(projectRepository, documentRepository, editingContextEPackageService, composedAdapterFactory,
                ePackageRegistry, new SimpleMeterRegistry());
        IEditingContext editingContext = editingContextSearchService.findById(projectId).get();

        assertThat(editingContext).isInstanceOf(EditingContext.class);
        EditingDomain editingDomain = ((EditingContext) editingContext).getDomain();

        assertThat(editingDomain.getResourceSet().getResources()).hasSize(2);
        Resource firstResource = editingDomain.getResourceSet().getResource(URI.createURI(firstDocumentEntity.getId().toString()), true);
        this.assertProperResourceLoading(firstResource, firstDocumentEntity);

        Resource secondResource = editingDomain.getResourceSet().getResource(URI.createURI(secondDocumentEntity.getId().toString()), true);
        this.assertProperResourceLoading(secondResource, secondDocumentEntity);
    }

    private void assertProperResourceLoading(Resource resource, DocumentEntity documentEntity) {
        assertThat(resource).isNotNull();
        assertThat(resource.eAdapters()).hasSize(2);
        // @formatter:off
        var optionalDocumentMetadataAdapter = resource.eAdapters().stream()
                                                      .filter(DocumentMetadataAdapter.class::isInstance)
                                                      .map(DocumentMetadataAdapter.class::cast)
                                                      .findFirst();
        // @formatter:on
        assertThat(optionalDocumentMetadataAdapter).isPresent();
        assertThat(optionalDocumentMetadataAdapter.get().getName()).isEqualTo(documentEntity.getName());

        // @formatter:off
        var optionalCrossReferencerAdapter = resource.eAdapters().stream()
                                                      .filter(ECrossReferenceAdapter.class::isInstance)
                                                      .map(ECrossReferenceAdapter.class::cast)
                                                      .findFirst();
        // @formatter:on
        assertThat(optionalCrossReferencerAdapter).isPresent();
    }
}
