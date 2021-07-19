/*******************************************************************************
 * Copyright (c) 2019, 2020 Obeo.
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
 * The description of the select widget.
 *
 * @author lfasani
 */
@Immutable
public final class SelectDescription extends AbstractWidgetDescription {

    private Function<VariableManager, String> idProvider;

    private Function<VariableManager, String> labelProvider;

    private Function<VariableManager, List<Object>> optionsProvider;

    private Function<VariableManager, Boolean> valueRequiredProvider;

    private Function<VariableManager, String> optionIdProvider;

    private Function<VariableManager, String> optionLabelProvider;

    private Function<VariableManager, String> valueProvider;

    private BiFunction<VariableManager, String, Status> newValueHandler;

    private SelectDescription() {
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

    public Function<VariableManager, Boolean> getValueRequiredProvider() {
        return this.valueRequiredProvider;
    }

    public Function<VariableManager, String> getOptionIdProvider() {
        return this.optionIdProvider;
    }

    public Function<VariableManager, String> getOptionLabelProvider() {
        return this.optionLabelProvider;
    }

    public Function<VariableManager, String> getValueProvider() {
        return this.valueProvider;
    }

    public BiFunction<VariableManager, String, Status> getNewValueHandler() {
        return this.newValueHandler;
    }

    public static Builder newSelectDescription(String id) {
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
     * @author lfasani
     */
    @SuppressWarnings("checkstyle:HiddenField")
    public static final class Builder {
        private String id;

        private Function<VariableManager, String> idProvider;

        private Function<VariableManager, String> labelProvider;

        private Function<VariableManager, List<Object>> optionsProvider;

        private Function<VariableManager, Boolean> valueRequiredProvider = variableManager -> false;

        private Function<VariableManager, String> optionIdProvider;

        private Function<VariableManager, String> optionLabelProvider;

        private Function<VariableManager, String> valueProvider;

        private BiFunction<VariableManager, String, Status> newValueHandler;

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

        public Builder valueRequiredProvider(Function<VariableManager, Boolean> valueRequiredProvider) {
            this.valueRequiredProvider = Objects.requireNonNull(valueRequiredProvider);
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

        public Builder valueProvider(Function<VariableManager, String> valueProvider) {
            this.valueProvider = Objects.requireNonNull(valueProvider);
            return this;
        }

        public Builder newValueHandler(BiFunction<VariableManager, String, Status> newValueHandler) {
            this.newValueHandler = Objects.requireNonNull(newValueHandler);
            return this;
        }

        public SelectDescription build() {
            SelectDescription selectDescription = new SelectDescription();
            selectDescription.id = Objects.requireNonNull(this.id);
            selectDescription.idProvider = Objects.requireNonNull(this.idProvider);
            selectDescription.labelProvider = Objects.requireNonNull(this.labelProvider);
            selectDescription.optionsProvider = Objects.requireNonNull(this.optionsProvider);
            selectDescription.valueRequiredProvider = Objects.requireNonNull(this.valueRequiredProvider);
            selectDescription.optionIdProvider = Objects.requireNonNull(this.optionIdProvider);
            selectDescription.optionLabelProvider = Objects.requireNonNull(this.optionLabelProvider);
            selectDescription.valueProvider = Objects.requireNonNull(this.valueProvider);
            selectDescription.newValueHandler = Objects.requireNonNull(this.newValueHandler);
            return selectDescription;
        }

    }
}
