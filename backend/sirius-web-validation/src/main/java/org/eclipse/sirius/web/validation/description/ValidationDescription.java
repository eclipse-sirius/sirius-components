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
package org.eclipse.sirius.web.validation.description;

import java.text.MessageFormat;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Predicate;

import org.eclipse.sirius.web.annotations.Immutable;
import org.eclipse.sirius.web.annotations.PublicApi;
import org.eclipse.sirius.web.annotations.graphql.GraphQLField;
import org.eclipse.sirius.web.annotations.graphql.GraphQLID;
import org.eclipse.sirius.web.annotations.graphql.GraphQLNonNull;
import org.eclipse.sirius.web.annotations.graphql.GraphQLObjectType;
import org.eclipse.sirius.web.representations.IRepresentationDescription;
import org.eclipse.sirius.web.representations.VariableManager;

/**
 * The root concept of the description of a validation representation.
 *
 * @author gcoutable
 */
@PublicApi
@Immutable
@GraphQLObjectType
public final class ValidationDescription implements IRepresentationDescription {
    private UUID id;

    private String label;

    private Predicate<VariableManager> canCreatePredicate;

    private Function<VariableManager, List<Object>> diagnosticsProvider;

    private Function<Object, String> kindProvider;

    private Function<Object, String> messageProvider;

    private ValidationDescription() {
        // Prevent instantiation
    }

    @Override
    @GraphQLID
    @GraphQLField
    @GraphQLNonNull
    public UUID getId() {
        return this.id;
    }

    @Override
    @GraphQLField
    @GraphQLNonNull
    public String getLabel() {
        return this.label;
    }

    @Override
    public Predicate<VariableManager> getCanCreatePredicate() {
        return this.canCreatePredicate;
    }

    public Function<VariableManager, List<Object>> getDiagnosticsProvider() {
        return this.diagnosticsProvider;
    }

    public Function<Object, String> getKindProvider() {
        return this.kindProvider;
    }

    public Function<Object, String> getMessageProvider() {
        return this.messageProvider;
    }

    public static Builder newValidationDescription(UUID id) {
        return new Builder(id);
    }

    @Override
    public String toString() {
        String pattern = "{0} '{'id: {1}, label: {2}'}'"; //$NON-NLS-1$
        return MessageFormat.format(pattern, this.getClass().getSimpleName(), this.id, this.label);
    }

    /**
     * Builder used to create the validation description.
     *
     * @author gcoutable
     */
    @SuppressWarnings("checkstyle:HiddenField")
    public static final class Builder {
        private UUID id;

        private String label;

        private Predicate<VariableManager> canCreatePredicate;

        private Function<VariableManager, List<Object>> diagnosticsProvider;

        private Function<Object, String> kindProvider;

        private Function<Object, String> messageProvider;

        public Builder(UUID id) {
            this.id = Objects.requireNonNull(id);
        }

        public Builder label(String label) {
            this.label = Objects.requireNonNull(label);
            return this;
        }

        public Builder canCreatePredicate(Predicate<VariableManager> canCreatePredicate) {
            this.canCreatePredicate = Objects.requireNonNull(canCreatePredicate);
            return this;
        }

        public Builder diagnosticsProvider(Function<VariableManager, List<Object>> diagnosticsProvider) {
            this.diagnosticsProvider = Objects.requireNonNull(diagnosticsProvider);
            return this;
        }

        public Builder kindProvider(Function<Object, String> kindProvider) {
            this.kindProvider = Objects.requireNonNull(kindProvider);
            return this;
        }

        public Builder messageProvider(Function<Object, String> messageProvider) {
            this.messageProvider = Objects.requireNonNull(messageProvider);
            return this;
        }

        public ValidationDescription build() {
            ValidationDescription validationDescription = new ValidationDescription();
            validationDescription.id = Objects.requireNonNull(this.id);
            validationDescription.label = Objects.requireNonNull(this.label);
            validationDescription.canCreatePredicate = Objects.requireNonNull(this.canCreatePredicate);
            validationDescription.diagnosticsProvider = Objects.requireNonNull(this.diagnosticsProvider);
            validationDescription.kindProvider = Objects.requireNonNull(this.kindProvider);
            validationDescription.messageProvider = Objects.requireNonNull(this.messageProvider);
            return validationDescription;
        }

    }

}
