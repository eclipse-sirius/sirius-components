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
package org.eclipse.sirius.web.interpreter;

import java.util.Collection;
import java.util.Collections;

import org.eclipse.acceleo.query.runtime.CrossReferenceProvider;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.util.ECrossReferenceAdapter;

/**
 * {@link CrossReferenceProvider} implementation which simply looks for an existing {@link ECrossReferenceAdapter}
 * already attached to the source {@link EObject}. Returns an empty set of references without any error if no
 * {@link ECrossReferenceAdapter} is found.
 *
 * @author pcdavid
 */
public class SimpleCrossReferenceProvider implements CrossReferenceProvider {
    @Override
    public Collection<Setting> getInverseReferences(EObject self) {
        if (self != null) {
            // @formatter:off
            var xref = self.eAdapters().stream()
                           .filter(ECrossReferenceAdapter.class::isInstance)
                           .map(ECrossReferenceAdapter.class::cast)
                           .findFirst();
            // @formatter:on
            if (xref.isPresent()) {
                return xref.get().getInverseReferences(self);
            }
        }
        return Collections.emptySet();
    }
}
