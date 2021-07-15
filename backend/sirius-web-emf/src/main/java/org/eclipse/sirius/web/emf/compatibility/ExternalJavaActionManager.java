/*******************************************************************************
 * Copyright (c) 2019, 2021 Obeo.
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
package org.eclipse.sirius.web.emf.compatibility;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.eclipse.sirius.tools.api.ui.IExternalJavaAction;
import org.eclipse.sirius.web.compat.api.IExternalJavaActionProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

/**
 * A service to manager all {@link IExternalJavaAction} extensions.
 *
 * @author Charles Wu
 */
@Service
public class ExternalJavaActionManager {

    private final Logger logger = LoggerFactory.getLogger(ExternalJavaActionManager.class);

    private final Map<String, Class<? extends IExternalJavaAction>> javaActionMap;

    public ExternalJavaActionManager(List<IExternalJavaActionProvider> javaActionProvider) {
        javaActionMap = javaActionProvider.stream()
            .map(provider -> provider.getExternalJavaActions().get())
            .map(Map::entrySet)
            .flatMap(Collection::stream)
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    /**
     * Get the {@link IExternalJavaAction} for the given id.
     *
     * @param id the id of the wanted extension.
     * @return the corresponding {@link IExternalJavaAction}.
     */
    @Nullable
    public IExternalJavaAction getJavaActionById(final String id) {
        Class<? extends IExternalJavaAction> clazz = javaActionMap.get(id);

        if (clazz != null) {
            try {
                return clazz.getConstructor().newInstance();
            } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
                logger.warn("Unable to initiate class:{}, reason:{}", clazz.getName(), e.getMessage());
            }
        }
        return null;
    }

}
