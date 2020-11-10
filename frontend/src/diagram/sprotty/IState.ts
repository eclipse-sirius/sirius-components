/*******************************************************************************
 * Copyright (c) 2019, 2020 Obeo.
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
import { SModelRoot, SModelElement, SModelElementSchema } from 'sprotty';
/**
 * This state class regroup all required state to use in sprotty handlers/listener.
 * @hmarchadour
 */
export interface IState {
  currentRoot?: SModelRoot | SModelElementSchema;
  activeTool?: any;
  sourceElement?: SModelElement;
}
