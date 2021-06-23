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
package org.eclipse.sirius.web.graphql.datafetchers.mutation;

import java.util.Objects;

import org.eclipse.sirius.web.annotations.graphql.GraphQLMutationTypes;
import org.eclipse.sirius.web.annotations.spring.graphql.MutationDataFetcher;
import org.eclipse.sirius.web.collaborative.api.dto.DeleteObjectInput;
import org.eclipse.sirius.web.collaborative.api.dto.DeleteObjectSuccessPayload;
import org.eclipse.sirius.web.collaborative.api.services.IEditingContextEventProcessorRegistry;
import org.eclipse.sirius.web.core.api.ErrorPayload;
import org.eclipse.sirius.web.core.api.IPayload;
import org.eclipse.sirius.web.graphql.datafetchers.IDataFetchingEnvironmentService;
import org.eclipse.sirius.web.graphql.messages.IGraphQLMessageService;
import org.eclipse.sirius.web.graphql.schema.MutationTypeProvider;
import org.eclipse.sirius.web.spring.graphql.api.IDataFetcherWithFieldCoordinates;

import graphql.schema.DataFetchingEnvironment;

/**
 * The data fetcher used to delete an object.
 * <p>
 * It will be used to handle the following GraphQL field:
 * </p>
 *
 * <pre>
 * type Mutation {
 *   deleteObject(projectId: String!, objectId: String!): DeleteObjectPayload!
 * }
 * </pre>
 *
 * @author sbegaudeau
 */
// @formatter:off
@GraphQLMutationTypes(
    input = DeleteObjectInput.class,
    payloads = {
        DeleteObjectSuccessPayload.class
    }
)
@MutationDataFetcher(type = MutationTypeProvider.TYPE, field = MutationDeleteObjectDataFetcher.DELETE_OBJECT_FIELD)
// @formatter:on
public class MutationDeleteObjectDataFetcher implements IDataFetcherWithFieldCoordinates<IPayload> {

    public static final String DELETE_OBJECT_FIELD = "deleteObject"; //$NON-NLS-1$

    private final IDataFetchingEnvironmentService dataFetchingEnvironmentService;

    private final IEditingContextEventProcessorRegistry editingContextEventProcessorRegistry;

    private final IGraphQLMessageService messageService;

    public MutationDeleteObjectDataFetcher(IDataFetchingEnvironmentService dataFetchingEnvironmentService, IEditingContextEventProcessorRegistry editingContextEventProcessorRegistry,
            IGraphQLMessageService messageService) {
        this.dataFetchingEnvironmentService = Objects.requireNonNull(dataFetchingEnvironmentService);
        this.editingContextEventProcessorRegistry = Objects.requireNonNull(editingContextEventProcessorRegistry);
        this.messageService = Objects.requireNonNull(messageService);
    }

    @Override
    public IPayload get(DataFetchingEnvironment environment) throws Exception {
        var input = this.dataFetchingEnvironmentService.getInput(environment, DeleteObjectInput.class);

        IPayload payload = new ErrorPayload(input.getId(), this.messageService.unauthorized());
        boolean canEdit = this.dataFetchingEnvironmentService.canEdit(environment, input.getEditingContextId());
        if (canEdit) {
            // @formatter:off
            payload = this.editingContextEventProcessorRegistry.dispatchEvent(input.getEditingContextId(), input)
                    .orElse(new ErrorPayload(input.getId(), this.messageService.unexpectedError()));
            // @formatter:on
        }

        return payload;
    }
}
