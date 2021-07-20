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
package org.eclipse.sirius.web.emf.compatibility.properties;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.sirius.web.forms.description.IfDescription;
import org.eclipse.sirius.web.forms.description.TextfieldDescription;
import org.eclipse.sirius.web.representations.Status;
import org.eclipse.sirius.web.representations.VariableManager;

/**
 * Provides the default description of the widget to use to support numbers.
 *
 * @author sbegaudeau
 */
public class NumberIfDescriptionProvider {

    private static final String TEXTFIELD_DESCRIPTION_ID = "Textfield"; //$NON-NLS-1$

    private final EDataType eDataType;

    private final ComposedAdapterFactory composedAdapterFactory;

    public NumberIfDescriptionProvider(EDataType eDataType, ComposedAdapterFactory composedAdapterFactory) {
        this.eDataType = Objects.requireNonNull(eDataType);
        this.composedAdapterFactory = Objects.requireNonNull(composedAdapterFactory);
    }

    public IfDescription getIfDescription() {
        // @formatter:off
        return IfDescription.newIfDescription(this.eDataType.getName())
                .predicate(this.getPredicate())
                .widgetDescription(this.getTextfieldDescription())
                .build();
        // @formatter:on
    }

    private Function<VariableManager, Boolean> getPredicate() {
        return variableManager -> {
            var optionalEAttribute = variableManager.get(PropertiesDefaultDescriptionProvider.ESTRUCTURAL_FEATURE, EAttribute.class);
            return optionalEAttribute.filter(eAttribute -> eAttribute.getEType().equals(this.eDataType)).isPresent();
        };
    }

    private TextfieldDescription getTextfieldDescription() {
        // @formatter:off
        return TextfieldDescription.newTextfieldDescription(TEXTFIELD_DESCRIPTION_ID)
                .idProvider(this.getIdProvider())
                .labelProvider(this.getLabelProvider())
                .valueProvider(this.getValueProvider())
                .newValueHandler(this.getNewValueHandler())
                .diagnosticsProvider((variableManager) -> List.of())
                .kindProvider((object) -> "") //$NON-NLS-1$
                .messageProvider((object) -> "") //$NON-NLS-1$
                .build();
        // @formatter:on
    }

    private Function<VariableManager, String> getIdProvider() {
        return variableManager -> {
            var eObjectPart = variableManager.get(VariableManager.SELF, EObject.class);
            var featurePart = variableManager.get(PropertiesDefaultDescriptionProvider.ESTRUCTURAL_FEATURE, EObject.class);
            return this.getEObjectId(eObjectPart) + this.getEObjectId(featurePart);
        };
    }

    private String getEObjectId(Optional<EObject> potentialEObject) {
        // @formatter:off
        return potentialEObject.map(EcoreUtil::getURI)
                .map(Object::toString)
                .orElse(""); //$NON-NLS-1$
        // @formatter:on
    }

    private Function<VariableManager, String> getLabelProvider() {
        return new EStructuralFeatureLabelProvider(PropertiesDefaultDescriptionProvider.ESTRUCTURAL_FEATURE, this.composedAdapterFactory);
    }

    private Function<VariableManager, String> getValueProvider() {
        return variableManager -> {
            var optionalEObject = variableManager.get(VariableManager.SELF, EObject.class);
            var optionalEAttribute = variableManager.get(PropertiesDefaultDescriptionProvider.ESTRUCTURAL_FEATURE, EAttribute.class);
            if (optionalEObject.isPresent() && optionalEAttribute.isPresent()) {
                EObject eObject = optionalEObject.get();
                EAttribute eAttribute = optionalEAttribute.get();

                Object value = eObject.eGet(eAttribute);
                return EcoreUtil.convertToString(this.eDataType, value);
            }

            return ""; //$NON-NLS-1$
        };
    }

    private BiFunction<VariableManager, String, Status> getNewValueHandler() {
        return (variableManager, newValue) -> {
            var optionalEObject = variableManager.get(VariableManager.SELF, EObject.class);
            var optionalEAttribute = variableManager.get(PropertiesDefaultDescriptionProvider.ESTRUCTURAL_FEATURE, EAttribute.class);

            Object value = EcoreUtil.createFromString(this.eDataType, newValue);
            if (optionalEObject.isPresent() && optionalEAttribute.isPresent()) {
                EObject eObject = optionalEObject.get();
                EAttribute eAttribute = optionalEAttribute.get();

                eObject.eSet(eAttribute, value);
                return Status.OK;
            }
            return Status.ERROR;
        };
    }
}
