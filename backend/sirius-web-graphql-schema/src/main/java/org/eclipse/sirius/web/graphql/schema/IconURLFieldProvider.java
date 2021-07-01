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
package org.eclipse.sirius.web.graphql.schema;

import graphql.Scalars;
import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.GraphQLNonNull;

/**
 * This class is used to create the definition of the iconURL field of any kind of Object.
 * <p>
 * It will be used to add the following field to any Object:
 * </p>
 *
 * <pre>
 * iconURL: String!
 * </pre>
 *
 * @author arichard
 */
public class IconURLFieldProvider {

    public static final String ICON_URL_FIELD = "iconURL"; //$NON-NLS-1$

    public GraphQLFieldDefinition getField() {
        // @formatter:off
        return GraphQLFieldDefinition.newFieldDefinition()
                .name(ICON_URL_FIELD)
                .type(new GraphQLNonNull(Scalars.GraphQLString))
                .build();
        // @formatter:on
    }

}
