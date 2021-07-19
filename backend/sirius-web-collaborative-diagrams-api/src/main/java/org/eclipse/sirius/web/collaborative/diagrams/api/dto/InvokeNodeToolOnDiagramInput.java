/*******************************************************************************
 * Copyright (c) 2019, 2021 Obeo and others.
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
package org.eclipse.sirius.web.collaborative.diagrams.api.dto;

import java.text.MessageFormat;
import java.util.UUID;

import org.eclipse.sirius.web.annotations.graphql.GraphQLField;
import org.eclipse.sirius.web.annotations.graphql.GraphQLID;
import org.eclipse.sirius.web.annotations.graphql.GraphQLInputObjectType;
import org.eclipse.sirius.web.annotations.graphql.GraphQLNonNull;
import org.eclipse.sirius.web.collaborative.diagrams.api.IDiagramInput;

/**
 * The input for the "Invoke a node tool on diagram" mutation.
 *
 * @author pcdavid
 */
@GraphQLInputObjectType
public final class InvokeNodeToolOnDiagramInput implements IDiagramInput {
    private UUID id;

    private UUID editingContextId;

    private UUID representationId;

    private UUID diagramElementId;

    private String toolId;

    private double startingPositionX;

    private double startingPositionY;

    private String selectedObjectId;

    @Override
    @GraphQLID
    @GraphQLField
    @GraphQLNonNull
    public UUID getId() {
        return this.id;
    }

    @GraphQLID
    @GraphQLField
    @GraphQLNonNull
    public UUID getEditingContextId() {
        return this.editingContextId;
    }

    @Override
    @GraphQLID
    @GraphQLField
    @GraphQLNonNull
    public UUID getRepresentationId() {
        return this.representationId;
    }

    @GraphQLID
    @GraphQLField
    @GraphQLNonNull
    public UUID getDiagramElementId() {
        return this.diagramElementId;
    }

    @GraphQLID
    @GraphQLField
    @GraphQLNonNull
    public String getToolId() {
        return this.toolId;
    }

    @GraphQLField
    @GraphQLNonNull
    public double getStartingPositionX() {
        return this.startingPositionX;
    }

    @GraphQLField
    @GraphQLNonNull
    public double getStartingPositionY() {
        return this.startingPositionY;
    }

    @GraphQLField
    public String getSelectedObjectId() {
        return this.selectedObjectId;
    }

    @Override
    public String toString() {
        String pattern = "{0} '{'id: {1}, editingContextId: {2}, representationId: {3}, diagramElementId: {4}, toolId: {5}'}'"; //$NON-NLS-1$
        return MessageFormat.format(pattern, this.getClass().getSimpleName(), this.id, this.editingContextId, this.representationId, this.diagramElementId, this.toolId);
    }

}
