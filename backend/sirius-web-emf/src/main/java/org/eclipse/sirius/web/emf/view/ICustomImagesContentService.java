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
package org.eclipse.sirius.web.emf.view;

import java.util.Optional;
import java.util.UUID;

/**
 * Service to retrieve the actual content of a custom image.
 *
 * @author pcdavid
 */
public interface ICustomImagesContentService {

    Optional<byte[]> getImageContentById(UUID id);

    Optional<String> getImageContentTypeById(UUID id);

    /**
     * Implementation which does nothing, used for mocks in unit tests.
     *
     * @author pcdavid
     */
    class NoOp implements ICustomImagesContentService {

        @Override
        public Optional<byte[]> getImageContentById(UUID id) {
            return Optional.empty();
        }

        @Override
        public Optional<String> getImageContentTypeById(UUID id) {
            return Optional.empty();
        }

    }

}