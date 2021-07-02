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
package org.eclipse.sirius.web.collaborative.selection.api.dto;

import java.text.MessageFormat;
import java.util.Objects;
import java.util.UUID;

import org.eclipse.sirius.web.annotations.graphql.GraphQLField;
import org.eclipse.sirius.web.annotations.graphql.GraphQLID;
import org.eclipse.sirius.web.annotations.graphql.GraphQLNonNull;
import org.eclipse.sirius.web.annotations.graphql.GraphQLObjectType;
import org.eclipse.sirius.web.core.api.IPayload;
import org.eclipse.sirius.web.selection.Selection;

/**
 * Payload used to indicate that the selection has been refreshed.
 *
 * @author arichard
 */
@GraphQLObjectType
public final class SelectionRefreshedEventPayload implements IPayload {
    private final UUID id;

    private final Selection selection;

    public SelectionRefreshedEventPayload(UUID id, Selection selection) {
        this.id = Objects.requireNonNull(id);
        this.selection = Objects.requireNonNull(selection);
    }

    @Override
    @GraphQLID
    @GraphQLField
    @GraphQLNonNull
    public UUID getId() {
        return this.id;
    }

    @GraphQLField
    @GraphQLNonNull
    public Selection getSelection() {
        return this.selection;
    }

    @Override
    public String toString() {
        String pattern = "{0} '{'id: {1}, selection: '{'id: {2}, label: {3}'}''}'"; //$NON-NLS-1$
        return MessageFormat.format(pattern, this.getClass().getSimpleName(), this.id, this.selection.getId(), this.selection.getLabel());
    }
}
