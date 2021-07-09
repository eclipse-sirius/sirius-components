/*******************************************************************************
 * Copyright (c) 2019, 2021 Obeo.
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
import { useMutation } from '@apollo/client';
import IconButton from '@material-ui/core/IconButton';
import Snackbar from '@material-ui/core/Snackbar';
import TextField from '@material-ui/core/TextField';
import CloseIcon from '@material-ui/icons/Close';
import { useMachine } from '@xstate/react';
import { Textarea, Widget } from 'form/Form.types';
import gql from 'graphql-tag';
import { PropertySectionLabel } from 'properties/propertysections/PropertySectionLabel';
import {
  GQLEditTextfieldMutationData,
  GQLEditTextfieldPayload,
  GQLErrorPayload,
  GQLUpdateWidgetFocusMutationData,
  GQLUpdateWidgetFocusPayload,
  TextfieldPropertySectionProps,
} from 'properties/propertysections/TextfieldPropertySection.types';
import {
  ChangeValueEvent,
  InitializeEvent,
  SchemaValue,
  ShowToastEvent,
  TextfieldPropertySectionContext,
  TextfieldPropertySectionEvent,
  textfieldPropertySectionMachine,
} from 'properties/propertysections/TextfieldPropertySectionMachine';
import React, { useEffect } from 'react';
import { v4 as uuid } from 'uuid';

const editTextfieldMutation = gql`
  mutation editTextfield($input: EditTextfieldInput!) {
    editTextfield(input: $input) {
      __typename
      ... on ErrorPayload {
        message
      }
    }
  }
`;

const updateWidgetFocusMutation = gql`
  mutation updateWidgetFocus($input: UpdateWidgetFocusInput!) {
    updateWidgetFocus(input: $input) {
      __typename
      ... on ErrorPayload {
        message
      }
    }
  }
`;

const isTextarea = (widget: Widget): widget is Textarea => widget.__typename === 'Textarea';
const isErrorPayload = (payload: GQLEditTextfieldPayload | GQLUpdateWidgetFocusPayload): payload is GQLErrorPayload =>
  payload.__typename === 'ErrorPayload';

/**
 * Defines the content of a Textfield property section.
 * The content is submitted when the focus is lost and when pressing the "Enter" key.
 */
export const TextfieldPropertySection = ({
  editingContextId,
  formId,
  widget,
  subscribers,
  readOnly,
  hasDiagnostic,
}: TextfieldPropertySectionProps) => {
  const [{ value: schemaValue, context }, dispatch] = useMachine<
    TextfieldPropertySectionContext,
    TextfieldPropertySectionEvent
  >(textfieldPropertySectionMachine);
  const { textfieldPropertySection, toast } = schemaValue as SchemaValue;
  const { value, message } = context;

  useEffect(() => {
    const initializeEvent: InitializeEvent = { type: 'INITIALIZE', value: widget.stringValue };
    dispatch(initializeEvent);
  }, [dispatch, widget.stringValue]);

  const onChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    const { value } = event.target;
    const changeValueEvent: ChangeValueEvent = { type: 'CHANGE_VALUE', value };
    dispatch(changeValueEvent);
  };

  const [editTextfield, { loading: updateTextfieldLoading, data: updateTextfieldData, error: updateTextfieldError }] =
    useMutation<GQLEditTextfieldMutationData>(editTextfieldMutation);
  const sendEditedValue = () => {
    if (textfieldPropertySection === 'edited') {
      const variables = {
        input: {
          id: uuid(),
          editingContextId,
          representationId: formId,
          textfieldId: widget.id,
          newValue: value,
        },
      };
      editTextfield({ variables });
    }
  };

  useEffect(() => {
    if (!updateTextfieldLoading) {
      let hasError = false;
      if (updateTextfieldError) {
        const showToastEvent: ShowToastEvent = {
          type: 'SHOW_TOAST',
          message: 'An unexpected error has occurred, please refresh the page',
        };
        dispatch(showToastEvent);

        hasError = true;
      }
      if (updateTextfieldData) {
        const { editTextfield } = updateTextfieldData;
        if (isErrorPayload(editTextfield)) {
          const { message } = editTextfield;
          const showToastEvent: ShowToastEvent = { type: 'SHOW_TOAST', message };
          dispatch(showToastEvent);

          hasError = true;
        }
      }

      if (hasError) {
        const initializeEvent: InitializeEvent = { type: 'INITIALIZE', value: widget.stringValue };
        dispatch(initializeEvent);
      }
    }
  }, [updateTextfieldLoading, updateTextfieldData, updateTextfieldError, widget, dispatch]);

  const [
    updateWidgetFocus,
    { loading: updateWidgetFocusLoading, data: updateWidgetFocusData, error: updateWidgetFocusError },
  ] = useMutation<GQLUpdateWidgetFocusMutationData>(updateWidgetFocusMutation);
  const sendUpdateWidgetFocus = (selected: boolean) => {
    const variables = {
      input: {
        id: uuid(),
        editingContextId,
        representationId: formId,
        widgetId: widget.id,
        selected,
      },
    };
    updateWidgetFocus({ variables });
  };

  useEffect(() => {
    if (!updateWidgetFocusLoading) {
      if (updateWidgetFocusError) {
        const showToastEvent: ShowToastEvent = {
          type: 'SHOW_TOAST',
          message: 'An unexpected error has occurred, please refresh the page',
        };
        dispatch(showToastEvent);
      }
      if (updateWidgetFocusData) {
        const { updateWidgetFocus } = updateWidgetFocusData;
        if (isErrorPayload(updateWidgetFocus)) {
          const { message } = updateWidgetFocus;
          const showToastEvent: ShowToastEvent = { type: 'SHOW_TOAST', message };
          dispatch(showToastEvent);
        }
      }
    }
  }, [updateWidgetFocusLoading, updateWidgetFocusData, updateWidgetFocusError, dispatch]);

  const onFocus = () => sendUpdateWidgetFocus(true);
  const onBlur = () => {
    sendUpdateWidgetFocus(false);
    sendEditedValue();
  };

  const onKeyPress = (event) => {
    if ('Enter' === event.key) {
      sendEditedValue();
    }
  };

  return (
    <div>
      <PropertySectionLabel label={widget.label} subscribers={subscribers} />
      <TextField
        name={widget.label}
        placeholder={widget.label}
        value={value}
        margin="dense"
        multiline={isTextarea(widget)}
        rowsMax={isTextarea(widget) ? 4 : 1}
        fullWidth
        onChange={onChange}
        onBlur={onBlur}
        onFocus={onFocus}
        onKeyPress={onKeyPress}
        data-testid={widget.label}
        disabled={readOnly}
        error={hasDiagnostic}
        helperText={hasDiagnostic ? widget.diagnostics[0].message : null}
      />
      <Snackbar
        anchorOrigin={{
          vertical: 'bottom',
          horizontal: 'right',
        }}
        open={toast === 'visible'}
        autoHideDuration={3000}
        onClose={() => dispatch({ type: 'HIDE_TOAST' })}
        message={message}
        action={
          <IconButton size="small" aria-label="close" color="inherit" onClick={() => dispatch({ type: 'HIDE_TOAST' })}>
            <CloseIcon fontSize="small" />
          </IconButton>
        }
        data-testid="error"
      />
    </div>
  );
};
