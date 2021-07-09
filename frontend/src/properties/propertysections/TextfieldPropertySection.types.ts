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
import { Subscriber, Textarea, Textfield } from 'form/Form.types';

export interface TextfieldPropertySectionProps {
  editingContextId: string;
  formId: string;
  widget: Textfield | Textarea;
  subscribers: Subscriber[];
  readOnly: boolean;
  hasDiagnostic: boolean;
}

export interface GQLEditTextfieldMutationData {
  editTextfield: GQLEditTextfieldPayload;
}

export interface GQLEditTextfieldPayload {
  __typename: string;
}

export interface GQLErrorPayload extends GQLEditTextfieldPayload, GQLUpdateWidgetFocusPayload {
  message: string;
}

export interface GQLUpdateWidgetFocusMutationData {
  updateWidgetFocus: GQLUpdateWidgetFocusPayload;
}

export interface GQLUpdateWidgetFocusPayload {
  __typename: string;
}
