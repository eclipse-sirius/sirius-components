/*******************************************************************************
 * Copyright (c) 2021 Obeo.
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

import org.eclipse.sirius.web.components.IProps;
import org.eclipse.sirius.web.forms.description.MultiSelectDescription;
import org.eclipse.sirius.web.representations.VariableManager;

/**
 * The properties of the multi-select component.
 *
 * @author sbegaudeau
 */
public class MultiSelectComponentProps implements IProps {

    private VariableManager variableManager;

    private MultiSelectDescription selectDescription;

    public MultiSelectComponentProps(VariableManager variableManager, MultiSelectDescription selectDescription) {
        this.variableManager = Objects.requireNonNull(variableManager);
        this.selectDescription = Objects.requireNonNull(selectDescription);
    }

    public VariableManager getVariableManager() {
        return this.variableManager;
    }

    public MultiSelectDescription getMultiSelectDescription() {
        return this.selectDescription;
    }
}
