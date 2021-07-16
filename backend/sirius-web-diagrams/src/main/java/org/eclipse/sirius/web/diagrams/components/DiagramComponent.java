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
package org.eclipse.sirius.web.diagrams.components;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.eclipse.sirius.web.components.Element;
import org.eclipse.sirius.web.components.IComponent;
import org.eclipse.sirius.web.diagrams.Diagram;
import org.eclipse.sirius.web.diagrams.Position;
import org.eclipse.sirius.web.diagrams.Size;
import org.eclipse.sirius.web.diagrams.description.DiagramDescription;
import org.eclipse.sirius.web.diagrams.elements.DiagramElementProps;
import org.eclipse.sirius.web.diagrams.renderer.DiagramRenderingCache;
import org.eclipse.sirius.web.representations.VariableManager;

/**
 * The component used to render the diagram.
 *
 * @author sbegaudeau
 */
public class DiagramComponent implements IComponent {

    private final DiagramComponentProps props;

    public DiagramComponent(DiagramComponentProps props) {
        this.props = props;
    }

    @Override
    public Element render() {
        VariableManager variableManager = this.props.getVariableManager();
        DiagramDescription diagramDescription = this.props.getDiagramDescription();
        var optionalPreviousDiagram = this.props.getPreviousDiagram();

        String label = diagramDescription.getLabelProvider().apply(variableManager);

        UUID diagramId = optionalPreviousDiagram.map(Diagram::getId).orElseGet(UUID::randomUUID);
        String targetObjectId = diagramDescription.getTargetObjectIdProvider().apply(variableManager);

        DiagramRenderingCache cache = new DiagramRenderingCache();

        IDiagramElementRequestor diagramElementRequestor = new DiagramElementRequestor();
        // @formatter:off
        var nodes = diagramDescription.getNodeDescriptions().stream()
                .map(nodeDescription -> {
                    var previousNodes = optionalPreviousDiagram.map(previousDiagram -> diagramElementRequestor.getRootNodes(previousDiagram, nodeDescription))
                            .orElse(List.of());
                    INodesRequestor nodesRequestor = new NodesRequestor(previousNodes);
                    var nodeComponentProps = NodeComponentProps.newNodeComponentProps()
                            .variableManager(variableManager)
                            .nodeDescription(nodeDescription)
                            .nodesRequestor(nodesRequestor)
                            .containmentKind(NodeContainmentKind.CHILD_NODE)
                            .cache(cache)
                            .viewCreationRequests(this.props.getViewCreationRequests())
                            .parentElementId(diagramId)
                            .build();
                    return new Element(NodeComponent.class, nodeComponentProps);
                }).collect(Collectors.toList());

        var edges = diagramDescription.getEdgeDescriptions().stream()
                .map(edgeDescription -> {
                    var previousEdges = optionalPreviousDiagram.map(previousDiagram -> diagramElementRequestor.getEdges(previousDiagram, edgeDescription))
                            .orElse(List.of());
                    IEdgesRequestor edgesRequestor = new EdgesRequestor(previousEdges);
                    var edgeComponentProps = new EdgeComponentProps(variableManager, edgeDescription, edgesRequestor, cache);
                    return new Element(EdgeComponent.class, edgeComponentProps);
                })
                .collect(Collectors.toList());
        // @formatter:on

        List<Element> children = new ArrayList<>();
        children.addAll(nodes);
        children.addAll(edges);

        // @formatter:off
        DiagramElementProps diagramElementProps = DiagramElementProps.newDiagramElementProps(diagramId)
                .targetObjectId(targetObjectId)
                .descriptionId(diagramDescription.getId())
                .label(label)
                .position(Position.UNDEFINED)
                .size(Size.UNDEFINED)
                .autoLayout(diagramDescription.isAutoLayout())
                .children(children)
                .build();
        // @formatter:on
        return new Element(DiagramElementProps.TYPE, diagramElementProps);
    }

}
