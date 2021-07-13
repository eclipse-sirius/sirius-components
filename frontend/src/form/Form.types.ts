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
export interface Form {
  id: string;
  label: string;
  pages: Page[];
}

export interface Subscriber {
  username: string;
}

export interface Page {
  id: string;
  label: string;
  groups: Group[];
}
export interface Group {
  id: string;
  label: string;
  widgets: Widget[];
}
export interface Widget {
  id: string;
  __typename: string;
}

export interface WidgetSubscription {
  widgetId: string;
  subscribers: Subscriber[];
}

export interface Textfield extends Widget {
  label: string;
  stringValue: string;
}

export interface Textarea extends Widget {
  label: string;
  stringValue: string;
}

export interface Checkbox extends Widget {
  label: string;
  booleanValue: boolean;
}

export interface Select extends Widget {
  label: string;
  value: string;
  options: SelectOption[];
}

export interface MultiSelect extends Widget {
  label: string;
  values: string[];
  options: SelectOption[];
}

export interface SelectOption {
  id: string;
  label: string;
}

export interface Radio extends Widget {
  label: string;
  options: RadioOption[];
}

export interface RadioOption {
  id: string;
  label: string;
  selected: boolean;
}

export interface List extends Widget {
  label: string;
  items: ListItem[];
}

export interface ListItem {
  id: string;
  label: string;
  imageURL: string;
}
