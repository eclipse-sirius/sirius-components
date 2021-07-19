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
package org.eclipse.sirius.web.diagrams.tools;

import java.text.MessageFormat;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import org.eclipse.sirius.web.annotations.Immutable;
import org.eclipse.sirius.web.annotations.graphql.GraphQLField;
import org.eclipse.sirius.web.annotations.graphql.GraphQLID;
import org.eclipse.sirius.web.annotations.graphql.GraphQLNonNull;
import org.eclipse.sirius.web.annotations.graphql.GraphQLObjectType;
import org.eclipse.sirius.web.diagrams.description.NodeDescription;
import org.eclipse.sirius.web.representations.Status;
import org.eclipse.sirius.web.representations.VariableManager;

/**
 * A create node Tool implementation.
 *
 * @author pcdavid
 * @author hmarchadour
 */
@Immutable
@GraphQLObjectType
public final class CreateNodeTool implements ITool {

    private String id;

    private String imageURL;

    private String label;

    private Function<VariableManager, Status> handler;

    private List<NodeDescription> targetDescriptions;

    private boolean appliesToDiagramRoot;

    private String selectionDescriptionId;

    private CreateNodeTool() {
        // Prevent instantiation
    }

    @GraphQLField
    @GraphQLNonNull
    public List<@GraphQLNonNull NodeDescription> getTargetDescriptions() {
        return this.targetDescriptions;
    }

    @GraphQLField
    @GraphQLNonNull
    public boolean isAppliesToDiagramRoot() {
        return this.appliesToDiagramRoot;
    }

    @Override
    @GraphQLID
    @GraphQLField
    @GraphQLNonNull
    public String getId() {
        return this.id;
    }

    // This field is defined in DiagramTypesProvider to add the server base URL prefix.
    @Override
    @GraphQLField
    @GraphQLNonNull
    public String getImageURL() {
        return this.imageURL;
    }

    @Override
    @GraphQLField
    @GraphQLNonNull
    public String getLabel() {
        return this.label;
    }

    @GraphQLField
    public String getSelectionDescriptionId() {
        return this.selectionDescriptionId;
    }

    @Override
    public Function<VariableManager, Status> getHandler() {
        return this.handler;
    }

    @Override
    public String toString() {
        String pattern = "{0} '{'id: {1}, label: {2}, imageURL: {3}'}'"; //$NON-NLS-1$
        return MessageFormat.format(pattern, this.getClass().getSimpleName(), this.id, this.label, this.imageURL);
    }

    public static Builder newCreateNodeTool(String id) {
        return new Builder(id);
    }

    /**
     * The builder used to create a tool.
     *
     * @author pcdavid
     */
    @SuppressWarnings("checkstyle:HiddenField")
    public static final class Builder {
        private String id;

        private String imageURL;

        private String label;

        private Function<VariableManager, Status> handler;

        private List<NodeDescription> targetDescriptions;

        private boolean appliesToDiagramRoot;

        private String selectionDescriptionId;

        private Builder(String id) {
            this.id = Objects.requireNonNull(id);
        }

        public Builder imageURL(String imageURL) {
            this.imageURL = Objects.requireNonNull(imageURL);
            return this;
        }

        public Builder label(String label) {
            this.label = Objects.requireNonNull(label);
            return this;
        }

        public Builder targetDescriptions(List<NodeDescription> targetDescriptions) {
            this.targetDescriptions = Objects.requireNonNull(targetDescriptions);
            return this;
        }

        public Builder appliesToDiagramRoot(boolean appliesToDiagramRoot) {
            this.appliesToDiagramRoot = appliesToDiagramRoot;
            return this;
        }

        public Builder handler(Function<VariableManager, Status> handler) {
            this.handler = Objects.requireNonNull(handler);
            return this;
        }

        public Builder selectionDescriptionId(String selectionDescriptionId) {
            this.selectionDescriptionId = selectionDescriptionId;
            return this;
        }

        public CreateNodeTool build() {
            CreateNodeTool tool = new CreateNodeTool();
            tool.id = Objects.requireNonNull(this.id);
            tool.imageURL = Objects.requireNonNull(this.imageURL);
            tool.label = Objects.requireNonNull(this.label);
            tool.handler = Objects.requireNonNull(this.handler);
            tool.targetDescriptions = Objects.requireNonNull(this.targetDescriptions);
            tool.appliesToDiagramRoot = this.appliesToDiagramRoot;
            tool.selectionDescriptionId = this.selectionDescriptionId;
            return tool;
        }
    }
}
