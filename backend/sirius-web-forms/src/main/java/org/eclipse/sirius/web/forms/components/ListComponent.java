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
package org.eclipse.sirius.web.forms.components;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.eclipse.sirius.web.components.Element;
import org.eclipse.sirius.web.components.IComponent;
import org.eclipse.sirius.web.forms.ListItem;
import org.eclipse.sirius.web.forms.description.ListDescription;
import org.eclipse.sirius.web.forms.elements.ListElementProps;
import org.eclipse.sirius.web.forms.validation.DiagnosticComponent;
import org.eclipse.sirius.web.forms.validation.DiagnosticComponentProps;
import org.eclipse.sirius.web.representations.VariableManager;

/**
 * The component used to create the list widget and its items.
 *
 * @author sbegaudeau
 */
public class ListComponent implements IComponent {

    public static final String CANDIDATE_VARIABLE = "candidate"; //$NON-NLS-1$

    private ListComponentProps props;

    public ListComponent(ListComponentProps props) {
        this.props = Objects.requireNonNull(props);
    }

    @Override
    public Element render() {
        VariableManager variableManager = this.props.getVariableManager();
        ListDescription listDescription = this.props.getListDescription();

        String id = listDescription.getIdProvider().apply(variableManager);
        String label = listDescription.getLabelProvider().apply(variableManager);
        List<Object> itemCandidates = listDescription.getItemsProvider().apply(variableManager);

        List<Element> children = List.of(new Element(DiagnosticComponent.class, new DiagnosticComponentProps(listDescription, variableManager)));

        List<ListItem> items = new ArrayList<>();
        for (Object itemCandidate : itemCandidates) {
            VariableManager itemVariableManager = variableManager.createChild();
            itemVariableManager.put(CANDIDATE_VARIABLE, itemCandidate);

            String itemId = listDescription.getItemIdProvider().apply(itemVariableManager);
            String itemLabel = listDescription.getItemLabelProvider().apply(itemVariableManager);
            String itemImageURL = listDescription.getItemImageURLProvider().apply(itemVariableManager);

            // @formatter:off
            ListItem item = ListItem.newListItem(itemId)
                    .label(itemLabel)
                    .imageURL(itemImageURL)
                    .build();
            // @formatter:on

            items.add(item);
        }

        // @formatter:off
        ListElementProps listElementProps = ListElementProps.newListElementProps(id)
                .label(label)
                .items(items)
                .children(children)
                .build();
        // @formatter:on

        return new Element(ListElementProps.TYPE, listElementProps);
    }

}
