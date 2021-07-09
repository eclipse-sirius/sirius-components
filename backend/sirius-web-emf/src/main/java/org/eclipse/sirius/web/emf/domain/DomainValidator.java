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
package org.eclipse.sirius.web.emf.domain;

import java.util.Map;
import java.util.Optional;

import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.DiagnosticChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EValidator;
import org.eclipse.sirius.web.domain.Domain;
import org.eclipse.sirius.web.domain.DomainPackage;

/**
 * The validator for Domain.
 *
 * @author gcoutable
 */
public class DomainValidator implements EValidator {
    public static final String DOMAIN_URI_SCHEME = "domain://"; //$NON-NLS-1$

    private static final String SIRIUS_WEB_EMF_PACKAGE = "org.eclipse.sirius.web.emf"; //$NON-NLS-1$

    private static final String DOMAIN_NAME_ERROR_MESSAGE = "The domain name should not be empty."; //$NON-NLS-1$

    private static final String DOMAIN_URI_SCHEME_ERROR_MESSAGE = "The domain %1$s uri's does not start with \"domain://\"."; //$NON-NLS-1$

    @Override
    public boolean validate(EDataType eDataType, Object value, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

    @Override
    public boolean validate(EClass eClass, EObject eObject, DiagnosticChain diagnostics, Map<Object, Object> context) {
        boolean isValid = true;
        if (eObject instanceof Domain) {
            Domain domain = (Domain) eObject;
            isValid = this.uriStartWithValidate(domain, diagnostics) && isValid;
            isValid = this.nameIsNotBlankValidate(domain, diagnostics) && isValid;
        }
        return isValid;
    }

    private boolean uriStartWithValidate(Domain domain, DiagnosticChain diagnostics) {
        boolean isValid = Optional.ofNullable(domain.getUri()).orElse("").startsWith(DOMAIN_URI_SCHEME); //$NON-NLS-1$

        if (!isValid && diagnostics != null) {
            // @formatter:off
            BasicDiagnostic basicDiagnostic = new BasicDiagnostic(Diagnostic.ERROR,
                    SIRIUS_WEB_EMF_PACKAGE,
                    0,
                    String.format(DOMAIN_URI_SCHEME_ERROR_MESSAGE, domain.getName()),
                    new Object [] {
                            DomainPackage.Literals.DOMAIN__URI.getName(),
                    });
            // @formatter:on

            diagnostics.add(basicDiagnostic);
        }
        return isValid;
    }

    private boolean nameIsNotBlankValidate(Domain domain, DiagnosticChain diagnostics) {
        boolean isValid = !Optional.ofNullable(domain.getName()).orElse("").isBlank(); //$NON-NLS-1$

        if (!isValid && diagnostics != null) {
         // @formatter:off
            BasicDiagnostic basicDiagnostic = new BasicDiagnostic(Diagnostic.WARNING,
                    SIRIUS_WEB_EMF_PACKAGE,
                    0,
                    DOMAIN_NAME_ERROR_MESSAGE,
                    new Object [] {
                            DomainPackage.Literals.NAMED_ELEMENT__NAME.getName(),
                    });
            // @formatter:on

            diagnostics.add(basicDiagnostic);
        }

        return isValid;
    }

    @Override
    public boolean validate(EObject eObject, DiagnosticChain diagnostics, Map<Object, Object> context) {
        return true;
    }

}
