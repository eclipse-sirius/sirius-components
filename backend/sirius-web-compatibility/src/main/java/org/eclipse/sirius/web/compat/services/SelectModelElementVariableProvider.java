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
package org.eclipse.sirius.web.compat.services;

import java.util.Optional;
import java.util.stream.Stream;

import org.eclipse.sirius.diagram.description.tool.NodeCreationVariable;
import org.eclipse.sirius.viewpoint.description.SubVariable;
import org.eclipse.sirius.viewpoint.description.tool.SelectModelElementVariable;

/**
 * Retrieves the first SelectModelElementVariable contained in a NodeCreationVariable.
 *
 * @author arichard
 */
public class SelectModelElementVariableProvider {

    public SelectModelElementVariableProvider() {
    }

    public Optional<SelectModelElementVariable> getSelectModelElementVariable(NodeCreationVariable nodeCreationVariable) {
        // @formatter:off
        Optional<SelectModelElementVariable> selectModelElementVariable = Stream.of(nodeCreationVariable)
                .map(variable -> {
                    for (SubVariable subVariable : variable.getSubVariables()) {
                        if (subVariable instanceof SelectModelElementVariable) {
                            return subVariable;
                        }
                    }
                    return Stream.empty();
                })
                .filter(SelectModelElementVariable.class::isInstance)
                .map(SelectModelElementVariable.class::cast)
                .findFirst();
        // @formatter:on
        return selectModelElementVariable;
    }
}
