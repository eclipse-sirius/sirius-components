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
package org.eclipse.sirius.web.emf.services.validation;

import org.assertj.core.api.Assertions;
import org.eclipse.emf.common.util.Diagnostic;

/**
 * Entry point of all the AssertJ assertions for diagnostics.
 *
 * @author sbegaudeau
 */
public class DiagnosticAssertions extends Assertions {
    public static DiagnosticAssert assertThat(Diagnostic diagnostic) {
        return new DiagnosticAssert(diagnostic);
    }
}
