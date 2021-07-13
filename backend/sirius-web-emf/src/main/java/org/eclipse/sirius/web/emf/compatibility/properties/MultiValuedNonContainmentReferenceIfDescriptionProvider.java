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

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.sirius.web.compat.forms.WidgetIdProvider;
import org.eclipse.sirius.web.core.api.IEditingContext;
import org.eclipse.sirius.web.core.api.IObjectService;
import org.eclipse.sirius.web.forms.components.SelectComponent;
import org.eclipse.sirius.web.forms.description.IfDescription;
import org.eclipse.sirius.web.forms.description.MultiSelectDescription;
import org.eclipse.sirius.web.representations.Status;
import org.eclipse.sirius.web.representations.VariableManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides the default description of the widget to use to support multi-valued non-containment reference.
 *
 * @author sbegaudeau
 */
public class MultiValuedNonContainmentReferenceIfDescriptionProvider {

    private static final String ID_DESCRIPTION_ID = "MultiValued NonContainment Reference"; //$NON-NLS-1$

    private static final String MULTI_SELECT_DESCRIPTION_ID = "MultiSelect"; //$NON-NLS-1$

    private final ComposedAdapterFactory composedAdapterFactory;

    private final IObjectService objectService;

    private final Logger logger = LoggerFactory.getLogger(MultiValuedNonContainmentReferenceIfDescriptionProvider.class);

    public MultiValuedNonContainmentReferenceIfDescriptionProvider(ComposedAdapterFactory composedAdapterFactory, IObjectService objectService) {
        this.composedAdapterFactory = Objects.requireNonNull(composedAdapterFactory);
        this.objectService = Objects.requireNonNull(objectService);
    }

    public IfDescription getIfDescription() {
        // @formatter:off
        return IfDescription.newIfDescription(ID_DESCRIPTION_ID)
                .predicate(this.getPredicate())
                .widgetDescription(this.getMultiSelectDescription())
                .build();
        // @formatter:on
    }

    private Function<VariableManager, Boolean> getPredicate() {
        return variableManager -> {
            var optionalEReference = variableManager.get(PropertiesDefaultDescriptionProvider.ESTRUCTURAL_FEATURE, EReference.class);
            return optionalEReference.filter(eReference -> {
                boolean isCandidate = true;
                isCandidate = isCandidate && eReference.isMany();
                isCandidate = isCandidate && !eReference.isContainment();
                return isCandidate;
            }).isPresent();
        };
    }

    private MultiSelectDescription getMultiSelectDescription() {
        // @formatter:off
        return MultiSelectDescription.newMultiSelectDescription(MULTI_SELECT_DESCRIPTION_ID)
                .idProvider(new WidgetIdProvider())
                .labelProvider(this.getLabelProvider())
                .valuesProvider(this.getValuesProvider())
                .optionsProvider(this.getOptionsProvider())
                .optionIdProvider(this.getOptionIdProvider())
                .optionLabelProvider(this.getOptionLabelProvider())
                .newValuesHandler(this.getNewValuesHandler())
                .build();
        // @formatter:on
    }

    private BiFunction<VariableManager, List<String>, Status> getNewValuesHandler() {
        return (variableManager, newValues) -> {
            var status = Status.ERROR;
            var optionalEObject = variableManager.get(VariableManager.SELF, EObject.class);
            var optionalEReference = variableManager.get(PropertiesDefaultDescriptionProvider.ESTRUCTURAL_FEATURE, EReference.class);
            var optionalEditingContext = variableManager.get(IEditingContext.EDITING_CONTEXT, IEditingContext.class);

            if (optionalEObject.isPresent() && optionalEReference.isPresent()) {
                EObject eObject = optionalEObject.get();
                EReference eReference = optionalEReference.get();
                EList<EObject> refElements = (EList<EObject>) eObject.eGet(eReference);
                List<EObject> newValuesToSet = new ArrayList<>();

                for (String newValue : newValues) {
                    // @formatter:off
                    var optionalNewValueToSet = optionalEditingContext.flatMap(context -> this.objectService.getObject(context, newValue))
                            .filter(EObject.class::isInstance)
                            .map(EObject.class::cast);
                    // @formatter:on
                    if (optionalNewValueToSet.isPresent()) {
                        EObject newValueToSet = optionalNewValueToSet.get();
                        newValuesToSet.add(newValueToSet);
                        try {
                            if (!refElements.contains(newValueToSet)) {
                                refElements.add(newValueToSet);
                            }
                        } catch (IllegalArgumentException | ClassCastException | ArrayStoreException e) {
                            this.logger.error(e.getMessage(), e);
                        }
                    } else {
                        this.logger.error("The " + newValue + " cannot be retrieved and set to " + eReference.getName() + " of " + eObject.toString()); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                    }
                }

                refElements.removeIf(refElt -> !newValuesToSet.contains(refElt));

                status = Status.OK;
            }
            return status;
        };
    }

    private Function<VariableManager, String> getLabelProvider() {
        return new EStructuralFeatureLabelProvider(PropertiesDefaultDescriptionProvider.ESTRUCTURAL_FEATURE, this.composedAdapterFactory);
    }

    private Function<VariableManager, List<String>> getValuesProvider() {
        return variableManager -> {
            Object object = variableManager.getVariables().get(VariableManager.SELF);
            Object eStructuralFeature = variableManager.getVariables().get(PropertiesDefaultDescriptionProvider.ESTRUCTURAL_FEATURE);

            if (object instanceof EObject && eStructuralFeature instanceof EReference) {
                EObject eObject = (EObject) object;
                EReference eReference = (EReference) eStructuralFeature;

                Object value = eObject.eGet(eReference);
                if (value instanceof EList<?>) {
                    return ((EList<?>) value).stream().map(this.objectService::getId).collect(Collectors.toList());
                }
            }
            return null;
        };
    }

    private Function<VariableManager, List<Object>> getOptionsProvider() {
        return new EStructuralFeatureChoiceOfValueProvider(PropertiesDefaultDescriptionProvider.ESTRUCTURAL_FEATURE, this.composedAdapterFactory);
    }

    private Function<VariableManager, String> getOptionIdProvider() {
        return variableManager -> {
            Object object = variableManager.getVariables().get(SelectComponent.CANDIDATE_VARIABLE);
            String objectId = this.objectService.getId(object);
            return objectId;
        };
    }

    private Function<VariableManager, String> getOptionLabelProvider() {
        return variableManager -> {
            Object object = variableManager.getVariables().get(SelectComponent.CANDIDATE_VARIABLE);
            String objectLabel = this.objectService.getFullLabel(object);
            return objectLabel;
        };
    }

}
