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
package org.eclipse.sirius.web.diagrams;

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
import org.eclipse.sirius.web.representations.ISemanticRepresentation;

/**
 * Root concept of the diagram representation.
 *
 * @author sbegaudeau
 */
@Immutable
@GraphQLObjectType
public final class Diagram implements IRepresentation, ISemanticRepresentation {

    public static final String KIND = "Diagram"; //$NON-NLS-1$

    private UUID id;

    private String kind;

    private String targetObjectId;

    private UUID descriptionId;

    private String label;

    private Position position;

    private Size size;

    private boolean autoLayout;

    private List<Node> nodes;

    private List<Edge> edges;

    private Diagram() {
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
    @GraphQLField
    @GraphQLNonNull
    public String getKind() {
        return this.kind;
    }

    @Override
    @GraphQLID
    @GraphQLField
    @GraphQLNonNull
    public String getTargetObjectId() {
        return this.targetObjectId;
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
    public String getLabel() {
        return this.label;
    }

    @GraphQLField
    @GraphQLNonNull
    public Position getPosition() {
        return this.position;
    }

    @GraphQLField
    @GraphQLNonNull
    public Size getSize() {
        return this.size;
    }

    @GraphQLField
    @GraphQLNonNull
    public boolean isAutoLayout() {
        return this.autoLayout;
    }

    @GraphQLField
    @GraphQLNonNull
    public List<@GraphQLNonNull Node> getNodes() {
        return this.nodes;
    }

    @GraphQLField
    @GraphQLNonNull
    public List<@GraphQLNonNull Edge> getEdges() {
        return this.edges;
    }

    public static Builder newDiagram(UUID id) {
        return new Builder(id);
    }

    public static Builder newDiagram(Diagram diagram) {
        return new Builder(diagram);
    }

    @Override
    public String toString() {
        String pattern = "{0} '{'id: {1}, targetObjectId: {2}, descriptionId: {3}, label: {4}, nodeCount: {5}, edgeCount: {6}'}'"; //$NON-NLS-1$
        return MessageFormat.format(pattern, this.getClass().getSimpleName(), this.id, this.targetObjectId, this.descriptionId, this.label, this.nodes.size(), this.edges.size());
    }

    /**
     * The builder used to create a diagram.
     *
     * @author sbegaudeau
     */
    @SuppressWarnings("checkstyle:HiddenField")
    public static final class Builder {
        private UUID id;

        private String kind = KIND;

        private String targetObjectId;

        private UUID descriptionId;

        private String label;

        private Position position;

        private Size size;

        private boolean autoLayout;

        private List<Node> nodes;

        private List<Edge> edges;

        private Builder(UUID id) {
            this.id = Objects.requireNonNull(id);
        }

        private Builder(Diagram diagram) {
            this.id = diagram.getId();
            this.targetObjectId = diagram.getTargetObjectId();
            this.descriptionId = diagram.getDescriptionId();
            this.label = diagram.getLabel();
            this.position = diagram.getPosition();
            this.size = diagram.getSize();
            this.autoLayout = diagram.autoLayout;
            this.nodes = diagram.getNodes();
            this.edges = diagram.getEdges();
        }

        public Builder targetObjectId(String targetObjectId) {
            this.targetObjectId = Objects.requireNonNull(targetObjectId);
            return this;
        }

        public Builder descriptionId(UUID descriptionId) {
            this.descriptionId = Objects.requireNonNull(descriptionId);
            return this;
        }

        public Builder label(String label) {
            this.label = Objects.requireNonNull(label);
            return this;
        }

        public Builder position(Position position) {
            this.position = Objects.requireNonNull(position);
            return this;
        }

        public Builder size(Size size) {
            this.size = Objects.requireNonNull(size);
            return this;
        }

        public Builder autoLayout(boolean autoLayout) {
            this.autoLayout = Objects.requireNonNull(autoLayout);
            return this;
        }

        public Builder nodes(List<Node> nodes) {
            this.nodes = Objects.requireNonNull(nodes);
            return this;
        }

        public Builder edges(List<Edge> edges) {
            this.edges = Objects.requireNonNull(edges);
            return this;
        }

        public Diagram build() {
            Diagram diagram = new Diagram();
            diagram.id = Objects.requireNonNull(this.id);
            diagram.kind = Objects.requireNonNull(this.kind);
            diagram.targetObjectId = Objects.requireNonNull(this.targetObjectId);
            diagram.descriptionId = Objects.requireNonNull(this.descriptionId);
            diagram.label = Objects.requireNonNull(this.label);
            diagram.position = Objects.requireNonNull(this.position);
            diagram.size = Objects.requireNonNull(this.size);
            diagram.autoLayout = this.autoLayout;
            diagram.nodes = Objects.requireNonNull(this.nodes);
            diagram.edges = Objects.requireNonNull(this.edges);
            return diagram;
        }
    }
}
