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
package org.eclipse.sirius.web.spring.collaborative.diagrams;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.eclipse.sirius.web.collaborative.api.dto.RenameRepresentationInput;
import org.eclipse.sirius.web.collaborative.api.services.ChangeDescription;
import org.eclipse.sirius.web.collaborative.api.services.ChangeKind;
import org.eclipse.sirius.web.collaborative.api.services.EventHandlerResponse;
import org.eclipse.sirius.web.collaborative.api.services.ISubscriptionManager;
import org.eclipse.sirius.web.collaborative.diagrams.api.IDiagramContext;
import org.eclipse.sirius.web.collaborative.diagrams.api.IDiagramCreationService;
import org.eclipse.sirius.web.collaborative.diagrams.api.IDiagramEventHandler;
import org.eclipse.sirius.web.collaborative.diagrams.api.IDiagramEventProcessor;
import org.eclipse.sirius.web.collaborative.diagrams.api.IDiagramInput;
import org.eclipse.sirius.web.collaborative.diagrams.api.dto.RenameDiagramInput;
import org.eclipse.sirius.web.core.api.IEditingContext;
import org.eclipse.sirius.web.core.api.IInput;
import org.eclipse.sirius.web.core.api.IPayload;
import org.eclipse.sirius.web.core.api.IRepresentationInput;
import org.eclipse.sirius.web.diagrams.Diagram;
import org.eclipse.sirius.web.representations.IRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import reactor.core.publisher.Flux;

/**
 * Reacts to input that target a specific diagram, and {@link #getDiagramUpdates() publishes} updated versions of the
 * diagram to interested subscribers.
 *
 * @author sbegaudeau
 * @author pcdavid
 */
public class DiagramEventProcessor implements IDiagramEventProcessor {

    private final Logger logger = LoggerFactory.getLogger(DiagramEventProcessor.class);

    private final IEditingContext editingContext;

    private final IDiagramContext diagramContext;

    private final List<IDiagramEventHandler> diagramEventHandlers;

    private final ISubscriptionManager subscriptionManager;

    private final IDiagramCreationService diagramCreationService;

    private final DiagramEventFlux diagramEventFlux;

    public DiagramEventProcessor(IEditingContext editingContext, IDiagramContext diagramContext, List<IDiagramEventHandler> diagramEventHandlers, ISubscriptionManager subscriptionManager,
            IDiagramCreationService diagramCreationService) {
        this.editingContext = Objects.requireNonNull(editingContext);
        this.diagramContext = Objects.requireNonNull(diagramContext);
        this.diagramEventHandlers = Objects.requireNonNull(diagramEventHandlers);
        this.subscriptionManager = Objects.requireNonNull(subscriptionManager);
        this.diagramCreationService = Objects.requireNonNull(diagramCreationService);

        // We automatically refresh the representation before using it since things may have changed since the moment it
        // has been saved in the database. This is quite similar to the auto-refresh on loading in Sirius.
        Diagram diagram = this.diagramCreationService.refresh(editingContext, diagramContext).orElse(null);
        diagramContext.update(diagram);
        this.diagramEventFlux = new DiagramEventFlux(diagram);
    }

    @Override
    public IRepresentation getRepresentation() {
        return this.diagramContext.getDiagram();
    }

    @Override
    public ISubscriptionManager getSubscriptionManager() {
        return this.subscriptionManager;
    }

    @Override
    public Optional<EventHandlerResponse> handle(IRepresentationInput representationInput) {
        IRepresentationInput effectiveInput = representationInput;
        if (representationInput instanceof RenameRepresentationInput) {
            RenameRepresentationInput renameRepresentationInput = (RenameRepresentationInput) representationInput;
            effectiveInput = new RenameDiagramInput(renameRepresentationInput.getId(), renameRepresentationInput.getProjectId(), renameRepresentationInput.getRepresentationId(),
                    renameRepresentationInput.getNewLabel());
        }
        if (effectiveInput instanceof IDiagramInput) {
            IDiagramInput diagramInput = (IDiagramInput) effectiveInput;

            Optional<IDiagramEventHandler> optionalDiagramEventHandler = this.diagramEventHandlers.stream().filter(handler -> handler.canHandle(diagramInput)).findFirst();

            if (optionalDiagramEventHandler.isPresent()) {
                IDiagramEventHandler diagramEventHandler = optionalDiagramEventHandler.get();
                EventHandlerResponse eventHandlerResponse = diagramEventHandler.handle(this.editingContext, this.diagramContext, diagramInput);

                this.refresh(representationInput, eventHandlerResponse.getChangeDescription());

                return Optional.of(eventHandlerResponse);
            } else {
                this.logger.warn("No handler found for event: {}", diagramInput); //$NON-NLS-1$
            }
        }
        return Optional.empty();
    }

    @Override
    public void refresh(IInput input, ChangeDescription changeDescription) {
        if (this.shouldRefresh(changeDescription)) {
            Diagram refreshedDiagram = this.diagramCreationService.refresh(this.editingContext, this.diagramContext).orElse(null);
            this.diagramContext.reset();
            this.diagramContext.update(refreshedDiagram);
            this.diagramEventFlux.diagramRefreshed(input, refreshedDiagram);
        }
    }

    /**
     * A diagram is refresh if there is a semantic change or if there is a diagram layout change coming from this very
     * diagram (not other diagrams)
     *
     * @param changeDescription
     *            The change description
     * @return <code>true</code> if the diagram should be refreshed, <code>false</code> otherwise
     */
    private boolean shouldRefresh(ChangeDescription changeDescription) {
        return ChangeKind.SEMANTIC_CHANGE.equals(changeDescription.getKind())
                || (DiagramChangeKind.DIAGRAM_LAYOUT_CHANGE.equals(changeDescription.getKind()) && changeDescription.getSourceId().equals(this.diagramContext.getDiagram().getId()));
    }

    @Override
    public Flux<IPayload> getOutputEvents(IInput input) {
        return Flux.merge(this.diagramEventFlux.getFlux(input), this.subscriptionManager.getFlux(input));
    }

    @Override
    public void dispose() {
        this.subscriptionManager.dispose();
        this.diagramEventFlux.dispose();
    }

    @Override
    public void preDestroy() {
        this.diagramEventFlux.preDestroy();
    }
}
