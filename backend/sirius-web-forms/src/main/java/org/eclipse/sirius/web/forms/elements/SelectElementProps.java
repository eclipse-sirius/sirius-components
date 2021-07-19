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
package org.eclipse.sirius.web.forms.elements;

import java.text.MessageFormat;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import org.eclipse.sirius.web.annotations.Immutable;
import org.eclipse.sirius.web.components.IProps;
import org.eclipse.sirius.web.forms.SelectOption;
import org.eclipse.sirius.web.representations.Status;

/**
 * The properties of the select element.
 *
 * @author sbegaudeau
 */
@Immutable
public final class SelectElementProps implements IProps {
    public static final String TYPE = "Select"; //$NON-NLS-1$

    private String id;

    private String label;

    private List<SelectOption> options;

    private boolean valueRequired;

    private String value;

    private Function<String, Status> newValueHandler;

    private SelectElementProps() {
        // Prevent instantiation
    }

    public String getId() {
        return this.id;
    }

    public String getLabel() {
        return this.label;
    }

    public List<SelectOption> getOptions() {
        return this.options;
    }

    public boolean isValueRequired() {
        return this.valueRequired;
    }

    public String getValue() {
        return this.value;
    }

    public Function<String, Status> getNewValueHandler() {
        return this.newValueHandler;
    }

    public static Builder newSelectElementProps(String id) {
        return new Builder(id);
    }

    @Override
    public String toString() {
        String pattern = "{0} '{'id: {1}, label: {2}, value: {3}, valueRequired: {4}, options:{5}'}'"; //$NON-NLS-1$
        return MessageFormat.format(pattern, this.getClass().getSimpleName(), this.id, this.label, this.value, this.valueRequired, this.options);
    }

    /**
     * The builder of the select element props.
     *
     * @author lfasani
     */
    @SuppressWarnings("checkstyle:HiddenField")
    public static final class Builder {

        private String id;

        private String label;

        private List<SelectOption> options;

        private boolean valueRequired;

        private String value;

        private Function<String, Status> newValueHandler;

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

        public Builder valueRequired(boolean valueRequired) {
            this.valueRequired = valueRequired;
            return this;
        }

        public Builder value(String value) {
            this.value = value;
            return this;
        }

        public Builder newValueHandler(Function<String, Status> handler) {
            this.newValueHandler = Objects.requireNonNull(handler);
            return this;
        }

        public SelectElementProps build() {
            SelectElementProps selectElementProps = new SelectElementProps();
            selectElementProps.id = Objects.requireNonNull(this.id);
            selectElementProps.label = Objects.requireNonNull(this.label);
            selectElementProps.options = Objects.requireNonNull(this.options);
            selectElementProps.valueRequired = this.valueRequired;
            selectElementProps.value = this.value;
            selectElementProps.newValueHandler = Objects.requireNonNull(this.newValueHandler);
            return selectElementProps;
        }
    }
}
