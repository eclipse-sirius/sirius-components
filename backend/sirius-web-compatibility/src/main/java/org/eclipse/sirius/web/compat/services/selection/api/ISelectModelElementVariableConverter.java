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
package org.eclipse.sirius.web.compat.services.selection.api;

import org.eclipse.sirius.viewpoint.description.tool.SelectModelElementVariable;
import org.eclipse.sirius.web.selection.description.SelectionDescription;

/**
 * Used to convert a Sirius SelectModelElementVariable into an Sirius Web SelectionDescription.
 *
 * @author arichard
 */
public interface ISelectModelElementVariableConverter {
    SelectionDescription convert(SelectModelElementVariable selectModelElementVariable, org.eclipse.sirius.diagram.description.DiagramDescription diagramDescription);
}
