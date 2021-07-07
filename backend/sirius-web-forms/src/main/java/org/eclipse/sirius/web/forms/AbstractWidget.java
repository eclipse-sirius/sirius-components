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
package org.eclipse.sirius.web.forms;

import org.eclipse.sirius.web.annotations.graphql.GraphQLField;
import org.eclipse.sirius.web.annotations.graphql.GraphQLID;
import org.eclipse.sirius.web.annotations.graphql.GraphQLInterfaceType;
import org.eclipse.sirius.web.annotations.graphql.GraphQLNonNull;
import org.eclipse.sirius.web.forms.validation.Diagnostic;

/**
 * Abstract class to be extended by all the widgets of the form-based representation.
 *
 * @author sbegaudeau
 */
@GraphQLInterfaceType(name = "Widget")
public abstract class AbstractWidget {
    protected String id;

    protected java.util.List<Diagnostic> diagnostics;

    @GraphQLID
    @GraphQLField
    @GraphQLNonNull
    public String getId() {
        return this.id;
    }

    @GraphQLField
    @GraphQLNonNull
    public java.util.List<@GraphQLNonNull Diagnostic> getDiagnostics() {
        return this.diagnostics;
    }
}
