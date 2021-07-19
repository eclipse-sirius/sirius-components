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
import gql from 'graphql-tag';

export const subscribersUpdatedEventPayloadFragment = gql`
  fragment subscribersUpdatedEventPayloadFragment on SubscribersUpdatedEventPayload {
    id
    subscribers {
      username
    }
  }
`;

export const widgetSubscriptionsUpdatedEventPayloadFragment = gql`
  fragment widgetSubscriptionsUpdatedEventPayloadFragment on WidgetSubscriptionsUpdatedEventPayload {
    id
    widgetSubscriptions {
      widgetId
      subscribers {
        username
      }
    }
  }
`;

export const formRefreshedEventPayloadFragment = gql`
  fragment formRefreshedEventPayloadFragment on FormRefreshedEventPayload {
    id
    form {
      id
      label
      pages {
        id
        label
        groups {
          id
          label
          widgets {
            id
            __typename
            ... on Textfield {
              label
              stringValue: value
            }
            ... on Textarea {
              label
              stringValue: value
            }
            ... on Checkbox {
              label
              booleanValue: value
            }
            ... on Select {
              label
              value
              options {
                id
                label
              }
            }
            ... on MultiSelect {
              label
              values
              options {
                id
                label
              }
            }
            ... on Radio {
              label
              options {
                id
                label
                selected
              }
            }
            ... on List {
              label
              items {
                id
                label
                imageURL
              }
            }
          }
        }
      }
    }
  }
`;
