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

import java.util.Objects;

import org.eclipse.sirius.web.components.Element;
import org.eclipse.sirius.web.components.IComponent;
import org.eclipse.sirius.web.forms.description.AbstractWidgetDescription;
import org.eclipse.sirius.web.forms.description.CheckboxDescription;
import org.eclipse.sirius.web.forms.description.ListDescription;
import org.eclipse.sirius.web.forms.description.MultiSelectDescription;
import org.eclipse.sirius.web.forms.description.RadioDescription;
import org.eclipse.sirius.web.forms.description.SelectDescription;
import org.eclipse.sirius.web.forms.description.TextareaDescription;
import org.eclipse.sirius.web.forms.description.TextfieldDescription;
import org.eclipse.sirius.web.representations.VariableManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The component used to render the widget.
 *
 * @author sbegaudeau
 */
public class WidgetComponent implements IComponent {

    private final Logger logger = LoggerFactory.getLogger(WidgetComponent.class);

    private final WidgetComponentProps props;

    public WidgetComponent(WidgetComponentProps props) {
        this.props = Objects.requireNonNull(props);
    }

    @Override
    public Element render() {
        VariableManager variableManager = this.props.getVariableManager();
        AbstractWidgetDescription widgetDescription = this.props.getWidgetDescription();

        Element element = null;
        if (widgetDescription instanceof TextfieldDescription) {
            TextfieldComponentProps textfieldProps = new TextfieldComponentProps(variableManager, (TextfieldDescription) widgetDescription);
            element = new Element(TextfieldComponent.class, textfieldProps);
        } else if (widgetDescription instanceof TextareaDescription) {
            TextareaComponentProps textareaProps = new TextareaComponentProps(variableManager, (TextareaDescription) widgetDescription);
            element = new Element(TextareaComponent.class, textareaProps);
        } else if (widgetDescription instanceof CheckboxDescription) {
            CheckboxComponentProps checkboxProps = new CheckboxComponentProps(variableManager, (CheckboxDescription) widgetDescription);
            element = new Element(CheckboxComponent.class, checkboxProps);
        } else if (widgetDescription instanceof SelectDescription) {
            SelectComponentProps selectProps = new SelectComponentProps(variableManager, (SelectDescription) widgetDescription);
            element = new Element(SelectComponent.class, selectProps);
        } else if (widgetDescription instanceof MultiSelectDescription) {
            MultiSelectComponentProps multiSelectProps = new MultiSelectComponentProps(variableManager, (MultiSelectDescription) widgetDescription);
            element = new Element(MultiSelectComponent.class, multiSelectProps);
        } else if (widgetDescription instanceof RadioDescription) {
            RadioComponentProps radioProps = new RadioComponentProps(variableManager, (RadioDescription) widgetDescription);
            element = new Element(RadioComponent.class, radioProps);
        } else if (widgetDescription instanceof ListDescription) {
            ListComponentProps listProps = new ListComponentProps(variableManager, (ListDescription) widgetDescription);
            element = new Element(ListComponent.class, listProps);
        } else {
            String pattern = "Unsupported widget description: {}"; //$NON-NLS-1$
            this.logger.warn(pattern, widgetDescription.getClass().getSimpleName());
        }
        return element;
    }
}
