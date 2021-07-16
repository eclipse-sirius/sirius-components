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
package org.eclipse.sirius.web.diagrams.renderer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.eclipse.sirius.web.components.IElementFactory;
import org.eclipse.sirius.web.components.IProps;
import org.eclipse.sirius.web.diagrams.Diagram;
import org.eclipse.sirius.web.diagrams.Edge;
import org.eclipse.sirius.web.diagrams.Label;
import org.eclipse.sirius.web.diagrams.Node;
import org.eclipse.sirius.web.diagrams.components.LabelType;
import org.eclipse.sirius.web.diagrams.elements.DiagramElementProps;
import org.eclipse.sirius.web.diagrams.elements.EdgeElementProps;
import org.eclipse.sirius.web.diagrams.elements.LabelElementProps;
import org.eclipse.sirius.web.diagrams.elements.NodeElementProps;
import org.slf4j.Logger;

/**
 * Used to instantiate the elements of the diagram.
 *
 * @author sbegaudeau
 */
public class DiagramElementFactory implements IElementFactory {

    private final Logger logger;

    public DiagramElementFactory(Logger logger) {
        this.logger = Objects.requireNonNull(logger);
    }

    @Override
    public Object instantiateElement(String type, IProps props, List<Object> children) {
        Object object = null;
        if (DiagramElementProps.TYPE.equals(type) && props instanceof DiagramElementProps) {
            object = this.instantiateDiagram((DiagramElementProps) props, children);
        } else if (NodeElementProps.TYPE.equals(type) && props instanceof NodeElementProps) {
            object = this.instantiateNode((NodeElementProps) props, children);
        } else if (EdgeElementProps.TYPE.equals(type) && props instanceof EdgeElementProps) {
            object = this.instantiateEdge((EdgeElementProps) props, children);
        } else if (LabelElementProps.TYPE.equals(type) && props instanceof LabelElementProps) {
            object = this.instantiateLabel((LabelElementProps) props, children);
        }
        return object;
    }

    private Diagram instantiateDiagram(DiagramElementProps props, List<Object> children) {
        List<Node> nodes = new ArrayList<>();
        List<Edge> edges = new ArrayList<>();

        // @formatter:off
        children.forEach(child -> {
            if (child instanceof Node) {
                nodes.add((Node) child);
            } else if (child instanceof Edge) {
                edges.add((Edge) child);
            } else {
                this.logger.error("Unsupported child {}", child); //$NON-NLS-1$
            }
        });

        return Diagram.newDiagram(props.getId())
                .targetObjectId(props.getTargetObjectId())
                .descriptionId(props.getDescriptionId())
                .label(props.getLabel())
                .position(props.getPosition())
                .size(props.getSize())
                .autoLayout(props.isAutoLayout())
                .nodes(nodes)
                .edges(edges)
                .build();
        // @formatter:on
    }

    private Node instantiateNode(NodeElementProps props, List<Object> children) {
        // @formatter:off
        Label label = children.stream()
                .filter(Label.class::isInstance)
                .map(Label.class::cast)
                .findFirst()
                .orElse(null);
        // @formatter:on

        List<Node> childNodes = new ArrayList<>();
        List<Node> borderNodes = new ArrayList<>();

        children.stream().filter(Node.class::isInstance).map(Node.class::cast).forEach(node -> {
            if (node.isBorderNode()) {
                borderNodes.add(node);
            } else {
                childNodes.add(node);
            }
        });

        // @formatter:off
        return Node.newNode(props.getId())
                .type(props.getType())
                .targetObjectId(props.getTargetObjectId())
                .targetObjectKind(props.getTargetObjectKind())
                .targetObjectLabel(props.getTargetObjectLabel())
                .descriptionId(props.getDescriptionId())
                .borderNode(props.isBorderNode())
                .label(label)
                .style(props.getStyle())
                .position(props.getPosition())
                .size(props.getSize())
                .borderNodes(borderNodes)
                .childNodes(childNodes)
                .customizedProperties(props.getCustomizableProperties())
                .build();
        // @formatter:on
    }

    private Edge instantiateEdge(EdgeElementProps props, List<Object> children) {
        // @formatter:off
        Label beginLabel = this.getLabel(children, LabelType.EDGE_BEGIN);
        Label centerLabel = this.getLabel(children, LabelType.EDGE_CENTER);
        Label endLabel = this.getLabel(children, LabelType.EDGE_END);
        return Edge.newEdge(props.getId())
                .type(props.getType())
                .targetObjectId(props.getTargetObjectId())
                .targetObjectKind(props.getTargetObjectKind())
                .targetObjectLabel(props.getTargetObjectLabel())
                .descriptionId(props.getDescriptionId())
                .beginLabel(beginLabel)
                .centerLabel(centerLabel)
                .endLabel(endLabel)
                .sourceId(props.getSourceId())
                .targetId(props.getTargetId())
                .style(props.getStyle())
                .routingPoints(props.getRoutingPoints())
                .build();
        // @formatter:on
    }

    private Label getLabel(List<Object> children, LabelType labelType) {
        // @formatter:off
        return children.stream()
                .filter(Label.class::isInstance)
                .map(Label.class::cast)
                .filter(label -> label.getType().equals(labelType.getValue()))
                .findFirst()
                .orElse(null);
        // @formatter:on

    }

    private Label instantiateLabel(LabelElementProps props, List<Object> children) {
        // @formatter:off
        return Label.newLabel(props.getId())
                .type(props.getType())
                .text(props.getText())
                .position(props.getPosition())
                .size(props.getSize())
                .alignment(props.getAlignment())
                .style(props.getStyle())
                .build();
        // @formatter:on
    }

}
