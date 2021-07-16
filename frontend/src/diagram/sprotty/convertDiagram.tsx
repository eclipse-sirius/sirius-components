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
import {
  boundsFeature,
  connectableFeature,
  createFeatureSet,
  deletableFeature,
  editLabelFeature,
  fadeFeature,
  FeatureSet,
  hoverFeedbackFeature,
  layoutContainerFeature,
  moveFeature,
  popupFeature,
  selectFeature,
  SLabel,
  viewportFeature,
  withEditLabelFeature,
} from 'sprotty';
import { resizeFeature } from './resize/model';

/**
 * Convert the given diagram object to a Sprotty diagram.
 *
 * SiriusWeb diagram and Sprotty diagram does not match exactly the same API.
 * This converter will ensure the creation of a proper Sprotty diagram from a given Sirius Web diagram..
 *
 * @param diagram the diagram object to convert
 * @param httpOrigin the URL of the server hosting the images
 * @param readOnly Whether the diagram is readonly
 * @return a Sprotty diagram object
 */
export const convertDiagram = (diagram, httpOrigin: string, readOnly: boolean) => {
  const { id, descriptionId, kind, targetObjectId, label, position, size, autoLayout } = diagram;
  const nodes = diagram.nodes.map((node) => convertNode(node, httpOrigin, readOnly, autoLayout));
  const edges = diagram.edges.map((edge) => convertEdge(edge, httpOrigin, readOnly));

  return {
    id,
    descriptionId,
    kind,
    type: 'graph',
    targetObjectId,
    label,
    position,
    features: createFeatureSet([hoverFeedbackFeature, viewportFeature]),
    size,
    autoLayout,
    children: [...nodes, ...edges],
  };
};

const convertNode = (node, httpOrigin: string, readOnly: boolean, autoLayout: boolean) => {
  const { id, type, targetObjectId, targetObjectKind, targetObjectLabel, descriptionId, label, style, size, position } =
    node;

  let borderNodes = [];
  if (node.borderNodes) {
    borderNodes = node.borderNodes.map((borderNode) => convertNode(borderNode, httpOrigin, readOnly, autoLayout));
  }
  let childNodes = [];
  if (node.childNodes) {
    childNodes = node.childNodes.map((childNode) => convertNode(childNode, httpOrigin, readOnly, autoLayout));
  }

  const convertedLabel = convertLabel(label, httpOrigin, readOnly);
  const convertedStyle = convertNodeStyle(style, httpOrigin);

  const features = handleNodeFeatures(node, readOnly, autoLayout);

  const editableLabel = handleNodeLabel(convertedLabel, readOnly);

  return {
    id,
    type,
    targetObjectId,
    targetObjectKind,
    targetObjectLabel,
    descriptionId,
    style: convertedStyle,
    size,
    position,
    features,
    editableLabel,
    children: [convertedLabel, ...borderNodes, ...childNodes],
  };
};

const handleNodeLabel = (label, readOnly: boolean) => {
  let editableLabel = undefined;
  if (!readOnly) {
    editableLabel = label;
  }

  return editableLabel;
};

const handleNodeFeatures = (node, readOnly: boolean, autoLayout: boolean): FeatureSet => {
  const features = new Set<symbol>([
    connectableFeature,
    deletableFeature,
    selectFeature,
    boundsFeature,
    layoutContainerFeature,
    fadeFeature,
    hoverFeedbackFeature,
    popupFeature,
    moveFeature,
    resizeFeature,
    withEditLabelFeature,
  ]);

  if (node.type === 'node:list') {
    features.delete(resizeFeature);
  } else if (node.type === 'node:list:item') {
    features.delete(resizeFeature);
    features.delete(connectableFeature);
    features.delete(moveFeature);
  }

  if (readOnly) {
    features.delete(resizeFeature);
    features.delete(moveFeature);
    features.delete(withEditLabelFeature);
  }

  if (autoLayout) {
    features.delete(resizeFeature);
    features.delete(moveFeature);
  }

  return features;
};

const convertLabel = (label, httpOrigin: string, readOnly: boolean) => {
  let convertedLabel = { ...label };

  if (!readOnly) {
    convertedLabel = {
      ...convertedLabel,
      features: createFeatureSet([...SLabel.DEFAULT_FEATURES, editLabelFeature]),
    };
  }

  if (convertedLabel?.style?.iconURL !== undefined && convertedLabel?.style?.iconURL !== '') {
    const { style } = convertedLabel;
    return { ...convertedLabel, style: { ...style, iconURL: httpOrigin + style.iconURL } };
  }
  return convertedLabel;
};

const convertNodeStyle = (style, httpOrigin: string) => {
  if (style?.imageURL !== undefined && style?.imageURL !== '') {
    return { ...style, imageURL: httpOrigin + style.imageURL };
  }
  return style;
};

const convertEdge = (edge, httpOrigin: string, readOnly: boolean) => {
  const {
    id,
    type,
    targetObjectId,
    targetObjectKind,
    targetObjectLabel,
    descriptionId,
    beginLabel,
    centerLabel,
    endLabel,
    sourceId,
    targetId,
    style,
    routingPoints,
  } = edge;

  let children = [];
  if (beginLabel) {
    const convertedBeginLabel = convertLabel(beginLabel, httpOrigin, readOnly);
    children.push(convertedBeginLabel);
  }
  if (centerLabel) {
    const convertedCenterLabel = convertLabel(centerLabel, httpOrigin, readOnly);
    children.push(convertedCenterLabel);
  }
  if (endLabel) {
    const convertedEndLabel = convertLabel(endLabel, httpOrigin, readOnly);
    children.push(convertedEndLabel);
  }

  const features = handleEdgeFeatures(readOnly);

  return {
    id,
    type,
    targetObjectId,
    targetObjectKind,
    targetObjectLabel,
    descriptionId,
    sourceId,
    targetId,
    style,
    routingPoints,
    features,
    children: children,
  };
};

const handleEdgeFeatures = (readonly) => {
  if (readonly) {
    return createFeatureSet([selectFeature, fadeFeature, hoverFeedbackFeature]);
  }

  return createFeatureSet([deletableFeature, selectFeature, fadeFeature, hoverFeedbackFeature]);
};
