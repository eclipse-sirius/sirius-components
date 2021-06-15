/*******************************************************************************
 * Copyright (c) 2019, 2021 Obeo and others.
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
package org.eclipse.sirius.web.diagrams.services.api;

import java.util.Optional;
import java.util.UUID;

import org.eclipse.sirius.web.diagrams.Diagram;

/**
 * Interface used to manipulate diagrams.
 *
 * @author sbegaudeau
 */
public interface IDiagramService {

    Optional<Diagram> findById(UUID diagramId);

}