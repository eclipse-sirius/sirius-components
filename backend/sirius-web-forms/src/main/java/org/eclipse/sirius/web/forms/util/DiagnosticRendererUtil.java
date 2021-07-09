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
package org.eclipse.sirius.web.forms.util;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.sirius.web.components.Element;
import org.eclipse.sirius.web.forms.description.AbstractWidgetDescription;
import org.eclipse.sirius.web.forms.validation.DiagnosticComponent;
import org.eclipse.sirius.web.forms.validation.DiagnosticComponentProps;
import org.eclipse.sirius.web.representations.VariableManager;

/**
 * Utility class used to render diagnostic elements.
 *
 * @author gcoutable
 */
public class DiagnosticRendererUtil {

    public List<Element> renderDiagnostics(AbstractWidgetDescription widgetDescription, VariableManager variableManager) {
        List<Element> children = new ArrayList<>();
        List<Object> diagnostics = widgetDescription.getDiagnosticsProviders().apply(variableManager);
        for (Object diagnostic : diagnostics) {
            var diagnosticComponentProps = new DiagnosticComponentProps(diagnostic, widgetDescription);
            children.add(new Element(DiagnosticComponent.class, diagnosticComponentProps));
        }
        return children;
    }

}
