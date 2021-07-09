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
import { makeStyles } from '@material-ui/core/styles';
import Typography from '@material-ui/core/Typography';
import { Checkbox, List, Radio, Select, Textarea, Textfield, Widget, WidgetSubscription } from 'form/Form.types';
import { GroupProps } from 'properties/Group.types';
import { CheckboxPropertySection } from 'properties/propertysections/CheckboxPropertySection';
import { ListPropertySection } from 'properties/propertysections/ListPropertySection';
import { RadioPropertySection } from 'properties/propertysections/RadioPropertySection';
import { SelectPropertySection } from 'properties/propertysections/SelectPropertySection';
import { TextfieldPropertySection } from 'properties/propertysections/TextfieldPropertySection';
import React from 'react';

const useGroupStyles = makeStyles((theme) => ({
  group: {
    display: 'flex',
    flexDirection: 'column',
  },
  title: {
    whiteSpace: 'nowrap',
    overflow: 'hidden',
    textOverflow: 'ellipsis',
  },
  sections: {
    display: 'flex',
    flexDirection: 'column',
    '& > *': {
      marginBottom: theme.spacing(2),
    },
  },
}));

export const Group = ({ editingContextId, formId, group, widgetSubscriptions, readOnly }: GroupProps) => {
  const classes = useGroupStyles();

  let propertySections = group.widgets.map((widget) =>
    widgetToPropertySection(editingContextId, formId, widget, widgetSubscriptions, readOnly)
  );

  return (
    <div className={classes.group}>
      <Typography variant="subtitle1" className={classes.title} gutterBottom>
        {group.label}
      </Typography>
      <div className={classes.sections}>{propertySections}</div>
    </div>
  );
};

const isTextfield = (widget: Widget): widget is Textfield => widget.__typename === 'Textfield';
const isTextarea = (widget: Widget): widget is Textarea => widget.__typename === 'Textarea';
const isCheckbox = (widget: Widget): widget is Checkbox => widget.__typename === 'Checkbox';
const isSelect = (widget: Widget): widget is Select => widget.__typename === 'Select';
const isRadio = (widget: Widget): widget is Radio => widget.__typename === 'Radio';
const isList = (widget: Widget): widget is List => widget.__typename === 'List';
const containsDiagnostic = (widget: Widget) => widget.diagnostics.length > 0;

const widgetToPropertySection = (
  editingContextId: string,
  formId: string,
  widget: Widget,
  widgetSubscriptions: WidgetSubscription[],
  readOnly: boolean
) => {
  let subscribers = [];
  widgetSubscriptions
    .filter((subscription) => subscription.widgetId === widget.id)
    .forEach((subscription) => subscribers.push(...subscription.subscribers));

  const hasDiagnostic = containsDiagnostic(widget);

  let propertySection = null;
  if (isTextfield(widget) || isTextarea(widget)) {
    propertySection = (
      <TextfieldPropertySection
        editingContextId={editingContextId}
        formId={formId}
        widget={widget}
        subscribers={subscribers}
        key={widget.id}
        readOnly={readOnly}
        hasDiagnostic={hasDiagnostic}
      />
    );
  } else if (isCheckbox(widget)) {
    propertySection = (
      <CheckboxPropertySection
        editingContextId={editingContextId}
        formId={formId}
        widget={widget}
        subscribers={subscribers}
        key={widget.id}
        readOnly={readOnly}
        hasDiagnostic={hasDiagnostic}
      />
    );
  } else if (isSelect(widget)) {
    propertySection = (
      <SelectPropertySection
        editingContextId={editingContextId}
        formId={formId}
        widget={widget}
        subscribers={subscribers}
        key={widget.id}
        readOnly={readOnly}
        hasDiagnostic={hasDiagnostic}
      />
    );
  } else if (isRadio(widget)) {
    propertySection = (
      <RadioPropertySection
        editingContextId={editingContextId}
        formId={formId}
        widget={widget}
        subscribers={subscribers}
        key={widget.id}
        readOnly={readOnly}
        hasDiagnostic={hasDiagnostic}
      />
    );
  } else if (isList(widget)) {
    propertySection = (
      <ListPropertySection
        widget={widget}
        key={widget.id}
        subscribers={subscribers}
        readonly={readOnly}
        hasDiagnostic={hasDiagnostic}
      />
    );
  } else {
    console.error(`Unsupported widget type ${widget.__typename}`);
  }
  return propertySection;
};
