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
import java.util.function.Function;

import org.eclipse.sirius.web.annotations.Immutable;
import org.eclipse.sirius.web.representations.VariableManager;

/**
 * The description of the list widget.
 *
 * @author sbegaudeau
 */
@Immutable
public final class ListDescription extends AbstractWidgetDescription {

    private Function<VariableManager, String> idProvider;

    private Function<VariableManager, String> labelProvider;

    private Function<VariableManager, List<Object>> itemsProvider;

    private Function<VariableManager, String> itemIdProvider;

    private Function<VariableManager, String> itemLabelProvider;

    private Function<VariableManager, String> itemImageURLProvider;

    private ListDescription() {
        // Prevent instantiation
    }

    public Function<VariableManager, String> getIdProvider() {
        return this.idProvider;
    }

    public Function<VariableManager, String> getLabelProvider() {
        return this.labelProvider;
    }

    public Function<VariableManager, List<Object>> getItemsProvider() {
        return this.itemsProvider;
    }

    public Function<VariableManager, String> getItemIdProvider() {
        return this.itemIdProvider;
    }

    public Function<VariableManager, String> getItemLabelProvider() {
        return this.itemLabelProvider;
    }

    public Function<VariableManager, String> getItemImageURLProvider() {
        return this.itemImageURLProvider;
    }

    public static Builder newListDescription(String id) {
        return new Builder(id);
    }

    @Override
    public String toString() {
        String pattern = "{0} '{'id: {1}'}'"; //$NON-NLS-1$
        return MessageFormat.format(pattern, this.getClass().getSimpleName(), this.getId());
    }

    /**
     * Builder used to create the list description.
     *
     * @author sbegaudeau
     */
    @SuppressWarnings("checkstyle:HiddenField")
    public static final class Builder {
        private String id;

        private Function<VariableManager, String> idProvider;

        private Function<VariableManager, String> labelProvider;

        private Function<VariableManager, List<Object>> itemsProvider;

        private Function<VariableManager, String> itemIdProvider;

        private Function<VariableManager, String> itemLabelProvider;

        private Function<VariableManager, String> itemImageURLProvider;

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

        public Builder itemsProvider(Function<VariableManager, List<Object>> itemsProvider) {
            this.itemsProvider = Objects.requireNonNull(itemsProvider);
            return this;
        }

        public Builder itemIdProvider(Function<VariableManager, String> itemIdProvider) {
            this.itemIdProvider = Objects.requireNonNull(itemIdProvider);
            return this;
        }

        public Builder itemLabelProvider(Function<VariableManager, String> itemLabelProvider) {
            this.itemLabelProvider = Objects.requireNonNull(itemLabelProvider);
            return this;
        }

        public Builder itemImageURLProvider(Function<VariableManager, String> itemImageURLProvider) {
            this.itemImageURLProvider = Objects.requireNonNull(itemImageURLProvider);
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

        public ListDescription build() {
            ListDescription listDescription = new ListDescription();
            listDescription.id = Objects.requireNonNull(this.id);
            listDescription.idProvider = Objects.requireNonNull(this.idProvider);
            listDescription.labelProvider = Objects.requireNonNull(this.labelProvider);
            listDescription.itemsProvider = Objects.requireNonNull(this.itemsProvider);
            listDescription.itemIdProvider = Objects.requireNonNull(this.itemIdProvider);
            listDescription.itemLabelProvider = Objects.requireNonNull(this.itemLabelProvider);
            listDescription.itemImageURLProvider = Objects.requireNonNull(this.itemImageURLProvider);
            listDescription.diagnosticsProviders = Objects.requireNonNull(this.diagnosticsProviders);
            listDescription.kindProvider = Objects.requireNonNull(this.kindProvider);
            listDescription.messageProvider = Objects.requireNonNull(this.messageProvider);
            return listDescription;
        }
    }
}
