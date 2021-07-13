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
package org.eclipse.sirius.web.graphql.schema;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.sirius.web.collaborative.forms.api.dto.WidgetSubscription;
import org.eclipse.sirius.web.forms.AbstractWidget;
import org.eclipse.sirius.web.forms.Checkbox;
import org.eclipse.sirius.web.forms.Form;
import org.eclipse.sirius.web.forms.Group;
import org.eclipse.sirius.web.forms.ListItem;
import org.eclipse.sirius.web.forms.MultiSelect;
import org.eclipse.sirius.web.forms.Page;
import org.eclipse.sirius.web.forms.Radio;
import org.eclipse.sirius.web.forms.RadioOption;
import org.eclipse.sirius.web.forms.Select;
import org.eclipse.sirius.web.forms.SelectOption;
import org.eclipse.sirius.web.forms.Textarea;
import org.eclipse.sirius.web.forms.Textfield;
import org.eclipse.sirius.web.forms.description.FormDescription;
import org.eclipse.sirius.web.graphql.utils.providers.GraphQLInterfaceTypeProvider;
import org.eclipse.sirius.web.graphql.utils.providers.GraphQLObjectTypeProvider;
import org.eclipse.sirius.web.graphql.utils.schema.ITypeProvider;
import org.springframework.stereotype.Service;

import graphql.schema.GraphQLType;

/**
 * This class is used to create all the definitions of the types related to the form-based representation.
 *
 * @author sbegaudeau
 */
@Service
public class FormTypesProvider implements ITypeProvider {
    public static final String FORM_TYPE = "Form"; //$NON-NLS-1$

    public static final String WIDGET_TYPE = "Widget"; //$NON-NLS-1$

    public static final String TEXTFIELD_TYPE = "Textfield"; //$NON-NLS-1$

    public static final String CHECKBOX_TYPE = "Checkbox"; //$NON-NLS-1$

    public static final String TEXTAREA_TYPE = "Textarea"; //$NON-NLS-1$

    public static final String SELECT_TYPE = "Select"; //$NON-NLS-1$

    public static final String MULTI_SELECT_TYPE = "MultiSelect"; //$NON-NLS-1$

    public static final String RADIO_TYPE = "Radio"; //$NON-NLS-1$

    private final GraphQLObjectTypeProvider graphQLObjectTypeProvider = new GraphQLObjectTypeProvider();

    private final GraphQLInterfaceTypeProvider graphQLInterfaceTypeProvider = new GraphQLInterfaceTypeProvider();

    @Override
    public Set<GraphQLType> getTypes() {
        // @formatter:off
        List<Class<?>> objectClasses = List.of(
            Form.class,
            Page.class,
            Group.class,
            Textfield.class,
            Textarea.class,
            Checkbox.class,
            Radio.class,
            RadioOption.class,
            Select.class,
            MultiSelect.class,
            SelectOption.class,
            org.eclipse.sirius.web.forms.List.class,
            ListItem.class,
            FormDescription.class,
            WidgetSubscription.class
        );
        var graphQLObjectTypes = objectClasses.stream()
                .map(this.graphQLObjectTypeProvider::getType)
                .collect(Collectors.toUnmodifiableList());

        List<Class<?>> interfaceClasses = List.of(
            AbstractWidget.class
        );
        var graphQLInterfaceTypes = interfaceClasses.stream()
                .map(this.graphQLInterfaceTypeProvider::getType)
                .collect(Collectors.toUnmodifiableList());
        // @formatter:on

        Set<GraphQLType> types = new LinkedHashSet<>();
        types.addAll(graphQLObjectTypes);
        types.addAll(graphQLInterfaceTypes);
        return types;
    }

}
