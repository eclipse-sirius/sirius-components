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
 * The description of the text area widget.
 *
 * @author lfasani
 */
@Immutable
public final class TextareaDescription extends AbstractWidgetDescription {
    private Function<VariableManager, String> idProvider;

    private Function<VariableManager, String> labelProvider;

    private Function<VariableManager, String> valueProvider;

    private BiFunction<VariableManager, String, Status> newValueHandler;

    private TextareaDescription() {
        // Prevent instantiation
    }

    public Function<VariableManager, String> getIdProvider() {
        return this.idProvider;
    }

    public Function<VariableManager, String> getLabelProvider() {
        return this.labelProvider;
    }

    public Function<VariableManager, String> getValueProvider() {
        return this.valueProvider;
    }

    public BiFunction<VariableManager, String, Status> getNewValueHandler() {
        return this.newValueHandler;
    }

    public static Builder newTextareaDescription(String id) {
        return new Builder(id);
    }

    @Override
    public String toString() {
        String pattern = "{0} '{'id: {1}'}'"; //$NON-NLS-1$
        return MessageFormat.format(pattern, this.getClass().getSimpleName(), this.getId());
    }

    /**
     * Builder used to create the textarea description.
     *
     * @author lfasani
     */
    @SuppressWarnings("checkstyle:HiddenField")
    public static final class Builder {

        private String id;

        private Function<VariableManager, String> idProvider;

        private Function<VariableManager, String> labelProvider;

        private Function<VariableManager, String> valueProvider;

        private BiFunction<VariableManager, String, Status> newValueHandler;

        private Function<VariableManager, List<Object>> diagnosticsProviders;

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

        public Builder valueProvider(Function<VariableManager, String> valueProvider) {
            this.valueProvider = Objects.requireNonNull(valueProvider);
            return this;
        }

        public Builder newValueHandler(BiFunction<VariableManager, String, Status> newValueHandler) {
            this.newValueHandler = Objects.requireNonNull(newValueHandler);
            return this;
        }

        public Builder diagnosticsProviders(Function<VariableManager, List<Object>> diagnosticsProviders) {
            this.diagnosticsProviders = Objects.requireNonNull(diagnosticsProviders);
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

        public TextareaDescription build() {
            TextareaDescription textareaDescription = new TextareaDescription();
            textareaDescription.id = Objects.requireNonNull(this.id);
            textareaDescription.idProvider = Objects.requireNonNull(this.idProvider);
            textareaDescription.labelProvider = Objects.requireNonNull(this.labelProvider);
            textareaDescription.valueProvider = Objects.requireNonNull(this.valueProvider);
            textareaDescription.newValueHandler = Objects.requireNonNull(this.newValueHandler);
            textareaDescription.diagnosticsProviders = Objects.requireNonNull(this.diagnosticsProviders);
            textareaDescription.kindProvider = Objects.requireNonNull(this.kindProvider);
            textareaDescription.messageProvider = Objects.requireNonNull(this.messageProvider);
            return textareaDescription;
        }
    }
}
