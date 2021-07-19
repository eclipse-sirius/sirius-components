/*******************************************************************************
 * Copyright (c) 2019, 2020 Obeo.
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
package org.eclipse.sirius.web.forms.components;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import org.eclipse.sirius.web.components.Element;
import org.eclipse.sirius.web.components.IComponent;
import org.eclipse.sirius.web.forms.SelectOption;
import org.eclipse.sirius.web.forms.description.SelectDescription;
import org.eclipse.sirius.web.forms.elements.SelectElementProps;
import org.eclipse.sirius.web.representations.Status;
import org.eclipse.sirius.web.representations.VariableManager;

/**
 * The component used to create the select widget and its options.
 *
 * @author sbegaudeau
 */
public class SelectComponent implements IComponent {

    public static final String CANDIDATE_VARIABLE = "candidate"; //$NON-NLS-1$

    private SelectComponentProps props;

    public SelectComponent(SelectComponentProps props) {
        this.props = Objects.requireNonNull(props);
    }

    @Override
    public Element render() {
        VariableManager variableManager = this.props.getVariableManager();
        SelectDescription selectDescription = this.props.getSelectDescription();

        String id = selectDescription.getIdProvider().apply(variableManager);
        String label = selectDescription.getLabelProvider().apply(variableManager);
        List<Object> optionCandidates = selectDescription.getOptionsProvider().apply(variableManager);
        boolean valueRequired = selectDescription.getValueRequiredProvider().apply(variableManager);
        String value = selectDescription.getValueProvider().apply(variableManager);

        List<SelectOption> options = new ArrayList<>();
        for (Object candidate : optionCandidates) {
            VariableManager optionVariableManager = variableManager.createChild();
            optionVariableManager.put(CANDIDATE_VARIABLE, candidate);

            String optionId = selectDescription.getOptionIdProvider().apply(optionVariableManager);
            String optionLabel = selectDescription.getOptionLabelProvider().apply(optionVariableManager);

            // @formatter:off
            SelectOption option = SelectOption.newSelectOption(optionId)
                    .label(optionLabel)
                    .build();
            // @formatter:on

            options.add(option);
        }
        Function<String, Status> specializedHandler = newValue -> {
            return selectDescription.getNewValueHandler().apply(variableManager, newValue);
        };

        // @formatter:off
        SelectElementProps selectElementProps = SelectElementProps.newSelectElementProps(id)
                .label(label)
                .options(options)
                .valueRequired(valueRequired)
                .value(value)
                .newValueHandler(specializedHandler)
                .build();
        return new Element(SelectElementProps.TYPE, selectElementProps);
        // @formatter:on
    }
}
