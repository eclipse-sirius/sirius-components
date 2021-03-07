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
export const EMPTY__STATE = 'EMPTY__STATE';
export const LOADING__STATE = 'LOADING__STATE';
export const READY__STATE = 'READY__STATE';
export const COMPLETE__STATE = 'COMPLETE__STATE';

export const INITIALIZE__ACTION = 'INITIALIZE__ACTION';
export const SET_TOOL_SECTIONS__ACTION = 'SET_TOOL_SECTIONS__ACTION';
export const SET_DEFAULT_TOOL__ACTION = 'SET_DEFAULT_TOOL__ACTION';
export const SET_CONTEXTUAL_PALETTE__ACTION = 'SET_CONTEXTUAL_PALETTE__ACTION';
export const SET_ACTIVE_TOOL__ACTION = 'SET_ACTIVE_TOOL__ACTION';
export const HANDLE_DATA__ACTION = 'HANDLE_DATA__ACTION';
export const HANDLE_CONNECTION_ERROR__ACTION = 'HANDLE_CONNECTION_ERROR__ACTION';
export const HANDLE_ERROR__ACTION = 'HANDLE_ERROR__ACTION';
export const HANDLE_COMPLETE__ACTION = 'HANDLE_COMPLETE__ACTION';
export const SELECTION__ACTION = 'SELECTION__ACTION';
export const SELECTED_ELEMENT__ACTION = 'SELECTED_ELEMENT__ACTION';
export const SELECT_ZOOM_LEVEL__ACTION = 'SELECT_ZOOM_LEVEL__ACTION';
export const SWITCH_REPRESENTATION__ACTION = 'SWITCH_REPRESENTATION__ACTION';

export const machine = {
  EMPTY__STATE: {
    SWITCH_REPRESENTATION__ACTION: [LOADING__STATE],
  },
  LOADING__STATE: {
    INITIALIZE__ACTION: [READY__STATE],
  },
  READY__STATE: {
    SWITCH_REPRESENTATION__ACTION: [LOADING__STATE],
    SET_TOOL_SECTIONS__ACTION: [READY__STATE],
    SET_DEFAULT_TOOL__ACTION: [READY__STATE],
    HANDLE_DATA__ACTION: [READY__STATE],
    SET_ACTIVE_TOOL__ACTION: [READY__STATE],
    SET_SOURCE_ELEMENT__ACTION: [READY__STATE],
    SET_CURRENT_ROOT__ACTION: [READY__STATE],
    SET_CONTEXTUAL_PALETTE__ACTION: [READY__STATE],
    SELECTION__ACTION: [READY__STATE],
    SELECTED_ELEMENT__ACTION: [READY__STATE],
    SELECT_ZOOM_LEVEL__ACTION: [READY__STATE],
    HANDLE_CONNECTION_ERROR__ACTION: [COMPLETE__STATE],
    HANDLE_ERROR__ACTION: [COMPLETE__STATE],
    HANDLE_COMPLETE__ACTION: [COMPLETE__STATE],
  },
  COMPLETE__STATE: {
    SWITCH_REPRESENTATION__ACTION: [LOADING__STATE],
  },
};
