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
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.eclipse.sirius.web.annotations.Immutable;
import org.eclipse.sirius.web.annotations.graphql.GraphQLField;
import org.eclipse.sirius.web.annotations.graphql.GraphQLID;
import org.eclipse.sirius.web.annotations.graphql.GraphQLNonNull;
import org.eclipse.sirius.web.annotations.graphql.GraphQLObjectType;
import org.eclipse.sirius.web.representations.IRepresentation;

/**
 * The root concept of the selection representation.
 *
 * @author arichard
 */
@Immutable
@GraphQLObjectType
public final class Selection implements IRepresentation {

    public static final String KIND = "Selection"; //$NON-NLS-1$

    private UUID id;

    private String kind;

    private UUID descriptionId;

    private String label;

    private String targetObjectId;

    private String message;

    private List<SelectionObject> objects;

    private Selection() {
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
    @GraphQLID
    @GraphQLField
    @GraphQLNonNull
    public UUID getDescriptionId() {
        return this.descriptionId;
    }

    @Override
    @GraphQLField
    @GraphQLNonNull
    public String getKind() {
        return this.kind;
    }

    @Override
    @GraphQLField
    @GraphQLNonNull
    public String getLabel() {
        return this.label;
    }

    @GraphQLField
    @GraphQLNonNull
    public String getTargetObjectId() {
        return this.targetObjectId;
    }

    @GraphQLField
    public String getMessage() {
        return this.message;
    }

    @GraphQLField
    @GraphQLNonNull
    public List<@GraphQLNonNull SelectionObject> getObjects() {
        return this.objects;
    }

    public static Builder newSelection(UUID id) {
        return new Builder(id);
    }

    @Override
    public String toString() {
        String pattern = "{0} '{'id: {1}, descriptionId: {2}, targetObjectId: {3}, label: {4}, message: {5}, objectsCount: {6}'}'"; //$NON-NLS-1$
        return MessageFormat.format(pattern, this.getClass().getSimpleName(), this.id, this.descriptionId, this.targetObjectId, this.label, this.message, this.objects.size());
    }

    /**
     * The builder used to create the selection.
     *
     * @author arichard
     */
    @SuppressWarnings("checkstyle:HiddenField")
    public static final class Builder {
        private UUID id;

        private String kind = KIND;

        private UUID descriptionId;

        private String label;

        private String targetObjectId;

        private String message;

        private List<SelectionObject> objects;

        private Builder(UUID id) {
            this.id = Objects.requireNonNull(id);
        }

        public Builder descriptionId(UUID descriptionId) {
            this.descriptionId = Objects.requireNonNull(descriptionId);
            return this;
        }

        public Builder label(String label) {
            this.label = Objects.requireNonNull(label);
            return this;
        }

        public Builder targetObjectId(String targetObjectId) {
            this.targetObjectId = Objects.requireNonNull(targetObjectId);
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder objects(List<SelectionObject> objects) {
            this.objects = Objects.requireNonNull(objects);
            return this;
        }

        public Selection build() {
            Selection selection = new Selection();
            selection.id = Objects.requireNonNull(this.id);
            selection.kind = Objects.requireNonNull(this.kind);
            selection.descriptionId = Objects.requireNonNull(this.descriptionId);
            selection.label = Objects.requireNonNull(this.label);
            selection.targetObjectId = Objects.requireNonNull(this.targetObjectId);
            selection.message = this.message;
            selection.objects = Objects.requireNonNull(this.objects);
            return selection;
        }
    }
}
