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
package org.eclipse.sirius.web.spring.collaborative.selection;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.sirius.web.collaborative.api.services.ChangeDescription;
import org.eclipse.sirius.web.collaborative.api.services.ChangeKind;
import org.eclipse.sirius.web.collaborative.api.services.EventHandlerResponse;
import org.eclipse.sirius.web.collaborative.api.services.ISubscriptionManager;
import org.eclipse.sirius.web.collaborative.selection.api.ISelectionEventProcessor;
import org.eclipse.sirius.web.collaborative.selection.api.dto.SelectionRefreshedEventPayload;
import org.eclipse.sirius.web.core.api.IEditingContext;
import org.eclipse.sirius.web.core.api.IInput;
import org.eclipse.sirius.web.core.api.IPayload;
import org.eclipse.sirius.web.core.api.IRepresentationInput;
import org.eclipse.sirius.web.representations.IRepresentation;
import org.eclipse.sirius.web.representations.VariableManager;
import org.eclipse.sirius.web.selection.Selection;
import org.eclipse.sirius.web.selection.description.SelectionDescription;
import org.eclipse.sirius.web.selection.renderer.SelectionRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import reactor.core.publisher.Sinks.EmitResult;
import reactor.core.publisher.Sinks.Many;

/**
 * Reacts to the input that target the selection and publishes updated versions of the {@link Selection} to interested
 * subscribers.
 *
 * @author arichard
 */
public class SelectionEventProcessor implements ISelectionEventProcessor {

    private final Logger logger = LoggerFactory.getLogger(SelectionEventProcessor.class);

    private final IEditingContext editingContext;

    private final SelectionDescription selectionDescription;

    private final UUID selectionId;

    private final Object object;

    private final ISubscriptionManager subscriptionManager;

    private final Many<IPayload> sink = Sinks.many().multicast().directBestEffort();

    private final Many<Boolean> canBeDisposedSink = Sinks.many().unicast().onBackpressureBuffer();

    private final AtomicReference<Selection> currentSelection = new AtomicReference<>();

    public SelectionEventProcessor(IEditingContext editingContext, SelectionDescription selectionDescription, UUID selectionId, Object object, ISubscriptionManager subscriptionManager) {
        this.logger.trace("Creating the selection event processor {}", selectionId); //$NON-NLS-1$

        this.selectionDescription = Objects.requireNonNull(selectionDescription);
        this.editingContext = Objects.requireNonNull(editingContext);
        this.selectionId = Objects.requireNonNull(selectionId);
        this.object = Objects.requireNonNull(object);
        this.subscriptionManager = Objects.requireNonNull(subscriptionManager);

        Selection selection = this.refreshSelection();
        this.currentSelection.set(selection);

    }

    @Override
    public IRepresentation getRepresentation() {
        return this.currentSelection.get();
    }

    @Override
    public ISubscriptionManager getSubscriptionManager() {
        return this.subscriptionManager;
    }

    @Override
    public Optional<EventHandlerResponse> handle(IRepresentationInput representationInput) {
        return Optional.empty();
    }

    @Override
    public void refresh(IInput input, ChangeDescription changeDescription) {
        if (ChangeKind.SEMANTIC_CHANGE.equals(changeDescription.getKind())) {
            Selection selection = this.refreshSelection();

            this.currentSelection.set(selection);
            EmitResult emitResult = this.sink.tryEmitNext(new SelectionRefreshedEventPayload(input.getId(), selection));
            if (emitResult.isFailure()) {
                String pattern = "An error has occurred while emitting a SelectionRefreshedEventPayload: {}"; //$NON-NLS-1$
                this.logger.warn(pattern, emitResult);
            }
        }
    }

    private Selection refreshSelection() {
        VariableManager variableManager = new VariableManager();
        variableManager.put(VariableManager.SELF, this.object);
        variableManager.put(IEditingContext.EDITING_CONTEXT, this.editingContext);

        Selection selection = new SelectionRenderer(variableManager, this.selectionDescription).render();

        this.logger.trace("Selection refreshed: {}", selection.getId()); //$NON-NLS-1$

        return selection;
    }

    @Override
    public Flux<IPayload> getOutputEvents(IInput input) {
        var initialRefresh = Mono.fromCallable(() -> new SelectionRefreshedEventPayload(input.getId(), this.currentSelection.get()));
        var refreshEventFlux = Flux.concat(initialRefresh, this.sink.asFlux());

        // @formatter:off
        return Flux.merge(
            refreshEventFlux,
            this.subscriptionManager.getFlux(input)
        )
        .doOnSubscribe(subscription -> {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            this.subscriptionManager.add(input, username);
            this.logger.trace("{} has subscribed to the selection {} {}", username, this.selectionId, this.subscriptionManager); //$NON-NLS-1$
        })
        .doOnCancel(() -> {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            this.subscriptionManager.remove(UUID.randomUUID(), username);
            this.logger.trace("{} has unsubscribed from the selection {} {}", username, this.selectionId, this.subscriptionManager); //$NON-NLS-1$

            if (this.subscriptionManager.isEmpty()) {
                EmitResult emitResult = this.canBeDisposedSink.tryEmitNext(Boolean.TRUE);
                if (emitResult.isFailure()) {
                    String pattern = "An error has occurred while emitting that the processor can be disposed: {}"; //$NON-NLS-1$
                    this.logger.warn(pattern, emitResult);
                }
            }
        });
        // @formatter:on
    }

    @Override
    public Flux<Boolean> canBeDisposed() {
        return this.canBeDisposedSink.asFlux();
    }

    @Override
    public void dispose() {
        this.logger.trace("Disposing the selection event processor {}", this.selectionId); //$NON-NLS-1$

        this.subscriptionManager.dispose();

        EmitResult emitResult = this.sink.tryEmitComplete();
        if (emitResult.isFailure()) {
            String pattern = "An error has occurred while marking the publisher as complete: {}"; //$NON-NLS-1$
            this.logger.warn(pattern, emitResult);
        }
    }

}
