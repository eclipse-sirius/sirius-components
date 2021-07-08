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

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EValidator;
import org.eclipse.emf.ecore.EValidator.Registry;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.Diagnostician;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.sirius.web.collaborative.validation.api.IValidationService;
import org.eclipse.sirius.web.core.api.IEditingContext;
import org.springframework.stereotype.Service;

/**
 * Used to validate EMF elements.
 *
 * @author gcoutable
 */
@Service
public class EMFValidationService implements IValidationService {

    private final Registry eValidatorRegistry;

    public EMFValidationService(EValidator.Registry eValidatorRegistry) {
        this.eValidatorRegistry = eValidatorRegistry;
    }

    @Override
    public List<Object> validate(IEditingContext editingContext) {
        // @formatter:off
        List<Diagnostic> diagnostics = Optional.of(editingContext)
            .filter(EditingContext.class::isInstance)
            .map(EditingContext.class::cast)
            .map(this::validate)
            .orElseGet(List::of);

        return diagnostics.stream()
            .map(this::convertToDiagnostic)
            .collect(Collectors.toList());
        // @formatter:on
    }

    private List<Diagnostic> validate(EditingContext editingContext) {
        AdapterFactoryEditingDomain domain = editingContext.getDomain();

        Map<Object, Object> options = new HashMap<>();
        options.put(Diagnostician.VALIDATE_RECURSIVELY, true);
        Diagnostician diagnostician = new Diagnostician(this.eValidatorRegistry);

        // @formatter:off
        return domain.getResourceSet().getResources().stream()
            .map(Resource::getContents)
            .flatMap(Collection::stream)
            .map(eObject -> diagnostician.validate(eObject, options))
            .map(Diagnostic::getChildren)
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
        // @formatter:on
    }

    private org.eclipse.sirius.web.validation.Diagnostic convertToDiagnostic(Diagnostic diagnostic) {
        String kind = ""; //$NON-NLS-1$
        switch (diagnostic.getSeverity()) {
        case org.eclipse.emf.common.util.Diagnostic.ERROR:
            kind = "Error"; //$NON-NLS-1$
            break;
        case org.eclipse.emf.common.util.Diagnostic.WARNING:
            kind = "Warning"; //$NON-NLS-1$
            break;
        case org.eclipse.emf.common.util.Diagnostic.INFO:
            kind = "Info"; //$NON-NLS-1$
            break;
        default:
            break;
        }

        // @formatter:off
        return org.eclipse.sirius.web.validation.Diagnostic.newDiagnostic(UUID.randomUUID())
                .kind(kind)
                .message(diagnostic.getMessage())
                .build();
        // @formatter:on
    }

}
