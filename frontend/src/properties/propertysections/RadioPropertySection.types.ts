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
import { Radio, Subscriber } from 'form/Form.types';

export interface RadioPropertySectionProps {
  editingContextId: string;
  formId: string;
  widget: Radio;
  subscribers: Subscriber[];
  readOnly: boolean;
  hasDiagnostic: boolean;
}

export interface GQLEditRadioMutationData {
  editRadio: GQLEditRadioPayload;
}

export interface GQLEditRadioPayload {
  __typename: string;
}

export interface GQLErrorPayload extends GQLEditRadioPayload, GQLUpdateWidgetFocusPayload {
  message: string;
}

export interface GQLUpdateWidgetFocusMutationData {
  updateWidgetFocus: GQLUpdateWidgetFocusPayload;
}

export interface GQLUpdateWidgetFocusPayload {
  __typename: string;
}
