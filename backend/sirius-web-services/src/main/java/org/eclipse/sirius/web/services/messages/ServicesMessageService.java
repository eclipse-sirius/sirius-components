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
package org.eclipse.sirius.web.services.messages;

import java.util.Objects;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Service;

/**
 * Implementation of the services messages.
 *
 * @author sbegaudeau
 */
@Service
public class ServicesMessageService implements IServicesMessageService {

    private final MessageSourceAccessor messageSourceAccessor;

    public ServicesMessageService(@Qualifier("servicesMessageSourceAccessor") MessageSourceAccessor messageSourceAccessor) {
        this.messageSourceAccessor = Objects.requireNonNull(messageSourceAccessor);
    }

    @Override
    public String invalidInput(String expectedInputTypeName, String receivedInputTypeName) {
        return this.messageSourceAccessor.getMessage("INVALID_INPUT", new Object[] { expectedInputTypeName, receivedInputTypeName }); //$NON-NLS-1$
    }

    @Override
    public String invalidProjectName() {
        return this.messageSourceAccessor.getMessage("INVALID_PROJECT_NAME"); //$NON-NLS-1$
    }

    @Override
    public String projectNotFound() {
        return this.messageSourceAccessor.getMessage("PROJECT_NOT_FOUND"); //$NON-NLS-1$
    }

    @Override
    public String unexpectedError() {
        return this.messageSourceAccessor.getMessage("UNEXPECTED_ERROR"); //$NON-NLS-1$
    }

    @Override
    public String invalidDocumentName(String name) {
        return this.messageSourceAccessor.getMessage("INVALID_DOCUMENT_NAME", new Object[] { name }); //$NON-NLS-1$
    }

    @Override
    public String stereotypeDescriptionNotFound(UUID stereotypeDescriptionId) {
        return this.messageSourceAccessor.getMessage("STEREOTYPE_DESCRIPTION_NOT_FOUND", new Object[] { stereotypeDescriptionId }); //$NON-NLS-1$
    }
}
