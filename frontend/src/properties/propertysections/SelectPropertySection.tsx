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
import MenuItem from '@material-ui/core/MenuItem';
import Select from '@material-ui/core/Select';
import Snackbar from '@material-ui/core/Snackbar';
import CloseIcon from '@material-ui/icons/Close';
import gql from 'graphql-tag';
import { PropertySectionLabel } from 'properties/propertysections/PropertySectionLabel';
import {
  GQLEditSelectMutationData,
  GQLEditSelectPayload,
  GQLErrorPayload,
  GQLUpdateWidgetFocusMutationData,
  SelectPropertySectionProps,
} from 'properties/propertysections/SelectPropertySection.types';
import React, { useEffect, useState } from 'react';
import { v4 as uuid } from 'uuid';

export const editSelectMutation = gql`
  mutation editSelect($input: EditSelectInput!) {
    editSelect(input: $input) {
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

const isErrorPayload = (payload: GQLEditSelectPayload): payload is GQLErrorPayload =>
  payload.__typename === 'ErrorPayload';

export const SelectPropertySection = ({
  editingContextId,
  formId,
  widget,
  subscribers,
  readOnly,
}: SelectPropertySectionProps) => {
  const [message, setMessage] = useState(null);
  const [isFocused, setFocus] = useState(false);

  const [editSelect, { loading, error, data }] = useMutation<GQLEditSelectMutationData>(editSelectMutation);
  const onChange = (event) => {
    const newValue = event.target.value;
    const variables = {
      input: {
        id: uuid(),
        editingContextId,
        representationId: formId,
        selectId: widget.id,
        newValue,
      },
    };
    editSelect({ variables });
  };

  useEffect(() => {
    if (!loading) {
      if (error) {
        setMessage('An unexpected error has occurred, please refresh the page');
      }
      if (data) {
        const { editSelect } = data;
        if (isErrorPayload(editSelect)) {
          setMessage(editSelect.message);
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

  const onFocus = () => {
    if (!isFocused) {
      setFocus(true);
      sendUpdateWidgetFocus(true);
    }
  };
  const onBlur = () => {
    setFocus(false);
    sendUpdateWidgetFocus(false);
  };

  let options = [];
  if (!widget.valueRequired) {
    options.push(
      <MenuItem value="">
        <em>None</em>
      </MenuItem>
    );
  }
  widget.options.forEach((option) =>
    options.push(
      <MenuItem value={option.id} key={option.id}>
        {option.label}
      </MenuItem>
    )
  );

  return (
    <div>
      <PropertySectionLabel label={widget.label} subscribers={subscribers} />
      <Select
        value={widget.value || ''}
        onChange={onChange}
        displayEmpty
        onFocus={onFocus}
        onBlur={onBlur}
        fullWidth
        data-testid={widget.label}
        disabled={readOnly}>
        {options}
      </Select>
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
    </div>
  );
};
