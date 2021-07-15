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
import java.util.Optional;
import java.util.function.Function;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.sirius.web.compat.forms.WidgetIdProvider;
import org.eclipse.sirius.web.compat.services.ImageConstants;
import org.eclipse.sirius.web.core.api.IObjectService;
import org.eclipse.sirius.web.forms.components.ListComponent;
import org.eclipse.sirius.web.forms.description.IfDescription;
import org.eclipse.sirius.web.forms.description.ListDescription;
import org.eclipse.sirius.web.representations.VariableManager;

/**
 * Provides the default description of the widget to use to support multi-valued non-containment reference.
 *
 * @author sbegaudeau
 */
public class MultiValuedNonContainmentReferenceIfDescriptionProvider {

    private static final String ID_DESCRIPTION_ID = "MultiValued NonContainment Reference"; //$NON-NLS-1$

    private static final String LIST_DESCRIPTION_ID = "List"; //$NON-NLS-1$

    private final ComposedAdapterFactory composedAdapterFactory;

    private final IObjectService objectService;

    public MultiValuedNonContainmentReferenceIfDescriptionProvider(ComposedAdapterFactory composedAdapterFactory, IObjectService objectService) {
        this.composedAdapterFactory = Objects.requireNonNull(composedAdapterFactory);
        this.objectService = Objects.requireNonNull(objectService);
    }

    public IfDescription getIfDescription() {
        // @formatter:off
        return IfDescription.newIfDescription(ID_DESCRIPTION_ID)
                .predicate(this.getPredicate())
                .widgetDescription(this.getListDescription())
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

    private ListDescription getListDescription() {
        // @formatter:off
        return ListDescription.newListDescription(LIST_DESCRIPTION_ID)
                .idProvider(new WidgetIdProvider())
                .labelProvider(this.getLabelProvider())
                .itemsProvider(this.getItemsProvider())
                .itemIdProvider(this.getItemIdProvider())
                .itemLabelProvider(this.getItemLabelProvider())
                .itemImageURLProvider(this.getImageURLProvider())
                .diagnosticsProvider((variableManager) -> List.of())
                .kindProvider((object) -> "") //$NON-NLS-1$
                .messageProvider((object) -> "") //$NON-NLS-1$
                .build();
        // @formatter:on
    }

    private Function<VariableManager, String> getLabelProvider() {
        return new EStructuralFeatureLabelProvider(PropertiesDefaultDescriptionProvider.ESTRUCTURAL_FEATURE, this.composedAdapterFactory);
    }

    private Function<VariableManager, String> getImageURLProvider() {
        return variableManager -> {
            // @formatter:off
            return variableManager.get(ListComponent.CANDIDATE_VARIABLE, EObject.class)
                                  .map(this.objectService::getImagePath)
                                  .orElse(ImageConstants.DEFAULT_SVG);
            // @formatter:on
        };
    }

    private Function<VariableManager, List<Object>> getItemsProvider() {
        return variableManager -> {
            Optional<EObject> self = variableManager.get(VariableManager.SELF, EObject.class);
            Optional<EReference> eStructuralFeature = variableManager.get(PropertiesDefaultDescriptionProvider.ESTRUCTURAL_FEATURE, EReference.class);
            if (self.isPresent() && eStructuralFeature.isPresent()) {
                EObject eObject = self.get();
                EReference eReference = eStructuralFeature.get();
                Object result = eObject.eGet(eReference);
                if (result instanceof List<?>) {
                    List<?> list = (List<?>) result;
                    return new ArrayList<>(list);
                }
            }
            return new ArrayList<>();
        };
    }

    private Function<VariableManager, String> getItemIdProvider() {
        return variableManager -> {
            Object object = variableManager.getVariables().get(ListComponent.CANDIDATE_VARIABLE);
            String objectId = this.objectService.getId(object);
            return objectId;
        };
    }

    private Function<VariableManager, String> getItemLabelProvider() {
        return variableManager -> {
            Object object = variableManager.getVariables().get(ListComponent.CANDIDATE_VARIABLE);
            String objectLabel = this.objectService.getFullLabel(object);
            return objectLabel;
        };
    }

}
