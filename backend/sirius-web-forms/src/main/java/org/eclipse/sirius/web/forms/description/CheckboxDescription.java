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

import java.text.MessageFormat;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.eclipse.sirius.web.annotations.Immutable;
import org.eclipse.sirius.web.representations.Status;
import org.eclipse.sirius.web.representations.VariableManager;

/**
 * The description of the checkbox widget.
 *
 * @author sbegaudeau
 */
@Immutable
public final class CheckboxDescription extends AbstractWidgetDescription {

    private Function<VariableManager, String> idProvider;

    private Function<VariableManager, String> labelProvider;

    private Function<VariableManager, Boolean> valueProvider;

    private BiFunction<VariableManager, Boolean, Status> newValueHandler;

    private CheckboxDescription() {
        // Prevent instantiation
    }

    public Function<VariableManager, String> getIdProvider() {
        return this.idProvider;
    }

    public Function<VariableManager, String> getLabelProvider() {
        return this.labelProvider;
    }

    public Function<VariableManager, Boolean> getValueProvider() {
        return this.valueProvider;
    }

    public BiFunction<VariableManager, Boolean, Status> getNewValueHandler() {
        return this.newValueHandler;
    }

    public static Builder newCheckboxDescription(String id) {
        return new Builder(id);
    }

    @Override
    public String toString() {
        String pattern = "{0} '{'id: {1}'}'"; //$NON-NLS-1$
        return MessageFormat.format(pattern, this.getClass().getSimpleName(), this.getId());
    }

    /**
     * Builder used to create the checkbox description.
     *
     * @author sbegaudeau
     */
    @SuppressWarnings("checkstyle:HiddenField")
    public static final class Builder {
        private String id;

        private Function<VariableManager, String> idProvider;

        private Function<VariableManager, String> labelProvider;

        private Function<VariableManager, Boolean> valueProvider;

        private BiFunction<VariableManager, Boolean, Status> newValueHandler;

        private Function<VariableManager, List<Object>> diagnosticsProvider;

        private Function<Object, String> kindProvider;

        private Function<Object, String> messageProvider;

        private Builder(String id) {
            this.id = Objects.requireNonNull(id);
        }

        public Builder idProvider(Function<VariableManager, String> idProvider) {
            this.idProvider = Objects.requireNonNull(idProvider);
            return this;
        }

        public Builder labelProvider(Function<VariableManager, String> labelProvider) {
            this.labelProvider = Objects.requireNonNull(labelProvider);
            return this;
        }

        public Builder valueProvider(Function<VariableManager, Boolean> valueProvider) {
            this.valueProvider = Objects.requireNonNull(valueProvider);
            return this;
        }

        public Builder newValueHandler(BiFunction<VariableManager, Boolean, Status> newValueHandler) {
            this.newValueHandler = Objects.requireNonNull(newValueHandler);
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

        public CheckboxDescription build() {
            CheckboxDescription checkboxDescription = new CheckboxDescription();
            checkboxDescription.id = Objects.requireNonNull(this.id);
            checkboxDescription.idProvider = Objects.requireNonNull(this.idProvider);
            checkboxDescription.labelProvider = Objects.requireNonNull(this.labelProvider);
            checkboxDescription.valueProvider = Objects.requireNonNull(this.valueProvider);
            checkboxDescription.newValueHandler = Objects.requireNonNull(this.newValueHandler);
            checkboxDescription.diagnosticsProvider = Objects.requireNonNull(this.diagnosticsProvider);
            checkboxDescription.kindProvider = Objects.requireNonNull(this.kindProvider);
            checkboxDescription.messageProvider = Objects.requireNonNull(this.messageProvider);
            return checkboxDescription;
        }
    }
}
