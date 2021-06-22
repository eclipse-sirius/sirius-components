/*******************************************************************************
 * Copyright (c) 2021 THALES GLOBAL SERVICES.
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
import { Palette, Subscriber, Tool, ToolSection } from 'diagram/DiagramWebSocketContainer.types';
import { createDependencyInjectionContainer } from 'diagram/sprotty/DependencyInjection';
import { SiriusWebWebSocketDiagramServer } from 'diagram/sprotty/WebSocketDiagramServer';
import { MutableRefObject } from 'react';
import { MousePositionTracker, TYPES } from 'sprotty';
import { v4 as uuid } from 'uuid';
import { Selection } from 'workbench/Workbench.types';
import { assign, Machine } from 'xstate';

export interface DiagramWebSocketContainerStateSchema {
  states: {
    toast: {
      states: {
        visible: {};
        hidden: {};
      };
    };
    diagramWebSocketContainer: {
      states: {
        empty: {};
        loading: {};
        ready: {};
        complete: {};
      };
    };
    selectionDialog: {
      states: {
        visible: {};
        hidden: {};
      }
    }
  };
}

export type SchemaValue = {
  toast: 'visible' | 'hidden';
  diagramWebSocketContainer: 'loading' | 'ready' | 'empty' | 'complete';
  selectionDialog: 'visible' | 'hidden';
};

export interface DiagramWebSocketContainerContext {
  id: string;
  displayedRepresentationId: string | null;
  diagramServer: any;
  diagram: any;
  toolSections: ToolSection[];
  activeTool: Tool | null;
  contextualPalette: Palette | null;
  latestSelection: Selection | null;
  newSelection: Selection | null;
  zoomLevel: string;
  subscribers: Subscriber[];
  message: string | null;
  selectedObjectId: string | null;
}

export type ShowToastEvent = { type: 'SHOW_TOAST'; message: string };
export type HideToastEvent = { type: 'HIDE_TOAST' };
export type ShowSelectionDialogEvent = { type: 'SHOW_SELECTION_DIALOG' };
export type HideSelectionDialogEvent = { type: 'HIDE_SELECTION_DIALOG' };
export type HandleSelectedObjectInSelectionDialogEvent = { type: 'HANDLE_SELECTED_OBJECT_IN_SELECTION_DIALOG', selectedObjectId: string };
export type ResetSelectedObjectInSelectionDialogEvent = { type: 'RESET_SELECTED_OBJECT_IN_SELECTION_DIALOG' };
export type SwithRepresentationEvent = { type: 'SWITCH_REPRESENTATION'; representationId: string };
export type SetToolSectionsEvent = { type: 'SET_TOOL_SECTIONS'; toolSections: ToolSection[] };
export type SetDefaultToolEvent = { type: 'SET_DEFAULT_TOOL'; defaultTool: Tool };
export type DiagramRefreshedEvent = { type: 'HANDLE_DIAGRAM_REFRESHED'; diagram: any };
export type SubscribersUpdatedEvent = { type: 'HANDLE_SUBSCRIBERS_UPDATED'; subscribers: Subscriber[] };
export type SetActiveToolEvent = { type: 'SET_ACTIVE_TOOL'; activeTool: Tool | null };
export type SetContextualPaletteEvent = { type: 'SET_CONTEXTUAL_PALETTE'; contextualPalette: Palette | null };
export type SelectionEvent = { type: 'SELECTION'; selection: Selection };
export type SelectedElementEvent = { type: 'SELECTED_ELEMENT'; selection: Selection };
export type SelectZoomLevelEvent = { type: 'SELECT_ZOOM_LEVEL'; level: string };
export type CompleteEvent = { type: 'HANDLE_COMPLETE' };
// TODO Add event for selection dialog

export type InitializeRepresentationEvent = {
  type: 'INITIALIZE';
  diagramDomElement: MutableRefObject<any>;
  deleteElements: any;
  invokeTool: any;
  moveElement: any;
  resizeElement: any;
  editLabel: any;
  onSelectElement: any;
  getCursorOn: any;
  setActiveTool: (tool: Tool) => void;
  toolSections: ToolSection[];
  setContextualPalette: (contextualPalette: Palette) => void;
  httpOrigin: string;
};

export type DiagramWebSocketContainerEvent =
  | ShowToastEvent
  | HideToastEvent
  | ShowSelectionDialogEvent
  | HideSelectionDialogEvent
  | HandleSelectedObjectInSelectionDialogEvent
  | ResetSelectedObjectInSelectionDialogEvent
  | SwithRepresentationEvent
  | InitializeRepresentationEvent
  | SetToolSectionsEvent
  | SetDefaultToolEvent
  | DiagramRefreshedEvent
  | SubscribersUpdatedEvent
  | SetActiveToolEvent
  | SetContextualPaletteEvent
  | SelectionEvent
  | SelectedElementEvent
  | SelectZoomLevelEvent
  | CompleteEvent;

export const diagramWebSocketContainerMachine = Machine<
  DiagramWebSocketContainerContext,
  DiagramWebSocketContainerStateSchema,
  DiagramWebSocketContainerEvent
>(
  {
    type: 'parallel',
    context: {
      id: uuid(),
      displayedRepresentationId: null,
      diagramServer: null,
      diagram: null,
      toolSections: [],
      activeTool: null,
      contextualPalette: null,
      latestSelection: null,
      newSelection: null,
      zoomLevel: '1',
      subscribers: [],
      message: null,
      selectedObjectId: null,
    },
    states: {
      toast: {
        initial: 'hidden',
        states: {
          hidden: {
            on: {
              SHOW_TOAST: {
                target: 'visible',
                actions: 'setMessage',
              },
            },
          },
          visible: {
            on: {
              HIDE_TOAST: {
                target: 'hidden',
                actions: 'clearMessage',
              },
            },
          },
        },
      },
      diagramWebSocketContainer: {
        initial: 'empty',
        states: {
          empty: {
            on: {
              SWITCH_REPRESENTATION: [
                {
                  target: 'loading',
                  actions: 'switchRepresentation',
                },
              ],
            },
          },
          loading: {
            on: {
              INITIALIZE: [
                {
                  target: 'ready',
                  actions: 'initialize',
                },
              ],
              SWITCH_REPRESENTATION: [
                {
                  target: 'loading',
                  actions: 'switchRepresentation',
                },
              ],
            },
          },
          ready: {
            on: {
              SWITCH_REPRESENTATION: [
                {
                  target: 'loading',
                  actions: 'switchRepresentation',
                },
              ],
              SET_TOOL_SECTIONS: [
                {
                  actions: 'setToolSections',
                },
              ],
              SET_DEFAULT_TOOL: [
                {
                  actions: 'setDefaultTool',
                },
              ],
              HANDLE_DIAGRAM_REFRESHED: [
                {
                  actions: 'handleDiagramRefreshed',
                },
              ],
              HANDLE_SUBSCRIBERS_UPDATED: [
                {
                  actions: 'handleSubscribersUpdated',
                },
              ],
              SET_ACTIVE_TOOL: [
                {
                  actions: 'setActiveTool',
                },
              ],
              SET_CONTEXTUAL_PALETTE: [
                {
                  actions: 'setContextualPalette',
                },
              ],
              SELECTION: [
                {
                  actions: 'setSelection',
                },
              ],
              SELECTED_ELEMENT: [
                {
                  actions: 'setSelectedElement',
                },
              ],
              SELECT_ZOOM_LEVEL: [
                {
                  actions: 'selectZoomLevel',
                },
              ],
              HANDLE_COMPLETE: [
                {
                  target: 'complete',
                  actions: 'handleComplete',
                },
              ],
            },
          },
          complete: {
            on: {
              SWITCH_REPRESENTATION: [
                {
                  target: 'loading',
                  actions: 'switchRepresentation',
                },
              ],
            },
          },
        },
      },
      selectionDialog: {
        initial: 'hidden',
        states: {
          hidden: {
            on: {
              SHOW_SELECTION_DIALOG: {
                target: 'visible',
              },
              HANDLE_SELECTED_OBJECT_IN_SELECTION_DIALOG: {
                target: 'hidden',
                actions: 'handleSelectedObjectInSelectionDialog',
              },
              RESET_SELECTED_OBJECT_IN_SELECTION_DIALOG: {
                target: 'hidden',
                actions: 'resetSelectedObjectInSelectionDialog',
              }
            },
          },
          visible: {
            on: {
              HIDE_SELECTION_DIALOG: {
                target: 'hidden',
              },
              HANDLE_SELECTED_OBJECT_IN_SELECTION_DIALOG: {
                target: 'visible',
                actions: 'handleSelectedObjectInSelectionDialog',
              },
              RESET_SELECTED_OBJECT_IN_SELECTION_DIALOG: {
                target: 'visible',
                actions: 'resetSelectedObjectInSelectionDialog',
              }
            },
          },
        },
      },
    },
  },
  {
    actions: {
      switchRepresentation: assign((_, event) => {
        const { representationId } = event as SwithRepresentationEvent;
        return { id: uuid(), displayedRepresentationId: representationId };
      }),
      initialize: assign((_, event) => {
        const {
          diagramDomElement,
          deleteElements,
          invokeTool,
          moveElement,
          resizeElement,
          editLabel,
          onSelectElement,
          getCursorOn,
          setActiveTool,
          toolSections,
          setContextualPalette,
          httpOrigin,
        } = event as InitializeRepresentationEvent;

        const container = createDependencyInjectionContainer(
          diagramDomElement.current.id,
          onSelectElement,
          getCursorOn,
          setActiveTool
        );
        const diagramServer = <SiriusWebWebSocketDiagramServer>container.get(TYPES.ModelSource);
        /**
         * workaround to inject objects in diagramServer from the injector.
         * We cannot use inversify annotation for now. (and perhaps never)
         */
        diagramServer.setMousePositionTracker(container.get(MousePositionTracker));
        diagramServer.setModelFactory(container.get(TYPES.IModelFactory));
        diagramServer.setLogger(container.get(TYPES.ILogger));

        diagramServer.setEditLabelListener(editLabel);
        diagramServer.setMoveElementListener(moveElement);
        diagramServer.setResizeElementListener(resizeElement);
        diagramServer.setDeleteElementsListener(deleteElements);
        diagramServer.setInvokeToolListener(invokeTool);
        diagramServer.setContextualPaletteListener(setContextualPalette);
        diagramServer.setHttpOrigin(httpOrigin);

        return {
          diagramServer,
          toolSections,
          contextualPalette: undefined,
          diagram: undefined,
          activeTool: undefined,
          latestSelection: undefined,
          newSelection: undefined,
          zoomLevel: '1',
          message: undefined,
          selectedObjectId: undefined,
        };
      }),

      setToolSections: assign((_, event) => {
        const { toolSections } = event as SetToolSectionsEvent;

        let toolSectionsWithDefaults = toolSections;
        if (toolSections) {
          toolSectionsWithDefaults = toolSections.map((toolSection) => {
            if (toolSection.tools && toolSection.tools.length > 0) {
              return { ...toolSection, defaultTool: toolSection.tools[0] };
            } else {
              return toolSection;
            }
          });
        }

        return { toolSections: toolSectionsWithDefaults };
      }),

      handleDiagramRefreshed: assign((_, event) => {
        const { diagram } = event as DiagramRefreshedEvent;
        return { diagram };
      }),

      handleSubscribersUpdated: assign((_, event) => {
        const { subscribers } = event as SubscribersUpdatedEvent;
        return { subscribers };
      }),

      setActiveTool: assign((_, event) => {
        const { activeTool } = event as SetActiveToolEvent;
        return { activeTool };
      }),
      setContextualPalette: assign((_, event) => {
        const { contextualPalette } = event as SetContextualPaletteEvent;
        return { contextualPalette };
      }),

      setSelection: assign((context, event) => {
        const { selection } = event as SelectionEvent;
        let newSelectionValue;
        if ((!context.latestSelection && !selection) || context.latestSelection?.id === selection?.id) {
          newSelectionValue = context.newSelection;
        } else {
          newSelectionValue = selection;
        }
        return {
          latestSelection: newSelectionValue,
          newSelection: newSelectionValue,
          zoomLevel: '1',
        };
      }),
      setSelectedElement: assign((_, event) => {
        const { selection } = event as SelectedElementEvent;
        return { latestSelection: selection };
      }),
      selectZoomLevel: assign((_, event) => {
        const { level } = event as SelectZoomLevelEvent;
        return { zoomLevel: level };
      }),
      handleSelectedObjectInSelectionDialog: assign((_, event) => {
        const { selectedObjectId } = event as HandleSelectedObjectInSelectionDialogEvent;
        return { selectedObjectId: selectedObjectId };
      }),
      resetSelectedObjectInSelectionDialog: assign((_) => {
        return { selectedObjectId: null };
      }),
      handleComplete: assign((_) => {
        return {
          diagramServer: undefined,
          diagram: undefined,
          toolSections: [],
          contextualPalette: undefined,
          activeTool: undefined,
          latestSelection: undefined,
          newSelection: undefined,
          zoomLevel: undefined,
          selectedObjectId: undefined,
        };
      }),
      setMessage: assign((_, event) => {
        const { message } = event as ShowToastEvent;
        return { message };
      }),
      clearMessage: assign((_) => {
        return { message: null };
      }),
    },
  }
);
