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
package org.eclipse.sirius.web.selection;

import java.text.MessageFormat;
import java.util.Objects;
import java.util.UUID;

import org.eclipse.sirius.web.annotations.Immutable;
import org.eclipse.sirius.web.annotations.graphql.GraphQLField;
import org.eclipse.sirius.web.annotations.graphql.GraphQLID;
import org.eclipse.sirius.web.annotations.graphql.GraphQLNonNull;
import org.eclipse.sirius.web.annotations.graphql.GraphQLObjectType;

/**
 * An object of the selection.
 *
 * @author arichard
 */
@Immutable
@GraphQLObjectType
public final class SelectionObject {

    private UUID id;

    private String label;

    private String iconURL;

    private SelectionObject() {
        // Prevent instantiation
    }

    @GraphQLID
    @GraphQLField
    @GraphQLNonNull
    public UUID getId() {
        return this.id;
    }

    @GraphQLField
    @GraphQLNonNull
    public String getLabel() {
        return this.label;
    }

    @GraphQLField
    @GraphQLNonNull
    public String getIconURL() {
        return this.iconURL;
    }

    public static Builder newSelectionObject(UUID id) {
        return new Builder(id);
    }

    @Override
    public String toString() {
        String pattern = "{0} '{'id: {1}, label: {2}, imageURL: {3}'}'"; //$NON-NLS-1$
        return MessageFormat.format(pattern, this.getClass().getSimpleName(), this.id, this.label, this.iconURL);
    }

    /**
     * The builder used to create the selection object.
     *
     * @author arichard
     */
    @SuppressWarnings("checkstyle:HiddenField")
    public static final class Builder {
        private UUID id;

        private String label;

        private String iconURL;

        private Builder(UUID id) {
            this.id = Objects.requireNonNull(id);
        }

        public Builder label(String label) {
            this.label = Objects.requireNonNull(label);
            return this;
        }

        public Builder iconURL(String iconURL) {
            this.iconURL = Objects.requireNonNull(iconURL);
            return this;
        }

        public SelectionObject build() {
            SelectionObject selectionObject = new SelectionObject();
            selectionObject.id = Objects.requireNonNull(this.id);
            selectionObject.label = Objects.requireNonNull(this.label);
            selectionObject.iconURL = Objects.requireNonNull(this.iconURL);
            return selectionObject;
        }
    }
}
