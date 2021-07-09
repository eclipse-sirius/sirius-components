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

import java.util.Map;

import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.util.Diagnostician;
import org.eclipse.sirius.web.domain.Domain;
import org.eclipse.sirius.web.domain.DomainFactory;
import org.eclipse.sirius.web.domain.DomainPackage;
import org.eclipse.sirius.web.emf.domain.DomainValidator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link DomainValidator}.
 *
 * @author gcoutable
 */
public class DomainValidatorTests {

    private DomainValidator domainValidator;

    private DomainFactory domainFactory;

    @BeforeEach
    public void setup() {
        this.domainValidator = new DomainValidator();
        this.domainFactory = DomainFactory.eINSTANCE;
    }

    @AfterEach
    public void tearDown() {
        this.domainValidator = null;
        this.domainFactory = null;
    }

    @Test
    public void testDomainShouldBeValid() {
        BasicDiagnostic basicDiagnostic = ValidatorTestUtils.getNewBasicDiagnostic();
        Map<Object, Object> defaultContext = Diagnostician.INSTANCE.createDefaultContext();
        Domain domain = this.domainFactory.createDomain();
        domain.setName("Family"); //$NON-NLS-1$
        domain.setUri("domain://Family"); //$NON-NLS-1$

        boolean validatedDomain = this.domainValidator.validate(domain.eClass(), domain, basicDiagnostic, defaultContext);
        assertThat(validatedDomain).isTrue();
        ValidatorTestUtils.diagnosticAssert(basicDiagnostic, ValidatorTestUtils.getNewBasicDiagnostic());
    }

    @Test
    public void testDomainInvalidURI() {
        BasicDiagnostic basicDiagnostic = ValidatorTestUtils.getNewBasicDiagnostic();
        Map<Object, Object> defaultContext = Diagnostician.INSTANCE.createDefaultContext();
        Domain domain = this.domainFactory.createDomain();
        domain.setName("Family"); //$NON-NLS-1$
        domain.setUri(""); //$NON-NLS-1$

        BasicDiagnostic expected = ValidatorTestUtils.getNewBasicDiagnostic(Diagnostic.ERROR);
        // @formatter:off
        expected.add(new BasicDiagnostic(Diagnostic.ERROR,
                "org.eclipse.sirius.web.emf", //$NON-NLS-1$
                0,
                String.format("The domain %1$s uri's does not start with \"domain://\".", domain.getName()), //$NON-NLS-1$
                new Object [] {
                        DomainPackage.Literals.DOMAIN__URI.getName(),
        }));
        // @formatter:on

        boolean validatedDomain = this.domainValidator.validate(domain.eClass(), domain, basicDiagnostic, defaultContext);
        assertThat(validatedDomain).isFalse();
        ValidatorTestUtils.diagnosticAssert(basicDiagnostic, expected);
    }

    @Test
    public void testDomainInvalidName() {
        BasicDiagnostic basicDiagnostic = ValidatorTestUtils.getNewBasicDiagnostic();
        Map<Object, Object> defaultContext = Diagnostician.INSTANCE.createDefaultContext();
        Domain domain = this.domainFactory.createDomain();
        domain.setName(""); //$NON-NLS-1$
        domain.setUri("domain://Family"); //$NON-NLS-1$

        BasicDiagnostic expected = ValidatorTestUtils.getNewBasicDiagnostic(Diagnostic.WARNING);
        // @formatter:off
        expected.add(new BasicDiagnostic(Diagnostic.WARNING,
                "org.eclipse.sirius.web.emf", //$NON-NLS-1$
                0,
                "The domain name should not be empty.", //$NON-NLS-1$
                new Object [] {
                        DomainPackage.Literals.NAMED_ELEMENT__NAME.getName(),
        }));
        // @formatter:on

        boolean validatedDomain = this.domainValidator.validate(domain.eClass(), domain, basicDiagnostic, defaultContext);
        assertThat(validatedDomain).isFalse();
        ValidatorTestUtils.diagnosticAssert(basicDiagnostic, expected);
    }

}
