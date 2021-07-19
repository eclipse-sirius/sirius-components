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
package org.eclipse.sirius.web.graphql.datafetchers.selection;

import org.eclipse.sirius.web.annotations.spring.graphql.QueryDataFetcher;
import org.eclipse.sirius.web.graphql.schema.IconURLFieldProvider;
import org.eclipse.sirius.web.graphql.schema.SelectionTypesProvider;
import org.eclipse.sirius.web.selection.SelectionObject;
import org.eclipse.sirius.web.spring.graphql.api.IDataFetcherWithFieldCoordinates;
import org.eclipse.sirius.web.spring.graphql.api.URLConstants;

import graphql.schema.DataFetchingEnvironment;

/**
 * The data fetcher used to concatenate the server image URL to the SelectionObject icon path.
 * <p>
 * It will be used to fetch the data for the following GraphQL field:
 * </p>
 *
 * <pre>
 * type SelectionObject {
 *   iconURL: String!
 * }
 * </pre>
 *
 * @author arichard
 */
@QueryDataFetcher(type = SelectionTypesProvider.SELECTION_OBJECT_TYPE, field = IconURLFieldProvider.ICON_URL_FIELD)
public class SelectionObjectIconURLDataFetcher implements IDataFetcherWithFieldCoordinates<String> {

    @Override
    public String get(DataFetchingEnvironment environment) throws Exception {
        SelectionObject selectionObject = environment.getSource();
        return URLConstants.IMAGE_BASE_PATH + selectionObject.getIconURL();
    }
}
