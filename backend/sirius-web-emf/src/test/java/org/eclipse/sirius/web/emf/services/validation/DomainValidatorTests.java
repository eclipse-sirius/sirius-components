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

import java.util.Map;

import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.util.Diagnostician;
import org.eclipse.sirius.web.domain.Domain;
import org.eclipse.sirius.web.domain.DomainFactory;
import org.eclipse.sirius.web.domain.DomainPackage;
import org.eclipse.sirius.web.emf.domain.DomainValidator;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link DomainValidator}.
 *
 * @author gcoutable
 */
public class DomainValidatorTests {

    @Test
    public void testDomainShouldBeValid() {
        Map<Object, Object> defaultContext = Diagnostician.INSTANCE.createDefaultContext();
        Domain domain = DomainFactory.eINSTANCE.createDomain();
        domain.setName("Family"); //$NON-NLS-1$
        domain.setUri("domain://Family"); //$NON-NLS-1$

        BasicDiagnostic diagnosticChain = new BasicDiagnostic(Diagnostic.OK, null, 0, null, null);

        boolean validationResult = new DomainValidator().validate(domain.eClass(), domain, diagnosticChain, defaultContext);
        assertThat(validationResult).isTrue();

        assertThat(diagnosticChain).isEqualTo(new BasicDiagnostic(Diagnostic.OK, null, 0, null, null));
    }

    @Test
    public void testDomainInvalidURI() {
        Map<Object, Object> defaultContext = Diagnostician.INSTANCE.createDefaultContext();
        Domain domain = DomainFactory.eINSTANCE.createDomain();
        domain.setName("Family"); //$NON-NLS-1$
        domain.setUri(""); //$NON-NLS-1$

        BasicDiagnostic diagnosticChain = new BasicDiagnostic(Diagnostic.OK, null, 0, null, null);

        boolean validationResult = new DomainValidator().validate(domain.eClass(), domain, diagnosticChain, defaultContext);
        assertThat(validationResult).isFalse();

        BasicDiagnostic expected = new BasicDiagnostic(Diagnostic.ERROR, null, 0, null, null);
        // @formatter:off
        expected.add(new BasicDiagnostic(Diagnostic.ERROR,
                "org.eclipse.sirius.web.emf", //$NON-NLS-1$
                0,
                String.format("The domain %1$s uri's does not start with \"domain://\".", domain.getName()), //$NON-NLS-1$
                new Object [] {
                        domain,
                        DomainPackage.Literals.DOMAIN__URI,

        }));
        // @formatter:on

        assertThat(diagnosticChain).isEqualTo(expected);
    }

    @Test
    public void testDomainInvalidName() {
        Map<Object, Object> defaultContext = Diagnostician.INSTANCE.createDefaultContext();
        Domain domain = DomainFactory.eINSTANCE.createDomain();
        domain.setName(""); //$NON-NLS-1$
        domain.setUri("domain://Family"); //$NON-NLS-1$

        BasicDiagnostic diagnosticChain = new BasicDiagnostic(Diagnostic.OK, null, 0, null, null);

        boolean validationResult = new DomainValidator().validate(domain.eClass(), domain, diagnosticChain, defaultContext);
        assertThat(validationResult).isFalse();

        BasicDiagnostic expected = new BasicDiagnostic(Diagnostic.WARNING, null, 0, null, null);
        // @formatter:off
        expected.add(new BasicDiagnostic(Diagnostic.WARNING,
                "org.eclipse.sirius.web.emf", //$NON-NLS-1$
                0,
                "The domain name should not be empty.", //$NON-NLS-1$
                new Object [] {
                        domain,
                        DomainPackage.Literals.NAMED_ELEMENT__NAME,
        }));
        // @formatter:on

        assertThat(diagnosticChain).isEqualTo(expected);
    }

}
