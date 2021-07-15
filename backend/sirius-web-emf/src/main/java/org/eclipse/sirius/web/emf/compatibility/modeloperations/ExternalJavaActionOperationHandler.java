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
package org.eclipse.sirius.web.emf.compatibility.modeloperations;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.sirius.tools.api.ui.IExternalJavaAction;
import org.eclipse.sirius.viewpoint.description.tool.ExternalJavaAction;
import org.eclipse.sirius.viewpoint.description.tool.ExternalJavaActionParameter;
import org.eclipse.sirius.viewpoint.description.tool.ModelOperation;
import org.eclipse.sirius.web.compat.api.IModelOperationHandler;
import org.eclipse.sirius.web.compat.utils.BeanUtil;
import org.eclipse.sirius.web.emf.compatibility.ExternalJavaActionManager;
import org.eclipse.sirius.web.interpreter.AQLInterpreter;
import org.eclipse.sirius.web.representations.Status;
import org.eclipse.sirius.web.representations.VariableManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handle the {@link ExternalJavaAction} model operation.
 *
 * @author Charles Wu
 */
public class ExternalJavaActionOperationHandler implements IModelOperationHandler {

    private final Logger logger = LoggerFactory.getLogger(MoveElementOperationHandler.class);

    private final AQLInterpreter interpreter;
    private final ChildModelOperationHandler childModelOperationHandler;
    private final ExternalJavaAction externalJavaAction;

    public ExternalJavaActionOperationHandler(AQLInterpreter interpreter, ChildModelOperationHandler childModelOperationHandler, ExternalJavaAction externalJavaAction) {
        this.interpreter = Objects.requireNonNull(interpreter);
        this.childModelOperationHandler = Objects.requireNonNull(childModelOperationHandler);
        this.externalJavaAction = Objects.requireNonNull(externalJavaAction);
    }

    @Override
    public Status handle(Map<String, Object> variables) {
        ExternalJavaActionManager actionManager = BeanUtil.getBean(ExternalJavaActionManager.class);
        IExternalJavaAction action = actionManager.getJavaActionById(externalJavaAction.getId());

        if (action == null) {
            this.logger.warn("Unable to find external java action from id:{}", externalJavaAction.getId()); //$NON-NLS-1$
            return Status.ERROR;
        }
        final Map<String, Object> parameters = new HashMap<>();

        // Evaluate the parameters
        for (final ExternalJavaActionParameter parameter : externalJavaAction.getParameters()) {
            final Optional<Object> value = interpreter.evaluateExpression(variables, parameter.getValue()).asObject();
            value.ifPresent(it -> parameters.put(parameter.getName(), it));
        }

        List<EObject> targets = Collections.singletonList((EObject) variables.get(VariableManager.SELF));

        if (action.canExecute(targets)) {
            action.execute(targets, parameters);
        }

        List<ModelOperation> subModelOperations = this.externalJavaAction.getSubModelOperations();
        return this.childModelOperationHandler.handle(this.interpreter, variables, subModelOperations);
    }

}
