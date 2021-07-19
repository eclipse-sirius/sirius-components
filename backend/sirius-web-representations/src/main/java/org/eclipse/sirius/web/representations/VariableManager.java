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
package org.eclipse.sirius.web.representations;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Used to manage the variables.
 *
 * @author sbegaudeau
 */
public class VariableManager {

    public static final String SELF = "self"; //$NON-NLS-1$

    public static final String SELECTED_OBJECT_IN_SELECTION_REPRESENTATION = "selectedObject"; //$NON-NLS-1$

    /**
     * The parent variable manager.
     */
    private VariableManager parent;

    /**
     * The variables.
     */
    private Map<String, Object> variables = new HashMap<>();

    /**
     * The constructor.
     */
    public VariableManager() {
        // do nothing
    }

    /**
     * The constructor.
     *
     * @param parent
     *            The parent of this VariableManager.
     */
    public VariableManager(VariableManager parent) {
        this.parent = parent;
    }

    public Object put(String name, Object value) {
        Object previous = this.variables.put(name, value);
        return previous;
    }

    public Map<String, Object> getVariables() {
        if (this.parent != null) {
            Map<String, Object> parentVariables = this.parent.getVariables();
            parentVariables.putAll(this.variables);
            return parentVariables;
        }
        return new HashMap<>(this.variables);
    }

    public <T> Optional<T> get(String name, Class<T> expectedType) {
        // @formatter:off
        return Optional.ofNullable(this.get(name))
                       .filter(expectedType::isInstance)
                       .map(expectedType::cast);
        // @formatter:on
    }

    private Object get(String name) {
        Object value = this.variables.get(name);
        if (value == null && this.parent != null) {
            value = this.parent.get(name);
        }
        return value;
    }

    public VariableManager createChild() {
        return new VariableManager(this);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, Object> var : this.getVariables().entrySet()) {
            String name = var.getKey();
            Object value = var.getValue();
            boolean local = this.variables.containsKey(name);
            stringBuilder.append(name).append(" = ").append(value); //$NON-NLS-1$
            if (!local) {
                stringBuilder.append(" [inherited]"); //$NON-NLS-1$
            }
            stringBuilder.append(System.lineSeparator());
        }
        return stringBuilder.toString();
    }
}
