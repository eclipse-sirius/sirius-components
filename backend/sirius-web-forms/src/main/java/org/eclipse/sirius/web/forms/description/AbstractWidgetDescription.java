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
package org.eclipse.sirius.web.forms.description;

import java.util.List;
import java.util.function.Function;

import org.eclipse.sirius.web.representations.VariableManager;

/**
 * The common superclass of all the widget descriptions.
 *
 * @author sbegaudeau
 */
public abstract class AbstractWidgetDescription extends AbstractControlDescription {

    protected Function<VariableManager, List<Object>> diagnosticsProviders;

    protected Function<Object, String> kindProvider;

    protected Function<Object, String> messageProvider;

    public Function<VariableManager, List<Object>> getDiagnosticsProviders() {
        return this.diagnosticsProviders;
    }

    public Function<Object, String> getKindProvider() {
        return this.kindProvider;
    }

    public Function<Object, String> getMessageProvider() {
        return this.messageProvider;
    }
}
