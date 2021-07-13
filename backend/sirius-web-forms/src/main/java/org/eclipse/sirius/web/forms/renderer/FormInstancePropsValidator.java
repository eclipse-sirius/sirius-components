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

import org.eclipse.sirius.web.components.IInstancePropsValidator;
import org.eclipse.sirius.web.components.IProps;
import org.eclipse.sirius.web.forms.elements.CheckboxElementProps;
import org.eclipse.sirius.web.forms.elements.FormElementProps;
import org.eclipse.sirius.web.forms.elements.GroupElementProps;
import org.eclipse.sirius.web.forms.elements.ListElementProps;
import org.eclipse.sirius.web.forms.elements.MultiSelectElementProps;
import org.eclipse.sirius.web.forms.elements.PageElementProps;
import org.eclipse.sirius.web.forms.elements.RadioElementProps;
import org.eclipse.sirius.web.forms.elements.SelectElementProps;
import org.eclipse.sirius.web.forms.elements.TextareaElementProps;
import org.eclipse.sirius.web.forms.elements.TextfieldElementProps;

/**
 * Used to validate the instance props.
 *
 * @author sbegaudeau
 */
public class FormInstancePropsValidator implements IInstancePropsValidator {

    @Override
    public boolean validateInstanceProps(String type, IProps props) {
        boolean checkValidProps = false;

        if (FormElementProps.TYPE.equals(type)) {
            checkValidProps = props instanceof FormElementProps;
        } else if (PageElementProps.TYPE.equals(type)) {
            checkValidProps = props instanceof PageElementProps;
        } else if (GroupElementProps.TYPE.equals(type)) {
            checkValidProps = props instanceof GroupElementProps;
        } else if (CheckboxElementProps.TYPE.equals(type)) {
            checkValidProps = props instanceof CheckboxElementProps;
        } else if (ListElementProps.TYPE.equals(type)) {
            checkValidProps = props instanceof ListElementProps;
        } else if (RadioElementProps.TYPE.equals(type)) {
            checkValidProps = props instanceof RadioElementProps;
        } else if (SelectElementProps.TYPE.equals(type)) {
            checkValidProps = props instanceof SelectElementProps;
        } else if (MultiSelectElementProps.TYPE.equals(type)) {
            checkValidProps = props instanceof MultiSelectElementProps;
        } else if (TextareaElementProps.TYPE.equals(type)) {
            checkValidProps = props instanceof TextareaElementProps;
        } else if (TextfieldElementProps.TYPE.equals(type)) {
            checkValidProps = props instanceof TextfieldElementProps;
        }

        return checkValidProps;
    }

}
