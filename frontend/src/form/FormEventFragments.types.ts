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

export interface GQLFormEventSubscription {
  formEvent: GQLFormEventPayload;
}
export interface GQLFormEventPayload {
  __typename: string;
}

export interface GQLPropertiesEventSubscription {
  propertiesEvent: GQLPropertiesEventPayload;
}

export interface GQLPropertiesEventPayload {
  __typename: string;
}

export interface GQLSubscribersUpdatedEventPayload extends GQLFormEventPayload, GQLPropertiesEventPayload {
  id: string;
  subscribers: GQLSubscriber[];
}

export interface GQLWidgetSubscriptionsUpdatedEventPayload extends GQLFormEventPayload, GQLPropertiesEventPayload {
  id: string;
  widgetSubscriptions: GQLWidgetSubscription[];
}

export interface GQLWidgetSubscription {
  widgetId: string;
  subscribers: GQLSubscriber[];
}

export interface GQLSubscriber {
  username: string;
}

export interface GQLFormRefreshedEventPayload extends GQLFormEventPayload, GQLPropertiesEventPayload {
  id: string;
  form: GQLForm;
}

export interface GQLForm {
  id: string;
  label: string;
  pages: GQLPage[];
}

export interface GQLPage {
  id: string;
  label: string;
  groups: GQLGroup[];
}
export interface GQLGroup {
  id: string;
  label: string;
  widgets: GQLWidget[];
}
export interface GQLWidget {
  id: string;
  __typename: string;
}

export interface GQLTextfield extends GQLWidget {
  label: string;
  stringValue: string;
}

export interface GQLTextarea extends GQLWidget {
  label: string;
  stringValue: string;
}

export interface GQLCheckbox extends GQLWidget {
  label: string;
  booleanValue: string;
}

export interface GQLSelect extends GQLWidget {
  label: string;
  value: string;
  options: GQLSelectOption[];
}

export interface GQLMultiSelect extends GQLWidget {
  label: string;
  values: string[];
  options: GQLSelectOption[];
}
export interface GQLSelectOption {
  id: string;
  label: string;
}

export interface GQLRadio extends GQLWidget {
  label: string;
  options: GQLRadioOption[];
}

export interface GQLRadioOption {
  id: string;
  label: string;
  selected: boolean;
}

export interface GQLList extends GQLWidget {
  label: string;
  items: GQLListItem[];
}

export interface GQLListItem {
  id: string;
  label: string;
  imageURL: string;
}
