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
import { Checkbox, Subscriber } from 'form/Form.types';

export interface CheckboxPropertySectionProps {
  editingContextId: string;
  formId: string;
  widget: Checkbox;
  subscribers: Subscriber[];
  readOnly: boolean;
  hasDiagnostic: boolean;
}

export interface GQLEditCheckboxMutationData {
  editCheckbox: GQLEditCheckboxPayload;
}

export interface GQLEditCheckboxPayload {
  __typename: string;
}

export interface GQLErrorPayload extends GQLEditCheckboxPayload, GQLUpdateWidgetFocusPayload {
  message: string;
}

export interface GQLUpdateWidgetFocusMutationData {
  updateWidgetFocus: GQLUpdateWidgetFocusPayload;
}

export interface GQLUpdateWidgetFocusPayload {
  __typename: string;
}
