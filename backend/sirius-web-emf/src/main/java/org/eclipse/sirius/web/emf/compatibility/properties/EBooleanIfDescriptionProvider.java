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
import java.util.function.BiFunction;
import java.util.function.Function;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.sirius.web.compat.forms.WidgetIdProvider;
import org.eclipse.sirius.web.forms.description.CheckboxDescription;
import org.eclipse.sirius.web.forms.description.IfDescription;
import org.eclipse.sirius.web.representations.Status;
import org.eclipse.sirius.web.representations.VariableManager;

/**
 * Provides the default description of the widget to use to support an EBoolean feature.
 *
 * @author sbegaudeau
 */
public class EBooleanIfDescriptionProvider {

    private static final String IF_DESCRIPTION_ID = "EBoolean"; //$NON-NLS-1$

    private static final String CHECKBOX_DESCRIPTION_ID = "Checkbox"; //$NON-NLS-1$

    private final ComposedAdapterFactory composedAdapterFactory;

    public EBooleanIfDescriptionProvider(ComposedAdapterFactory composedAdapterFactory) {
        this.composedAdapterFactory = Objects.requireNonNull(composedAdapterFactory);
    }

    public IfDescription getIfDescription() {
        // @formatter:off
        return IfDescription.newIfDescription(IF_DESCRIPTION_ID)
                .predicate(this.getPredicate())
                .widgetDescription(this.getCheckboxDescription())
                .build();
        // @formatter:on
    }

    private Function<VariableManager, Boolean> getPredicate() {
        return variableManager -> {
            var optionalEAttribute = variableManager.get(PropertiesDefaultDescriptionProvider.ESTRUCTURAL_FEATURE, EAttribute.class);
            return optionalEAttribute.filter(eAttribute -> eAttribute.getEType().equals(EcorePackage.Literals.EBOOLEAN)).isPresent();
        };
    }

    private CheckboxDescription getCheckboxDescription() {
        // @formatter:off
        return CheckboxDescription.newCheckboxDescription(CHECKBOX_DESCRIPTION_ID)
                .idProvider(new WidgetIdProvider())
                .labelProvider(this.getLabelProvider())
                .valueProvider(this.getValueProvider())
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

    private Function<VariableManager, Boolean> getValueProvider() {
        return variableManager -> {
            var optionalEObject = variableManager.get(VariableManager.SELF, EObject.class);
            var optionalEAttribute = variableManager.get(PropertiesDefaultDescriptionProvider.ESTRUCTURAL_FEATURE, EAttribute.class);
            if (optionalEObject.isPresent() && optionalEAttribute.isPresent()) {
                EObject eObject = optionalEObject.get();
                EAttribute eAttribute = optionalEAttribute.get();

                Object value = eObject.eGet(eAttribute);
                return Boolean.valueOf(value.toString());
            }
            return Boolean.FALSE;
        };
    }

    private BiFunction<VariableManager, Boolean, Status> getNewValueHandler() {
        return (variableManager, newValue) -> {
            var optionalEObject = variableManager.get(VariableManager.SELF, EObject.class);
            var optionalEAttribute = variableManager.get(PropertiesDefaultDescriptionProvider.ESTRUCTURAL_FEATURE, EAttribute.class);
            if (optionalEObject.isPresent() && optionalEAttribute.isPresent()) {
                EObject eObject = optionalEObject.get();
                EAttribute eAttribute = optionalEAttribute.get();

                eObject.eSet(eAttribute, newValue);
                return Status.OK;
            }
            return Status.ERROR;
        };
    }

}
