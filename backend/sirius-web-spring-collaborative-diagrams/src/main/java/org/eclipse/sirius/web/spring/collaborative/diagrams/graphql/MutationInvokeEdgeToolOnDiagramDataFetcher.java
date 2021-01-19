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
package org.eclipse.sirius.web.spring.collaborative.diagrams.graphql;

import java.util.Objects;

import org.eclipse.sirius.web.annotations.graphql.GraphQLMutationTypes;
import org.eclipse.sirius.web.annotations.spring.graphql.MutationDataFetcher;
import org.eclipse.sirius.web.collaborative.api.services.IProjectEventProcessorRegistry;
import org.eclipse.sirius.web.collaborative.diagrams.api.dto.InvokeEdgeToolOnDiagramInput;
import org.eclipse.sirius.web.collaborative.diagrams.api.dto.InvokeEdgeToolOnDiagramSuccessPayload;
import org.eclipse.sirius.web.core.api.ErrorPayload;
import org.eclipse.sirius.web.core.api.IPayload;
import org.eclipse.sirius.web.graphql.datafetchers.IDataFetchingEnvironmentService;
import org.eclipse.sirius.web.graphql.messages.IGraphQLMessageService;
import org.eclipse.sirius.web.graphql.schema.MutationTypeProvider;
import org.eclipse.sirius.web.spring.graphql.api.IDataFetcherWithFieldCoordinates;

import graphql.schema.DataFetchingEnvironment;

/**
 * The data fetcher used to invoke an edge tool on a diagram.
 * <p>
 * It will be used to handle the following GraphQL field:
 * </p>
 *
 * <pre>
 * type Mutation {
 *   invokeEdgeToolOnDiagram(input: InvokeEdgeToolOnDiagramInput!): InvokeEdgeDiagamToolPayload!
 * }
 * </pre>
 *
 * @author pcdavid
 * @author hmarchadour
 */
// @formatter:off
@GraphQLMutationTypes(
    input = InvokeEdgeToolOnDiagramInput.class,
    payloads = {
        InvokeEdgeToolOnDiagramSuccessPayload.class
    }
)
@MutationDataFetcher(type = MutationTypeProvider.TYPE, field = MutationInvokeEdgeToolOnDiagramDataFetcher.INVOKE_EDGE_TOOL_ON_DIAGRAM_FIELD)
// @formatter:on
public class MutationInvokeEdgeToolOnDiagramDataFetcher implements IDataFetcherWithFieldCoordinates<IPayload> {

    public static final String INVOKE_EDGE_TOOL_ON_DIAGRAM_FIELD = "invokeEdgeToolOnDiagram"; //$NON-NLS-1$

    private final IDataFetchingEnvironmentService dataFetchingEnvironmentService;

    private final IProjectEventProcessorRegistry projectEventProcessorRegistry;

    private final IGraphQLMessageService messageService;

    public MutationInvokeEdgeToolOnDiagramDataFetcher(IDataFetchingEnvironmentService dataFetchingEnvironmentService, IProjectEventProcessorRegistry projectEventProcessorRegistry,
            IGraphQLMessageService messageService) {
        this.dataFetchingEnvironmentService = Objects.requireNonNull(dataFetchingEnvironmentService);
        this.projectEventProcessorRegistry = Objects.requireNonNull(projectEventProcessorRegistry);
        this.messageService = Objects.requireNonNull(messageService);
    }

    @Override
    public IPayload get(DataFetchingEnvironment environment) throws Exception {
        var input = this.dataFetchingEnvironmentService.getInput(environment, InvokeEdgeToolOnDiagramInput.class);
        var context = this.dataFetchingEnvironmentService.getContext(environment);

        IPayload payload = new ErrorPayload(this.messageService.unauthorized());
        boolean canEdit = this.dataFetchingEnvironmentService.canEdit(environment, input.getProjectId());
        if (canEdit) {
            // @formatter:off
            payload = this.projectEventProcessorRegistry.dispatchEvent(input.getProjectId(), input, context)
                    .orElse(new ErrorPayload(this.messageService.unexpectedError()));
            // @formatter:on
        }

        return payload;
    }

}
