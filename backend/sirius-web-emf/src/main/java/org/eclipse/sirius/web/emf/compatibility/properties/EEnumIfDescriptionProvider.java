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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.eclipse.emf.common.util.Enumerator;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.sirius.web.compat.forms.WidgetIdProvider;
import org.eclipse.sirius.web.forms.components.SelectComponent;
import org.eclipse.sirius.web.forms.description.IfDescription;
import org.eclipse.sirius.web.forms.description.RadioDescription;
import org.eclipse.sirius.web.representations.Status;
import org.eclipse.sirius.web.representations.VariableManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides the default description of the widget to use to support an EEnum feature.
 *
 * @author sbegaudeau
 */
public class EEnumIfDescriptionProvider {

    private static final String IF_DESCRIPTION_ID = "EEnum"; //$NON-NLS-1$

    private static final String RADIO_DESCRIPTION_ID = "Radio"; //$NON-NLS-1$

    private final Logger logger = LoggerFactory.getLogger(EEnumIfDescriptionProvider.class);

    private final ComposedAdapterFactory composedAdapterFactory;

    public EEnumIfDescriptionProvider(ComposedAdapterFactory composedAdapterFactory) {
        this.composedAdapterFactory = Objects.requireNonNull(composedAdapterFactory);
    }

    public IfDescription getIfDescription() {
        // @formatter:off
        return IfDescription.newIfDescription(IF_DESCRIPTION_ID)
                .predicate(this.getPredicate())
                .widgetDescription(this.getRadioDescription())
                .build();
        // @formatter:on
    }

    private Function<VariableManager, Boolean> getPredicate() {
        return variableManager -> {
            var optionalEAttribute = variableManager.get(PropertiesDefaultDescriptionProvider.ESTRUCTURAL_FEATURE, EAttribute.class);
            // @formatter:off
            return optionalEAttribute.map(EAttribute::getEType)
                    .filter(EEnum.class::isInstance)
                    .isPresent();
            // @formatter:on
        };
    }

    private RadioDescription getRadioDescription() {
        // @formatter:off
        return RadioDescription.newRadioDescription(RADIO_DESCRIPTION_ID)
                .idProvider(new WidgetIdProvider())
                .labelProvider(this.getLabelProvider())
                .optionsProvider(this.getOptionsProvider())
                .optionSelectedProvider(this.getOptionSelectedProvider())
                .optionIdProvider(this.getOptionIdProvider())
                .optionLabelProvider(this.getOptionLabelProvider())
                .newValueHandler(this.getNewValueHandler())
                .diagnosticsProviders((variableManager) -> List.of())
                .kindProvider((object) -> "") //$NON-NLS-1$
                .messageProvider((object) -> "") //$NON-NLS-1$
                .build();
        // @formatter:on
    }

    private Function<VariableManager, String> getLabelProvider() {
        return new EStructuralFeatureLabelProvider(PropertiesDefaultDescriptionProvider.ESTRUCTURAL_FEATURE, this.composedAdapterFactory);
    }

    private Function<VariableManager, List<Object>> getOptionsProvider() {
        return variableManager -> {
            Object feature = variableManager.getVariables().get(PropertiesDefaultDescriptionProvider.ESTRUCTURAL_FEATURE);
            if (feature instanceof EAttribute) {
                EDataType eEnum = ((EAttribute) feature).getEAttributeType();
                if (eEnum instanceof EEnum) {
                    // @formatter:off
                    return ((EEnum) eEnum).getELiterals().stream()
                            .map(EEnumLiteral::getInstance)
                            .collect(Collectors.toUnmodifiableList());
                    // @formatter:on
                }
            }
            return new ArrayList<>();
        };
    }

    private Function<VariableManager, String> getOptionIdProvider() {
        return variableManager -> {
            Object litteral = variableManager.getVariables().get(SelectComponent.CANDIDATE_VARIABLE);
            if (litteral instanceof Enumerator) {
                return Integer.valueOf(((Enumerator) litteral).getValue()).toString();
            }
            return ""; //$NON-NLS-1$
        };
    }

    private Function<VariableManager, String> getOptionLabelProvider() {
        return variableManager -> {
            Object litteral = variableManager.getVariables().get(SelectComponent.CANDIDATE_VARIABLE);
            if (litteral instanceof Enumerator) {
                return ((Enumerator) litteral).getName();
            }
            return ""; //$NON-NLS-1$
        };
    }

    private Function<VariableManager, Boolean> getOptionSelectedProvider() {
        return variableManager -> {
            var optionalEnumerator = variableManager.get(SelectComponent.CANDIDATE_VARIABLE, Enumerator.class);
            if (optionalEnumerator.isPresent()) {
                Enumerator enumerator = optionalEnumerator.get();
                String optionLitteralId = Integer.valueOf(enumerator.getValue()).toString();

                var optionalEObject = variableManager.get(VariableManager.SELF, EObject.class);
                var optionalEStructuralFeature = variableManager.get(PropertiesDefaultDescriptionProvider.ESTRUCTURAL_FEATURE, EStructuralFeature.class);
                if (optionalEObject.isPresent() && optionalEStructuralFeature.isPresent()) {
                    EObject eObject = optionalEObject.get();
                    EStructuralFeature eStructuralFeature = optionalEStructuralFeature.get();

                    Object value = eObject.eGet(eStructuralFeature);
                    if (value instanceof Enumerator) {
                        String selectedLitteralId = Integer.valueOf(((Enumerator) value).getValue()).toString();
                        return optionLitteralId.equals(selectedLitteralId);
                    }
                }
            }
            return false;
        };
    }

    private BiFunction<VariableManager, String, Status> getNewValueHandler() {
        return (variableManager, newValue) -> {
            var optionalEObject = variableManager.get(VariableManager.SELF, EObject.class);
            var optionalEAttribute = variableManager.get(PropertiesDefaultDescriptionProvider.ESTRUCTURAL_FEATURE, EAttribute.class);

            try {
                int id = Integer.valueOf(newValue).intValue();
                if (optionalEObject.isPresent() && optionalEAttribute.isPresent()) {
                    EObject eObject = optionalEObject.get();
                    EAttribute eAttribute = optionalEAttribute.get();
                    EClassifier eType = eAttribute.getEType();
                    if (eType instanceof EEnum) {
                        EEnum eEnum = (EEnum) eType;
                        EEnumLiteral literal = eEnum.getEEnumLiteral(id);
                        if (literal != null) {
                            Object value = EcoreUtil.createFromString(eEnum, literal.getName());
                            eObject.eSet(eAttribute, value);
                        }
                    }
                }
            } catch (NumberFormatException exception) {
                this.logger.error(exception.getMessage(), exception);
                return Status.ERROR;
            }
            return Status.OK;
        };
    }

}
