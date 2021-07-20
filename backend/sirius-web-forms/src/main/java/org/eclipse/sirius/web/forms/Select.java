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

import java.text.MessageFormat;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import org.eclipse.sirius.web.annotations.Immutable;
import org.eclipse.sirius.web.annotations.graphql.GraphQLField;
import org.eclipse.sirius.web.annotations.graphql.GraphQLNonNull;
import org.eclipse.sirius.web.annotations.graphql.GraphQLObjectType;
import org.eclipse.sirius.web.forms.validation.Diagnostic;
import org.eclipse.sirius.web.representations.Status;

/**
 * The select widget.
 *
 * @author lfasani
 */
@Immutable
@GraphQLObjectType
public final class Select extends AbstractWidget {
    private String label;

    private List<SelectOption> options;

    private String value;

    private Function<String, Status> newValueHandler;

    private Select() {
        // Prevent instantiation
    }

    @GraphQLField
    @GraphQLNonNull
    public String getLabel() {
        return this.label;
    }

    @GraphQLField
    @GraphQLNonNull
    public List<@GraphQLNonNull SelectOption> getOptions() {
        return this.options;
    }

    @GraphQLField
    public String getValue() {
        return this.value;
    }

    public Function<String, Status> getNewValueHandler() {
        return this.newValueHandler;
    }

    public static Builder newSelect(String id) {
        return new Builder(id);
    }

    @Override
    public String toString() {
        String pattern = "{0} '{'id: {1}, label: {2}, value: {3}, options: {4}'}'"; //$NON-NLS-1$
        return MessageFormat.format(pattern, this.getClass().getSimpleName(), this.getId(), this.label, this.value, this.options);
    }

    /**
     * The builder used to create the select.
     *
     * @author lfasani
     */
    @SuppressWarnings("checkstyle:HiddenField")
    public static final class Builder {
        private String id;

        private String label;

        private List<SelectOption> options;

        private String value;

        private Function<String, Status> newValueHandler;

        private List<Diagnostic> diagnostics;

        private Builder(String id) {
            this.id = Objects.requireNonNull(id);
        }

        public Builder label(String label) {
            this.label = Objects.requireNonNull(label);
            return this;
        }

        public Builder options(List<SelectOption> options) {
            this.options = Objects.requireNonNull(options);
            return this;
        }

        public Builder value(String value) {
            this.value = value;
            return this;
        }

        public Builder newValueHandler(Function<String, Status> newValueHandler) {
            this.newValueHandler = Objects.requireNonNull(newValueHandler);
            return this;
        }

        public Builder diagnostics(List<Diagnostic> diagnostics) {
            this.diagnostics = Objects.requireNonNull(diagnostics);
            return this;
        }

        public Select build() {
            Select select = new Select();
            select.id = Objects.requireNonNull(this.id);
            select.label = Objects.requireNonNull(this.label);
            select.options = Objects.requireNonNull(this.options);
            select.value = this.value;
            select.newValueHandler = Objects.requireNonNull(this.newValueHandler);
            select.diagnostics = Objects.requireNonNull(this.diagnostics);
            return select;
        }
    }
}
