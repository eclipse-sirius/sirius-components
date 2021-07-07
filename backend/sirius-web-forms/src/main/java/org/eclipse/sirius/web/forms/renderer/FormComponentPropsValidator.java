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
package org.eclipse.sirius.web.forms.renderer;

import org.eclipse.sirius.web.components.IComponentPropsValidator;
import org.eclipse.sirius.web.components.IProps;
import org.eclipse.sirius.web.forms.components.CheckboxComponent;
import org.eclipse.sirius.web.forms.components.CheckboxComponentProps;
import org.eclipse.sirius.web.forms.components.ForComponent;
import org.eclipse.sirius.web.forms.components.ForComponentProps;
import org.eclipse.sirius.web.forms.components.FormComponent;
import org.eclipse.sirius.web.forms.components.FormComponentProps;
import org.eclipse.sirius.web.forms.components.GroupComponent;
import org.eclipse.sirius.web.forms.components.GroupComponentProps;
import org.eclipse.sirius.web.forms.components.IfComponent;
import org.eclipse.sirius.web.forms.components.IfComponentProps;
import org.eclipse.sirius.web.forms.components.ListComponent;
import org.eclipse.sirius.web.forms.components.ListComponentProps;
import org.eclipse.sirius.web.forms.components.PageComponent;
import org.eclipse.sirius.web.forms.components.PageComponentProps;
import org.eclipse.sirius.web.forms.components.RadioComponent;
import org.eclipse.sirius.web.forms.components.RadioComponentProps;
import org.eclipse.sirius.web.forms.components.SelectComponent;
import org.eclipse.sirius.web.forms.components.SelectComponentProps;
import org.eclipse.sirius.web.forms.components.TextareaComponent;
import org.eclipse.sirius.web.forms.components.TextareaComponentProps;
import org.eclipse.sirius.web.forms.components.TextfieldComponent;
import org.eclipse.sirius.web.forms.components.TextfieldComponentProps;
import org.eclipse.sirius.web.forms.components.WidgetComponent;
import org.eclipse.sirius.web.forms.components.WidgetComponentProps;
import org.eclipse.sirius.web.forms.validation.DiagnosticComponent;
import org.eclipse.sirius.web.forms.validation.DiagnosticComponentProps;

/**
 * Used to validate the properties of a component.
 *
 * @author sbegaudeau
 */
public class FormComponentPropsValidator implements IComponentPropsValidator {

    @Override
    public boolean validateComponentProps(Class<?> componentType, IProps props) {
        boolean checkValidProps = false;

        if (FormComponent.class.equals(componentType)) {
            checkValidProps = props instanceof FormComponentProps;
        } else if (PageComponent.class.equals(componentType)) {
            checkValidProps = props instanceof PageComponentProps;
        } else if (GroupComponent.class.equals(componentType)) {
            checkValidProps = props instanceof GroupComponentProps;
        } else if (ForComponent.class.equals(componentType)) {
            checkValidProps = props instanceof ForComponentProps;
        } else if (IfComponent.class.equals(componentType)) {
            checkValidProps = props instanceof IfComponentProps;
        } else if (WidgetComponent.class.equals(componentType)) {
            checkValidProps = props instanceof WidgetComponentProps;
        } else if (CheckboxComponent.class.equals(componentType)) {
            checkValidProps = props instanceof CheckboxComponentProps;
        } else if (ListComponent.class.equals(componentType)) {
            checkValidProps = props instanceof ListComponentProps;
        } else if (RadioComponent.class.equals(componentType)) {
            checkValidProps = props instanceof RadioComponentProps;
        } else if (SelectComponent.class.equals(componentType)) {
            checkValidProps = props instanceof SelectComponentProps;
        } else if (TextareaComponent.class.equals(componentType)) {
            checkValidProps = props instanceof TextareaComponentProps;
        } else if (TextfieldComponent.class.equals(componentType)) {
            checkValidProps = props instanceof TextfieldComponentProps;
        } else if (DiagnosticComponent.class.equals(componentType)) {
            checkValidProps = props instanceof DiagnosticComponentProps;
        }

        return checkValidProps;
    }

}
