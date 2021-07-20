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
package org.eclipse.sirius.web.forms.components;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import org.eclipse.sirius.web.components.Element;
import org.eclipse.sirius.web.components.IComponent;
import org.eclipse.sirius.web.forms.description.TextareaDescription;
import org.eclipse.sirius.web.forms.elements.TextareaElementProps;
import org.eclipse.sirius.web.forms.validation.DiagnosticComponent;
import org.eclipse.sirius.web.forms.validation.DiagnosticComponentProps;
import org.eclipse.sirius.web.representations.Status;
import org.eclipse.sirius.web.representations.VariableManager;

/**
 * The component used to create the textarea widget.
 *
 * @author sbegaudeau
 */
public class TextareaComponent implements IComponent {

    private TextareaComponentProps props;

    public TextareaComponent(TextareaComponentProps props) {
        this.props = Objects.requireNonNull(props);
    }

    @Override
    public Element render() {
        VariableManager variableManager = this.props.getVariableManager();
        TextareaDescription textareaDescription = this.props.getTextareaDescription();

        String id = textareaDescription.getIdProvider().apply(variableManager);
        String label = textareaDescription.getLabelProvider().apply(variableManager);
        String value = textareaDescription.getValueProvider().apply(variableManager);
        Function<String, Status> specializedHandler = newValue -> {
            return textareaDescription.getNewValueHandler().apply(variableManager, newValue);
        };

        List<Element> children = List.of(new Element(DiagnosticComponent.class, new DiagnosticComponentProps(textareaDescription, variableManager)));

        // @formatter:off
        TextareaElementProps textareaElementProps = TextareaElementProps.newTextareaElementProps(id)
                .label(label)
                .value(value)
                .newValueHandler(specializedHandler)
                .children(children)
                .build();
        return new Element(TextareaElementProps.TYPE, textareaElementProps);
        // @formatter:on
    }
}
