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
package org.eclipse.sirius.web.emf.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.DiagnosticChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EValidator;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.sirius.web.domain.Domain;
import org.eclipse.sirius.web.domain.DomainPackage;
import org.eclipse.sirius.web.domain.Entity;
import org.eclipse.sirius.web.view.Conditional;
import org.eclipse.sirius.web.view.NodeDescription;
import org.eclipse.sirius.web.view.NodeStyle;
import org.eclipse.sirius.web.view.ViewPackage;

/**
 * The validator for View.
 *
 * @author gcoutable
 */
public class ViewValidator implements EValidator {

    private static final String NODE_DESCRIPTION_INVALID_DOMAIN_TYPE_ERROR_MESSAGE = "The node description \"%1$s\" does not have a valid domain class"; //$NON-NLS-1$

    private static final String SIRIUS_WEB_EMF_PACKAGE = "org.eclipse.sirius.web.emf"; //$NON-NLS-1$

    private static final String DOMAIN_URI_SCHEME = "domain://"; //$NON-NLS-1$

    @Override
    public boolean validate(EObject eObject, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    @Override
    public boolean validate(EClass eClass, EObject eObject, DiagnosticChain diagnostics, Map<Object, Object> context) {
        boolean isValid = true;
        if (eObject instanceof NodeDescription) {
            NodeDescription nodeDescription = (NodeDescription) eObject;
            isValid = this.hasProperDomainType(nodeDescription, diagnostics) && isValid;
        }
        if (eObject instanceof NodeStyle) {
            NodeStyle nodeStyle = (NodeStyle) eObject;
            isValid = this.hasProperColor(nodeStyle, diagnostics) && isValid;
        }
        if (eObject instanceof Conditional) {
            Conditional conditional = (Conditional) eObject;
            isValid = this.conditionIsPresent(conditional, diagnostics, context) && isValid;
        }
        return isValid;
    }

    @Override
    public boolean validate(EDataType eDataType, Object value, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    private boolean conditionIsPresent(Conditional conditional, DiagnosticChain diagnostics, Map<Object, Object> context) {
        boolean isValid = !conditional.getCondition().isBlank();

        if (!isValid && diagnostics != null) {
            // @formatter:off
            BasicDiagnostic basicDiagnostic = new BasicDiagnostic(Diagnostic.ERROR,
                    SIRIUS_WEB_EMF_PACKAGE,
                    0,
                    "The condition should not be empty", //$NON-NLS-1$
                    new Object [] {
                            conditional,
                            ViewPackage.Literals.CONDITIONAL__CONDITION,
                    });
            // @formatter:on

            diagnostics.add(basicDiagnostic);
        }

        return isValid;
    }

    private boolean hasProperColor(NodeStyle nodeStyle, DiagnosticChain diagnostics) {
        boolean isValid = !nodeStyle.getColor().isBlank();

        if (!isValid && diagnostics != null) {
            // @formatter:off
            BasicDiagnostic basicDiagnostic = new BasicDiagnostic(Diagnostic.ERROR,
                    SIRIUS_WEB_EMF_PACKAGE,
                    0,
                    "The color should not be empty", //$NON-NLS-1$
                    new Object [] {
                            nodeStyle,
                            ViewPackage.Literals.STYLE__COLOR,
                    });
            // @formatter:on

            diagnostics.add(basicDiagnostic);
        }

        return isValid;
    }

    private boolean hasProperDomainType(NodeDescription nodeDescription, DiagnosticChain diagnostics) {
        boolean isValid = false;
        ResourceSet resourceSet = nodeDescription.eResource().getResourceSet();
        List<Entity> entities = this.getDomainEntitiesFromResourceSet(resourceSet);
        List<EPackage> ePackages = this.getDomainEPackagesFromRegistry(resourceSet.getPackageRegistry());

        // @formatter:off
        isValid = entities.stream()
                .map(Entity::getName)
                .anyMatch(name -> name.equals(nodeDescription.getDomainType()));
        // @formatter:on

        if (!isValid) {
            // @formatter:off
            isValid = ePackages.stream()
                    .map(EPackage::getEClassifiers)
                    .flatMap(Collection::stream)
                    .anyMatch(classifier -> classifier.getName().equals(nodeDescription.getDomainType()));
            // @formatter:off
        }

        if (!isValid && diagnostics != null) {
            // @formatter:off
            BasicDiagnostic basicDiagnostic = new BasicDiagnostic(Diagnostic.ERROR,
                    SIRIUS_WEB_EMF_PACKAGE,
                    0,
                    String.format(NODE_DESCRIPTION_INVALID_DOMAIN_TYPE_ERROR_MESSAGE, nodeDescription.getDomainType()),
                    new Object [] {
                            nodeDescription,
                            ViewPackage.Literals.DIAGRAM_ELEMENT_DESCRIPTION__DOMAIN_TYPE,
                    });
            // @formatter:on

            diagnostics.add(basicDiagnostic);
        }

        return isValid;
    }

    private List<Entity> getDomainEntitiesFromResourceSet(ResourceSet resourceSet) {
        // @formatter:off
        return resourceSet.getResources().stream()
                .map(Resource::getContents)
                .flatMap(Collection::stream)
                .filter(e -> DomainPackage.Literals.DOMAIN.equals(e.eClass()))
                .map(Domain.class::cast)
                .map(Domain::getTypes)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        // @formatter:on
    }

    private List<EPackage> getDomainEPackagesFromRegistry(EPackage.Registry ePackageRegistry) {
        List<EPackage> allEPackage = new ArrayList<>();

        // @formatter:off
        List<EPackage> ePackages = ePackageRegistry.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(DOMAIN_URI_SCHEME))
                .map(Entry::getValue)
                .filter(EPackage.class::isInstance)
                .map(EPackage.class::cast)
                .collect(Collectors.toList());
        allEPackage.addAll(ePackages);

        ePackages.stream()
                .map(this::getSubPackages)
                .forEach(allEPackage::addAll);
        // @formatter:on
        return allEPackage;
    }

    private List<EPackage> getSubPackages(EPackage ePackage) {
        List<EPackage> allEPackage = new ArrayList<>();

        EList<EPackage> eSubpackages = ePackage.getESubpackages();
        allEPackage.addAll(eSubpackages);

        // @formatter:off
        eSubpackages.stream()
            .map(this::getSubPackages)
            .forEach(allEPackage::addAll);
        // @formatter:on

        return allEPackage;
    }
}
