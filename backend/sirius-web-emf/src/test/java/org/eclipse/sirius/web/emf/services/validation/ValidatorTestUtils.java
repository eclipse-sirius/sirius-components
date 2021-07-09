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

import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;

/**
 * Utility class to help testing validation.
 *
 * @author gcoutable
 */
public final class ValidatorTestUtils {

    private ValidatorTestUtils() {
        // Prevent instantiation
    }

    public static void diagnosticAssert(Diagnostic actual, Diagnostic expected) {
        assertThat(actual.getSeverity()).isEqualTo(expected.getSeverity());
        assertThat(actual.getCode()).isEqualTo(expected.getCode());
        assertThat(actual.getMessage()).isEqualTo(expected.getMessage());
        if (actual.getChildren() != null) {
            assertThat(actual.getChildren()).hasSameSizeAs(expected.getChildren());
            for (int i = 0; i < actual.getChildren().size(); ++i) {
                ValidatorTestUtils.diagnosticAssert(actual.getChildren().get(i), expected.getChildren().get(i));
            }

        } else {
            assertThat(actual.getChildren()).isEqualTo(expected.getChildren());
        }
        if (actual.getData() != null) {
            assertThat(actual.getData()).hasSameSizeAs(expected.getData());
            for (int i = 0; i < actual.getData().size(); ++i) {
                assertThat(actual.getData().get(i)).isEqualTo(expected.getData().get(i));
            }
        } else {
            assertThat(actual.getData()).isEqualTo(expected.getData());
        }
    }

    public static BasicDiagnostic getNewBasicDiagnostic() {
        return ValidatorTestUtils.getNewBasicDiagnostic(Diagnostic.OK);
    }

    public static BasicDiagnostic getNewBasicDiagnostic(int severity) {
        return new BasicDiagnostic(severity, null, 0, null, null);
    }

}
