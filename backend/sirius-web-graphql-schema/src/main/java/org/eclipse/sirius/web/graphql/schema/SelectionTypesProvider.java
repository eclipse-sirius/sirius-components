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

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.sirius.web.graphql.utils.providers.GraphQLObjectTypeProvider;
import org.eclipse.sirius.web.graphql.utils.schema.ITypeProvider;
import org.eclipse.sirius.web.selection.Selection;
import org.eclipse.sirius.web.selection.SelectionObject;
import org.eclipse.sirius.web.selection.description.SelectionDescription;
import org.springframework.stereotype.Service;

import graphql.schema.GraphQLType;

/**
 *
 * This class is used to create all the definitions of the types related to the selection representation.
 *
 * @author arichard
 */
@Service
public class SelectionTypesProvider implements ITypeProvider {

    public static final String SELECTION_TYPE = "Selection"; //$NON-NLS-1$

    public static final String SELECTION_OBJECT_TYPE = "SelectionObject"; //$NON-NLS-1$

    public static final String TARGET_FIELD = "target"; //$NON-NLS-1$

    private final GraphQLObjectTypeProvider graphQLObjectTypeProvider = new GraphQLObjectTypeProvider();

    @Override
    public Set<GraphQLType> getTypes() {
        // @formatter:off
        List<Class<?>> objectClasses = List.of(
            Selection.class,
            SelectionObject.class,
            SelectionDescription.class
        );
        var graphQLObjectTypes = objectClasses.stream()
                .map(this.graphQLObjectTypeProvider::getType)
                .collect(Collectors.toUnmodifiableList());
        // @formatter:on

        Set<GraphQLType> types = new LinkedHashSet<>();
        types.addAll(graphQLObjectTypes);
        return types;
    }
}
