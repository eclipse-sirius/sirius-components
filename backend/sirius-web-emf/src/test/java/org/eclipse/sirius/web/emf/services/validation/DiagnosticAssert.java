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

import static org.assertj.core.api.Assertions.assertThat;
import static org.eclipse.sirius.web.emf.services.validation.DiagnosticAssertions.assertThat;

import org.assertj.core.api.AbstractAssert;
import org.eclipse.emf.common.util.Diagnostic;

/**
 * Custom assertion class used to perform tests on diagnostics.
 *
 * @author sbegaudeau
 */
public class DiagnosticAssert extends AbstractAssert<DiagnosticAssert, Diagnostic> {

    public DiagnosticAssert(Diagnostic diagnostic) {
        super(diagnostic, DiagnosticAssert.class);
    }

    @Override
    public DiagnosticAssert isEqualTo(Object expected) {
        if (expected instanceof Diagnostic) {
            Diagnostic expectedDiagnostic = (Diagnostic) expected;

            assertThat(this.actual.getSeverity()).isEqualTo(expectedDiagnostic.getSeverity());
            assertThat(this.actual.getCode()).isEqualTo(expectedDiagnostic.getCode());
            assertThat(this.actual.getMessage()).isEqualTo(expectedDiagnostic.getMessage());
            if (this.actual.getChildren() != null) {
                assertThat(this.actual.getChildren()).hasSameSizeAs(expectedDiagnostic.getChildren());
                for (int i = 0; i < this.actual.getChildren().size(); ++i) {
                    assertThat(this.actual.getChildren().get(i)).isEqualTo(expectedDiagnostic.getChildren().get(i));
                }

            } else {
                assertThat(this.actual.getChildren()).isEqualTo(expectedDiagnostic.getChildren());
            }
            if (this.actual.getData() != null) {
                assertThat(this.actual.getData()).hasSameSizeAs(expectedDiagnostic.getData());
                for (int i = 0; i < this.actual.getData().size(); ++i) {
                    assertThat(this.actual.getData().get(i)).isEqualTo(expectedDiagnostic.getData().get(i));
                }
            } else {
                assertThat(this.actual.getData()).isEqualTo(expectedDiagnostic.getData());
            }
        }
        return this;
    }
}
