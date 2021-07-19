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
package org.eclipse.sirius.web.compat.services.representations;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.sirius.diagram.description.tool.ContainerCreationDescription;
import org.eclipse.sirius.diagram.description.tool.NodeCreationDescription;
import org.eclipse.sirius.properties.ViewExtensionDescription;
import org.eclipse.sirius.viewpoint.description.Group;
import org.eclipse.sirius.viewpoint.description.RepresentationDescription;
import org.eclipse.sirius.viewpoint.description.Viewpoint;
import org.eclipse.sirius.viewpoint.description.tool.SelectModelElementVariable;
import org.eclipse.sirius.web.compat.services.SelectModelElementVariableProvider;
import org.eclipse.sirius.web.compat.services.diagrams.api.IDiagramDescriptionConverter;
import org.eclipse.sirius.web.compat.services.forms.api.IViewExtensionDescriptionConverter;
import org.eclipse.sirius.web.compat.services.selection.api.ISelectModelElementVariableConverter;
import org.eclipse.sirius.web.forms.description.FormDescription;
import org.eclipse.sirius.web.representations.IRepresentationDescription;
import org.eclipse.sirius.web.selection.description.SelectionDescription;
import org.springframework.stereotype.Service;

/**
 * Retrieves and converts the representation descriptions from the given odesign classpath resource.
 *
 * @author sbegaudeau
 */
@Service
public class SiriusRepresentationDescriptionProvider {

    private final IDiagramDescriptionConverter diagramDescriptionConverter;

    private final IViewExtensionDescriptionConverter viewExtensionDescriptionConverter;

    private final ISelectModelElementVariableConverter selectModelElementVariableConverter;

    public SiriusRepresentationDescriptionProvider(IDiagramDescriptionConverter diagramDescriptionConverter, IViewExtensionDescriptionConverter viewExtensionDescriptionConverter,
            ISelectModelElementVariableConverter selectModelElementVariableConverter) {
        this.diagramDescriptionConverter = Objects.requireNonNull(diagramDescriptionConverter);
        this.viewExtensionDescriptionConverter = Objects.requireNonNull(viewExtensionDescriptionConverter);
        this.selectModelElementVariableConverter = Objects.requireNonNull(selectModelElementVariableConverter);
    }

    public List<IRepresentationDescription> getRepresentationDescriptions(Group group) {
        List<Viewpoint> viewpoints = group.getOwnedViewpoints();

        // @formatter:off
        List<RepresentationDescription> siriusRepresentationDescriptions = viewpoints.stream()
                .map(Viewpoint::getOwnedRepresentations)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        // @formatter:on

        List<IRepresentationDescription> representationDescriptions = new ArrayList<>();
        for (RepresentationDescription siriusRepresentationDescription : siriusRepresentationDescriptions) {
            if (siriusRepresentationDescription instanceof org.eclipse.sirius.diagram.description.DiagramDescription
                    && !(siriusRepresentationDescription instanceof org.eclipse.sirius.diagram.sequence.description.SequenceDiagramDescription)) {
                representationDescriptions.add(this.diagramDescriptionConverter.convert((org.eclipse.sirius.diagram.description.DiagramDescription) siriusRepresentationDescription));
                representationDescriptions.addAll(this.getSelectionDescriptions((org.eclipse.sirius.diagram.description.DiagramDescription) siriusRepresentationDescription));
            }
        }
        representationDescriptions.addAll(this.getFormDescriptions(group));
        return representationDescriptions;
    }

    private List<SelectionDescription> getSelectionDescriptions(org.eclipse.sirius.diagram.description.DiagramDescription diagramDescription) {
        // @formatter:off
        List<ContainerCreationDescription> containerCreationDescriptions = diagramDescription.getAllTools().stream()
                .filter(ContainerCreationDescription.class::isInstance)
                .map(ContainerCreationDescription.class::cast)
                .collect(Collectors.toList());

        List<NodeCreationDescription> nodeCreationDescriptions = diagramDescription.getAllTools().stream()
                .filter(NodeCreationDescription.class::isInstance)
                .map(NodeCreationDescription.class::cast)
                .collect(Collectors.toList());

        List<SelectModelElementVariable> smeVariablesFromContainers = containerCreationDescriptions.stream()
                .map(containerCreationDescription ->  {
                    Optional<SelectModelElementVariable> optional = new SelectModelElementVariableProvider().getSelectModelElementVariable(containerCreationDescription.getVariable());
                    if (optional.isPresent()) {
                        return optional.get();
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        List<SelectModelElementVariable> smeVariablesFromNodes = nodeCreationDescriptions.stream()
                .map(nodeCreationDescription ->  {
                    Optional<SelectModelElementVariable> optional = new SelectModelElementVariableProvider().getSelectModelElementVariable(nodeCreationDescription.getVariable());
                    if (optional.isPresent()) {
                        return optional.get();
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return Stream.concat(smeVariablesFromContainers.stream(), smeVariablesFromNodes.stream())
                .map(selectModelElementVariable -> this.selectModelElementVariableConverter.convert(selectModelElementVariable, diagramDescription))
                .collect(Collectors.toUnmodifiableList());
        // @formatter:on
    }

    private List<FormDescription> getFormDescriptions(Group group) {
        // @formatter:off
        List<ViewExtensionDescription> viewExtensionDescriptions = group.getExtensions().stream()
                .filter(ViewExtensionDescription.class::isInstance)
                .map(ViewExtensionDescription.class::cast)
                .collect(Collectors.toList());

        return viewExtensionDescriptions.stream()
                .map(this.viewExtensionDescriptionConverter::convert)
                .collect(Collectors.toUnmodifiableList());
        // @formatter:on
    }

}
