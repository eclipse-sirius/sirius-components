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
package org.eclipse.sirius.web.forms.validation;

import java.util.Objects;
import java.util.UUID;

import org.eclipse.sirius.web.annotations.Immutable;
import org.eclipse.sirius.web.annotations.graphql.GraphQLField;
import org.eclipse.sirius.web.annotations.graphql.GraphQLID;
import org.eclipse.sirius.web.annotations.graphql.GraphQLNonNull;
import org.eclipse.sirius.web.annotations.graphql.GraphQLObjectType;

/**
 * The diagnostic of a widget.
 *
 * @author gcoutable
 */
@Immutable
@GraphQLObjectType
public final class Diagnostic {

    private UUID id;

    private String kind;

    private String message;

    private Diagnostic() {
        // Prevent instantiation
    }

    @GraphQLID
    @GraphQLField
    @GraphQLNonNull
    public UUID getId() {
        return this.id;
    }

    @GraphQLField
    @GraphQLNonNull
    public String getKind() {
        return this.kind;
    }

    @GraphQLField
    @GraphQLNonNull
    public String getMessage() {
        return this.message;
    }

    public static Builder newDiagnostic(UUID id) {
        return new Builder(id);
    }

    /**
     * The builder used to create a new diagnostic.
     *
     * @author gcoutable
     */
    @SuppressWarnings("checkstyle:HiddenField")
    public static final class Builder {

        private UUID id;

        private String kind;

        private String message;

        public Builder(UUID id) {
            this.id = Objects.requireNonNull(id);
        }

        public Builder kind(String kind) {
            this.kind = Objects.requireNonNull(kind);
            return this;
        }

        public Builder message(String message) {
            this.message = Objects.requireNonNull(message);
            return this;
        }

        public Diagnostic build() {
            Diagnostic diagnostic = new Diagnostic();
            diagnostic.id = Objects.requireNonNull(this.id);
            diagnostic.kind = Objects.requireNonNull(this.kind);
            diagnostic.message = Objects.requireNonNull(this.message);
            return diagnostic;
        }

    }
}
