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
package org.eclipse.sirius.web.forms.validation;

import java.text.MessageFormat;
import java.util.Objects;
import java.util.UUID;

import org.eclipse.sirius.web.annotations.Immutable;
import org.eclipse.sirius.web.components.IProps;

/**
 * The properties of the diagnostic element for forms.
 *
 * @author gcoutable
 */
@Immutable
public final class DiagnosticElementProps implements IProps {
    public static final String TYPE = "Diagnostic"; //$NON-NLS-1$

    private UUID id;

    private String kind;

    private String message;

    private DiagnosticElementProps() {
        // Prevent instantiation
    }

    public UUID getId() {
        return this.id;
    }

    public String getKind() {
        return this.kind;
    }

    public String getMessage() {
        return this.message;
    }

    public static Builder newDiagnosticElementProps(UUID id) {
        return new Builder(id);
    }

    @Override
    public String toString() {
        String pattern = "{0} '{'id: {1}, kind: {2}'}'"; //$NON-NLS-1$
        return MessageFormat.format(pattern, this.getClass().getSimpleName(), this.id, this.kind);
    }

    /**
     * The builder of the diagnostic element props.
     *
     * @author gcoutable
     */
    @SuppressWarnings("checkstyle:HiddenField")
    public static final class Builder {

        private UUID id;

        private String kind;

        private String message;

        private Builder(UUID id) {
            this.id = Objects.requireNonNull(id);
        }

        public Builder kind(String kind) {
            this.kind = Objects.requireNonNull(kind);
            return this;
        }

        public Builder message(String message) {
            this.message = Objects.requireNonNull(message);
            return this;
        }

        public DiagnosticElementProps build() {
            DiagnosticElementProps diagnosticElementProps = new DiagnosticElementProps();
            diagnosticElementProps.id = Objects.requireNonNull(this.id);
            diagnosticElementProps.kind = Objects.requireNonNull(this.kind);
            diagnosticElementProps.message = Objects.requireNonNull(this.message);
            return diagnosticElementProps;
        }
    }

}
