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
import org.eclipse.sirius.web.representations.Status;

/**
 * The properties of the textfield element.
 *
 * @author sbegaudeau
 */
@Immutable
public final class TextfieldElementProps implements IProps {
    public static final String TYPE = "Textfield"; //$NON-NLS-1$

    private String id;

    private String label;

    private String value;

    private Function<String, Status> newValueHandler;

    private List<Element> children;

    private TextfieldElementProps() {
        // Prevent instantiation
    }

    public String getId() {
        return this.id;
    }

    public String getLabel() {
        return this.label;
    }

    public String getValue() {
        return this.value;
    }

    @Override
    public List<Element> getChildren() {
        return this.children;
    }

    public Function<String, Status> getNewValueHandler() {
        return this.newValueHandler;
    }

    public static Builder newTextfieldElementProps(String id) {
        return new Builder(id);
    }

    @Override
    public String toString() {
        String pattern = "{0} '{'id: {1}, label: {2}, value: {3}'}'"; //$NON-NLS-1$
        return MessageFormat.format(pattern, this.getClass().getSimpleName(), this.id, this.label, this.value);
    }

    /**
     * The builder of the textfield element props.
     *
     * @author sbegaudeau
     */
    @SuppressWarnings("checkstyle:HiddenField")
    public static final class Builder {
        private String id;

        private String label;

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

        public Builder value(String value) {
            this.value = Objects.requireNonNull(value);
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

        public TextfieldElementProps build() {
            TextfieldElementProps textfieldElementProps = new TextfieldElementProps();
            textfieldElementProps.id = Objects.requireNonNull(this.id);
            textfieldElementProps.label = Objects.requireNonNull(this.label);
            textfieldElementProps.value = Objects.requireNonNull(this.value);
            textfieldElementProps.newValueHandler = Objects.requireNonNull(this.newValueHandler);
            textfieldElementProps.children = Objects.requireNonNull(this.children);
            return textfieldElementProps;
        }
    }
}
