/*******************************************************************************
 * Copyright (c) 2018 Obeo and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2
 * which accompanies this distribution and is available at
 * https://www.eclipse.org/legal/epl-2.0.
 *******************************************************************************/

import React from 'react';

import { classNames } from '../../common/classnames';

import './Form.css';

const FORM__CLASS_NAMES = 'form';

/**
 * The Form component.
 */
export const Form = ({ className, ...props }) => {
  const formClassNames = classNames(FORM__CLASS_NAMES, className);
  return <form className={formClassNames} {...props} />;
};

const FIELD__CLASS_NAMES = 'field';

/**
 * The Field component used to contain the label, the description and the widget.
 */
export const Field = ({ className, ...props }) => {
  const fieldClassNames = classNames(FIELD__CLASS_NAMES, className);
  return <div className={fieldClassNames} {...props} />;
};

const LABEL__CLASS_NAMES = 'label body-l';

/**
 * The Label component.
 */
export const Label = ({ className, ...props }) => {
  const labelClassNames = classNames(LABEL__CLASS_NAMES, className);
  return <label className={labelClassNames} {...props} />;
};

const DESCRIPTION__CLASS_NAMES = 'description caption-m';

/**
 * The Description of the widget.
 */
export const Description = ({ className, ...props }) => {
  const descriptionClassNames = classNames(DESCRIPTION__CLASS_NAMES, className);
  return <p className={descriptionClassNames} {...props} />;
};

const TEXTFIELD__CLASS_NAMES = 'textfield';

/**
 * The Text widget.
 */
export const TextField = ({ className, ...props }) => {
  const textClassNames = classNames(TEXTFIELD__CLASS_NAMES, className);
  return <input className={textClassNames} type="text" {...props} />;
};

const ACTION_GROUP__CLASS_NAMES = 'actiongroup';

/**
 * The group of actions of the form, for example, the submit button.
 */
export const ActionGroup = ({ className, ...props }) => {
  const actionGroupClassNames = classNames(ACTION_GROUP__CLASS_NAMES, className);
  return <div className={actionGroupClassNames} {...props} />;
};

const ERROR_GROUP__CLASS_NAMES = 'errorgroup';

/**
 * The group of errors of the form.
 */
export const ErrorGroup = ({ className, ...props }) => {
  const errorGroupClassNames = classNames(ERROR_GROUP__CLASS_NAMES, className);
  return <div className={errorGroupClassNames} {...props} />;
};

const ERROR__CLASS_NAMES = 'error';

/**
 * An error to be displayed in the form.
 */
export const Error = ({ className, ...props }) => {
  const errorClassNames = classNames(ERROR__CLASS_NAMES, className);
  return <div className={errorClassNames} {...props} />;
};
