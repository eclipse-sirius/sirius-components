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
package org.eclipse.sirius.web.forms;

import java.text.MessageFormat;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import org.eclipse.sirius.web.annotations.Immutable;
import org.eclipse.sirius.web.annotations.graphql.GraphQLField;
import org.eclipse.sirius.web.annotations.graphql.GraphQLNonNull;
import org.eclipse.sirius.web.annotations.graphql.GraphQLObjectType;
import org.eclipse.sirius.web.representations.Status;

/**
 * The select widget.
 *
 * @author pcdavid
 * @author arichard
 */
@Immutable
@GraphQLObjectType
public final class MultiSelect extends AbstractWidget {
    private String label;

    private List<SelectOption> options;

    private List<String> values;

    private Function<List<String>, Status> newValuesHandler;

    private MultiSelect() {
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
    public List<String> getValues() {
        return this.values;
    }

    public Function<List<String>, Status> getNewValuesHandler() {
        return this.newValuesHandler;
    }

    public static Builder newMultiSelect(String id) {
        return new Builder(id);
    }

    @Override
    public String toString() {
        String pattern = "{0} '{'id: {1}, label: {2}, values: {3}, options: {4}'}'"; //$NON-NLS-1$
        return MessageFormat.format(pattern, this.getClass().getSimpleName(), this.getId(), this.label, this.values, this.options);
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

        private List<String> values;

        private Function<List<String>, Status> newValuesHandler;

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

        public Builder values(List<String> values) {
            this.values = List.copyOf(values);
            return this;
        }

        public Builder newValuesHandler(Function<List<String>, Status> newValuesHandler) {
            this.newValuesHandler = Objects.requireNonNull(newValuesHandler);
            return this;
        }

        public MultiSelect build() {
            MultiSelect select = new MultiSelect();
            select.id = Objects.requireNonNull(this.id);
            select.label = Objects.requireNonNull(this.label);
            select.options = Objects.requireNonNull(this.options);
            select.values = this.values;
            select.newValuesHandler = Objects.requireNonNull(this.newValuesHandler);
            return select;
        }
    }
}
