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

export interface GQLSelectionEventSubscription {
  selectionEvent: GQLSelectionEventPayload;
}

export interface GQLSelectionEventPayload {
  __typename: string;
}

export interface GQLSelectionRefreshedEventPayload extends GQLSelectionEventPayload {
  id: string;
  selection: GQLSelection;
}

export interface GQLSelection {
  id: string;
  message: string;
  objects: GQLSelectionObject[];
}

export interface GQLSelectionObject {
  id: string;
  label: string;
  iconURL: string;
}
