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
import { SubscriptionResult } from '@apollo/client';
import { v4 as uuid } from 'uuid';
import { assign, Machine } from 'xstate';
import { Selection } from './Selection.types';
import {
  GQLSelectionEventPayload,
  GQLSelectionEventSubscription,
  GQLSelectionRefreshedEventPayload,
} from './SelectionEvent.types';

export interface SelectionDialogWebSocketContainerStateSchema {
  states: {
    toast: {
      states: {
        visible: {};
        hidden: {};
      };
    };
    selectionDialogWebSocketContainer: {
      states: {
        idle: {};
        ready: {};
        complete: {};
      };
    };
  };
}

export type SchemaValue = {
  toast: 'visible' | 'hidden';
  selectionWebSocketContainer: 'idle' | 'ready' | 'complete';
};

export interface SelectionDialogWebSocketContainerContext {
  id: string;
  selection: Selection | null;
  message: string | null;
  selectedObjectId: string | null;
}

export type ShowToastEvent = { type: 'SHOW_TOAST'; message: string };
export type HideToastEvent = { type: 'HIDE_TOAST' };
export type HandleSelectionUpdatedEvent = { type: 'HANDLE_SELECTION_UPDATED'; selectedObjectId: string };
export type HandleCompleteEvent = { type: 'HANDLE_COMPLETE' };
export type HandleSubscriptionResultEvent = {
  type: 'HANDLE_SUBSCRIPTION_RESULT';
  result: SubscriptionResult<GQLSelectionEventSubscription>;
};

export type SelectionDialogWebSocketContainerEvent =
  | HandleCompleteEvent
  | HandleSelectionUpdatedEvent
  | HandleSubscriptionResultEvent
  | ShowToastEvent
  | HideToastEvent;

const isSelectionRefreshedEventPayload = (
  payload: GQLSelectionEventPayload
): payload is GQLSelectionRefreshedEventPayload => payload.__typename === 'SelectionRefreshedEventPayload';

export const selectionDialogWebSocketContainerMachine = Machine<
  SelectionDialogWebSocketContainerContext,
  SelectionDialogWebSocketContainerStateSchema,
  SelectionDialogWebSocketContainerEvent
>(
  {
    type: 'parallel',
    context: {
      id: uuid(),
      selection: null,
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
      selectionDialogWebSocketContainer: {
        initial: 'idle',
        states: {
          idle: {
            on: {
              HANDLE_SUBSCRIPTION_RESULT: [
                {
                  cond: 'isSelectionRefreshedEventPayload',
                  target: 'ready',
                  actions: 'handleSubscriptionResult',
                },
                {
                  target: 'idle',
                  actions: 'handleSubscriptionResult',
                },
              ],
            },
          },
          ready: {
            on: {
              HANDLE_SELECTION_UPDATED: [
                {
                  target: 'ready',
                  actions: 'handleSelectionUpdated',
                },
              ],
              HANDLE_SUBSCRIPTION_RESULT: [
                {
                  cond: 'isSelectionRefreshedEventPayload',
                  target: 'ready',
                  actions: 'handleSubscriptionResult',
                },
                {
                  target: 'idle',
                  actions: 'handleSubscriptionResult',
                },
              ],
              HANDLE_COMPLETE: {
                target: 'complete',
              },
            },
          },
          complete: {},
        },
      },
    },
  },
  {
    guards: {
      isSelectionRefreshedEventPayload: (_, event) => {
        const { result } = event as HandleSubscriptionResultEvent;
        const { data } = result;
        return isSelectionRefreshedEventPayload(data.selectionEvent);
      },
    },
    actions: {
      handleSelectionUpdated: assign((_, event) => {
        const { selectedObjectId } = event as HandleSelectionUpdatedEvent;
        return { selectedObjectId };
      }),
      handleSubscriptionResult: assign((_, event) => {
        const { result } = event as HandleSubscriptionResultEvent;
        const { data } = result;
        if (isSelectionRefreshedEventPayload(data.selectionEvent)) {
          const { selection } = data.selectionEvent;
          return { selection };
        }
        return {};
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
