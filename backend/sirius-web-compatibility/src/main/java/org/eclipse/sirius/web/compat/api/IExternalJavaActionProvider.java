/*******************************************************************************
 * Copyright (c) 2021 THALES GLOBAL SERVICES.
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
package org.eclipse.sirius.web.compat.api;

import java.util.Map;
import java.util.function.Supplier;

import org.eclipse.sirius.tools.api.ui.IExternalJavaAction;

/**
 * Used to register the {@link IExternalJavaAction}.
 *
 * @author Charles Wu
 */
@FunctionalInterface
public interface IExternalJavaActionProvider {

    Supplier<Map<String, Class<? extends IExternalJavaAction>>> getExternalJavaActions();
}
