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
package org.eclipse.sirius.web.selection.renderer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.eclipse.sirius.web.representations.VariableManager;
import org.eclipse.sirius.web.selection.Selection;
import org.eclipse.sirius.web.selection.SelectionObject;
import org.eclipse.sirius.web.selection.description.SelectionDescription;

/**
 * Renderer used to create the selection representation from its description and some variables.
 *
 * @author arichard
 */
public class SelectionRenderer {

    private VariableManager variableManager;

    private SelectionDescription selectionDescription;

    public SelectionRenderer(VariableManager variableManager, SelectionDescription selectionDescription) {
        this.variableManager = Objects.requireNonNull(variableManager);
        this.selectionDescription = Objects.requireNonNull(selectionDescription);
    }

    public Selection render() {
        UUID selectionId = this.selectionDescription.getIdProvider().apply(this.variableManager);
        String label = this.selectionDescription.getLabelProvider().apply(this.variableManager);
        String message = this.selectionDescription.getMessageProvider().apply(this.variableManager);
        String targetObjectId = this.selectionDescription.getTargetObjectIdProvider().apply(this.variableManager);

        List<SelectionObject> objects = new ArrayList<>();
        List<Object> selectionObjects = this.selectionDescription.getObjectsProvider().apply(this.variableManager);
        for (Object selectionObject : selectionObjects) {
            VariableManager selectionObjectVariableManager = this.variableManager.createChild();
            selectionObjectVariableManager.put(VariableManager.SELF, selectionObject);
            objects.add(this.renderSelectionObject(selectionObjectVariableManager));
        }

        // @formatter:off
        return Selection.newSelection(selectionId)
                .descriptionId(this.selectionDescription.getId())
                .label(label)
                .targetObjectId(targetObjectId)
                .message(message)
                .objects(objects)
                .build();
        // @formatter:on
    }

    private SelectionObject renderSelectionObject(VariableManager selectionObjectVariableManager) {
        UUID id = this.selectionDescription.getIdProvider().apply(selectionObjectVariableManager);
        String label = this.selectionDescription.getLabelProvider().apply(selectionObjectVariableManager);
        String iconURL = this.selectionDescription.getIconURLProvider().apply(selectionObjectVariableManager);

        // @formatter:off
        return SelectionObject.newSelectionObject(id)
                .label(label)
                .iconURL(iconURL)
                .build();
        // @formatter:on
    }
}
