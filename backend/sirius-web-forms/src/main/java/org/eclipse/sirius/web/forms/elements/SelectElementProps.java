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
package org.eclipse.sirius.web.forms.elements;

import java.text.MessageFormat;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import org.eclipse.sirius.web.annotations.Immutable;
import org.eclipse.sirius.web.components.Element;
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

    private String value;

    private Function<String, Status> newValueHandler;

    private List<Element> children;

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

    public String getValue() {
        return this.value;
    }

    public Function<String, Status> getNewValueHandler() {
        return this.newValueHandler;
    }

    @Override
    public List<Element> getChildren() {
        return this.children;
    }

    public static Builder newSelectElementProps(String id) {
        return new Builder(id);
    }

    @Override
    public String toString() {
        String pattern = "{0} '{'id: {1}, label: {2}, value: {3}, options:{4}'}'"; //$NON-NLS-1$
        return MessageFormat.format(pattern, this.getClass().getSimpleName(), this.id, this.label, this.value, this.options);
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

        private String value;

        private Function<String, Status> newValueHandler;

        private List<Element> children;

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

        public Builder newValueHandler(Function<String, Status> handler) {
            this.newValueHandler = Objects.requireNonNull(handler);
            return this;
        }

        public Builder children(List<Element> children) {
            this.children = Objects.requireNonNull(children);
            return this;
        }

        public SelectElementProps build() {
            SelectElementProps selectElementProps = new SelectElementProps();
            selectElementProps.id = Objects.requireNonNull(this.id);
            selectElementProps.label = Objects.requireNonNull(this.label);
            selectElementProps.options = Objects.requireNonNull(this.options);
            selectElementProps.value = this.value;
            selectElementProps.newValueHandler = Objects.requireNonNull(this.newValueHandler);
            selectElementProps.children = Objects.requireNonNull(this.children);
            return selectElementProps;
        }
    }
}
