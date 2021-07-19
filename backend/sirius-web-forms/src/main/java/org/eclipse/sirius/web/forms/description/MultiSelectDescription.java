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
 * The description of the multi-select widget.
 *
 * @author pcdavid
 * @author arichard
 */
@Immutable
public final class MultiSelectDescription extends AbstractWidgetDescription {

    private Function<VariableManager, String> idProvider;

    private Function<VariableManager, String> labelProvider;

    private Function<VariableManager, List<Object>> optionsProvider;

    private Function<VariableManager, String> optionIdProvider;

    private Function<VariableManager, String> optionLabelProvider;

    private Function<VariableManager, List<String>> valuesProvider;

    private BiFunction<VariableManager, List<String>, Status> newValuesHandler;

    private MultiSelectDescription() {
        // Prevent instantiation
    }

    public Function<VariableManager, String> getIdProvider() {
        return this.idProvider;
    }

    public Function<VariableManager, String> getLabelProvider() {
        return this.labelProvider;
    }

    public Function<VariableManager, List<Object>> getOptionsProvider() {
        return this.optionsProvider;
    }

    public Function<VariableManager, String> getOptionIdProvider() {
        return this.optionIdProvider;
    }

    public Function<VariableManager, String> getOptionLabelProvider() {
        return this.optionLabelProvider;
    }

    public Function<VariableManager, List<String>> getValuesProvider() {
        return this.valuesProvider;
    }

    public BiFunction<VariableManager, List<String>, Status> getNewValuesHandler() {
        return this.newValuesHandler;
    }

    public static Builder newMultiSelectDescription(String id) {
        return new Builder(id);
    }

    @Override
    public String toString() {
        String pattern = "{0} '{'id: {1}'}'"; //$NON-NLS-1$
        return MessageFormat.format(pattern, this.getClass().getSimpleName(), this.getId());
    }

    /**
     * Builder used to create the select description.
     *
     * @author pcdavid
     * @author arichard
     */
    @SuppressWarnings("checkstyle:HiddenField")
    public static final class Builder {
        private String id;

        private Function<VariableManager, String> idProvider;

        private Function<VariableManager, String> labelProvider;

        private Function<VariableManager, List<Object>> optionsProvider;

        private Function<VariableManager, String> optionIdProvider;

        private Function<VariableManager, String> optionLabelProvider;

        private Function<VariableManager, List<String>> valuesProvider;

        private BiFunction<VariableManager, List<String>, Status> newValuesHandler;

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

        public Builder optionsProvider(Function<VariableManager, List<Object>> optionsProvider) {
            this.optionsProvider = Objects.requireNonNull(optionsProvider);
            return this;
        }

        public Builder optionIdProvider(Function<VariableManager, String> optionIdProvider) {
            this.optionIdProvider = Objects.requireNonNull(optionIdProvider);
            return this;
        }

        public Builder optionLabelProvider(Function<VariableManager, String> optionLabelProvider) {
            this.optionLabelProvider = Objects.requireNonNull(optionLabelProvider);
            return this;
        }

        public Builder valuesProvider(Function<VariableManager, List<String>> valuesProvider) {
            this.valuesProvider = Objects.requireNonNull(valuesProvider);
            return this;
        }

        public Builder newValuesHandler(BiFunction<VariableManager, List<String>, Status> newValuesHandler) {
            this.newValuesHandler = Objects.requireNonNull(newValuesHandler);
            return this;
        }

        public MultiSelectDescription build() {
            MultiSelectDescription multiSelectDescription = new MultiSelectDescription();
            multiSelectDescription.id = Objects.requireNonNull(this.id);
            multiSelectDescription.idProvider = Objects.requireNonNull(this.idProvider);
            multiSelectDescription.labelProvider = Objects.requireNonNull(this.labelProvider);
            multiSelectDescription.optionsProvider = Objects.requireNonNull(this.optionsProvider);
            multiSelectDescription.optionIdProvider = Objects.requireNonNull(this.optionIdProvider);
            multiSelectDescription.optionLabelProvider = Objects.requireNonNull(this.optionLabelProvider);
            multiSelectDescription.valuesProvider = Objects.requireNonNull(this.valuesProvider);
            multiSelectDescription.newValuesHandler = Objects.requireNonNull(this.newValuesHandler);
            return multiSelectDescription;
        }

    }
}
