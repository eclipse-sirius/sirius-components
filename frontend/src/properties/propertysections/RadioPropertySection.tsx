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
import { FormHelperText } from '@material-ui/core';
import FormControl from '@material-ui/core/FormControl';
import FormControlLabel from '@material-ui/core/FormControlLabel';
import IconButton from '@material-ui/core/IconButton';
import Radio from '@material-ui/core/Radio';
import RadioGroup from '@material-ui/core/RadioGroup';
import Snackbar from '@material-ui/core/Snackbar';
import { makeStyles } from '@material-ui/core/styles';
import CloseIcon from '@material-ui/icons/Close';
import gql from 'graphql-tag';
import { PropertySectionLabel } from 'properties/propertysections/PropertySectionLabel';
import {
  GQLEditRadioMutationData,
  GQLEditRadioPayload,
  GQLErrorPayload,
  GQLUpdateWidgetFocusMutationData,
  GQLUpdateWidgetFocusPayload,
  RadioPropertySectionProps,
} from 'properties/propertysections/RadioPropertySection.types';
import React, { useEffect, useState } from 'react';
import { v4 as uuid } from 'uuid';

export const editRadioMutation = gql`
  mutation editRadio($input: EditRadioInput!) {
    editRadio(input: $input) {
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

const useRadioPropertySectionStyles = makeStyles((theme) => ({
  radioGroupRoot: {
    flexDirection: 'row',
  },
}));

const isErrorPayload = (payload: GQLEditRadioPayload | GQLUpdateWidgetFocusPayload): payload is GQLErrorPayload =>
  payload.__typename === 'ErrorPayload';

export const RadioPropertySection = ({
  editingContextId,
  formId,
  widget,
  subscribers,
  readOnly,
  hasDiagnostic,
}: RadioPropertySectionProps) => {
  const classes = useRadioPropertySectionStyles();
  const [message, setMessage] = useState(null);

  const [editRadio, { loading, error, data }] = useMutation<GQLEditRadioMutationData>(editRadioMutation);

  const onChange = (event) => {
    const newValue = event.target.value;
    const variables = {
      input: {
        id: uuid(),
        editingContextId,
        representationId: formId,
        radioId: widget.id,
        newValue,
      },
    };
    editRadio({ variables });
  };

  useEffect(() => {
    if (!loading) {
      if (error) {
        setMessage('An unexpected error has occurred, please refresh the page');
      }
      if (data) {
        const { editRadio } = data;
        if (isErrorPayload(editRadio)) {
          setMessage(editRadio.message);
        }
      }
    }
  }, [loading, error, data]);

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
        setMessage('An unexpected error has occurred, please refresh the page');
      }
      if (updateWidgetFocusData) {
        const { updateWidgetFocus } = updateWidgetFocusData;
        if (isErrorPayload(updateWidgetFocus)) {
          const { message } = updateWidgetFocus;
          setMessage(message);
        }
      }
    }
  }, [updateWidgetFocusLoading, updateWidgetFocusData, updateWidgetFocusError]);

  const onFocus = () => sendUpdateWidgetFocus(true);
  const onBlur = () => sendUpdateWidgetFocus(false);

  const selectedOption = widget.options.find((option) => option.selected);
  return (
    <FormControl error={hasDiagnostic}>
      <PropertySectionLabel label={widget.label} subscribers={subscribers} />
      <RadioGroup
        classes={{ root: classes.radioGroupRoot }}
        aria-label={widget.label}
        name={widget.label}
        value={selectedOption ? selectedOption.id : null}
        onChange={onChange}>
        {widget.options.map((option) => (
          <FormControlLabel
            value={option.id}
            control={<Radio color="primary" onFocus={onFocus} onBlur={onBlur} data-testid={option.label} />}
            label={option.label}
            key={option.id}
            disabled={readOnly}
          />
        ))}
      </RadioGroup>
      <FormHelperText>{hasDiagnostic ? widget.diagnostics[0].message : null}</FormHelperText>
      <Snackbar
        anchorOrigin={{
          vertical: 'bottom',
          horizontal: 'right',
        }}
        open={!!message}
        autoHideDuration={3000}
        onClose={() => setMessage(null)}
        message={message}
        action={
          <IconButton size="small" aria-label="close" color="inherit" onClick={() => setMessage(null)}>
            <CloseIcon fontSize="small" />
          </IconButton>
        }
        data-testid="error"
      />
    </FormControl>
  );
};
