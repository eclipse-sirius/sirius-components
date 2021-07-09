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
package org.eclipse.sirius.web.compat.forms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.eclipse.sirius.properties.WidgetDescription;
import org.eclipse.sirius.viewpoint.description.tool.InitialOperation;
import org.eclipse.sirius.viewpoint.description.tool.ModelOperation;
import org.eclipse.sirius.web.compat.api.IIdentifierProvider;
import org.eclipse.sirius.web.compat.api.IModelOperationHandler;
import org.eclipse.sirius.web.compat.api.IModelOperationHandlerSwitchProvider;
import org.eclipse.sirius.web.compat.utils.BooleanValueProvider;
import org.eclipse.sirius.web.compat.utils.StringValueProvider;
import org.eclipse.sirius.web.core.api.IEditingContext;
import org.eclipse.sirius.web.core.api.IObjectService;
import org.eclipse.sirius.web.forms.components.RadioComponent;
import org.eclipse.sirius.web.forms.components.SelectComponent;
import org.eclipse.sirius.web.forms.description.AbstractWidgetDescription;
import org.eclipse.sirius.web.forms.description.CheckboxDescription;
import org.eclipse.sirius.web.forms.description.RadioDescription;
import org.eclipse.sirius.web.forms.description.SelectDescription;
import org.eclipse.sirius.web.forms.description.TextareaDescription;
import org.eclipse.sirius.web.forms.description.TextfieldDescription;
import org.eclipse.sirius.web.interpreter.AQLInterpreter;
import org.eclipse.sirius.web.representations.Status;
import org.eclipse.sirius.web.representations.VariableManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is used to convert a Sirius WidgetDescription to an Sirius Web AbstractWidgetDescription.
 *
 * @author fbarbin
 */
public class WidgetDescriptionConverter {

    private static final String NEW_VALUE = "newValue"; //$NON-NLS-1$

    private final Logger logger = LoggerFactory.getLogger(WidgetDescriptionConverter.class);

    private final AQLInterpreter interpreter;

    private final IObjectService objectService;

    private final IIdentifierProvider identifierProvider;

    private final IModelOperationHandlerSwitchProvider modelOperationHandlerSwitchProvider;

    public WidgetDescriptionConverter(AQLInterpreter interpreter, IObjectService objectService, IIdentifierProvider identifierProvider,
            IModelOperationHandlerSwitchProvider modelOperationHandlerSwitchProvider) {
        this.interpreter = Objects.requireNonNull(interpreter);
        this.objectService = Objects.requireNonNull(objectService);
        this.identifierProvider = Objects.requireNonNull(identifierProvider);
        this.modelOperationHandlerSwitchProvider = Objects.requireNonNull(modelOperationHandlerSwitchProvider);
    }

    public Optional<AbstractWidgetDescription> convert(WidgetDescription controlDescription) {
        Optional<AbstractWidgetDescription> optionalWidgetDescription = Optional.empty();
        if (controlDescription instanceof org.eclipse.sirius.properties.CheckboxDescription) {
            optionalWidgetDescription = Optional.of(this.convertCheckbox((org.eclipse.sirius.properties.CheckboxDescription) controlDescription));
        } else if (controlDescription instanceof org.eclipse.sirius.properties.TextDescription) {
            optionalWidgetDescription = Optional.of(this.convertTextfield((org.eclipse.sirius.properties.TextDescription) controlDescription));
        } else if (controlDescription instanceof org.eclipse.sirius.properties.TextAreaDescription) {
            optionalWidgetDescription = Optional.of(this.convertTextarea((org.eclipse.sirius.properties.TextAreaDescription) controlDescription));
        } else if (controlDescription instanceof org.eclipse.sirius.properties.RadioDescription) {
            optionalWidgetDescription = Optional.of(this.convertRadio((org.eclipse.sirius.properties.RadioDescription) controlDescription));
        } else if (controlDescription instanceof org.eclipse.sirius.properties.SelectDescription) {
            optionalWidgetDescription = Optional.of(this.convertSelect((org.eclipse.sirius.properties.SelectDescription) controlDescription));
        } else {
            this.logger.warn("The provided type {} is not yet handled", controlDescription.getClass().getName()); //$NON-NLS-1$
        }
        return optionalWidgetDescription;
    }

    private TextfieldDescription convertTextfield(org.eclipse.sirius.properties.TextDescription textDescription) {
        String labelExpression = Optional.ofNullable(textDescription.getLabelExpression()).orElse(""); //$NON-NLS-1$
        StringValueProvider labelProvider = new StringValueProvider(this.interpreter, labelExpression);

        String valueExpression = Optional.ofNullable(textDescription.getValueExpression()).orElse(""); //$NON-NLS-1$
        StringValueProvider valueProvider = new StringValueProvider(this.interpreter, valueExpression);

        BiFunction<VariableManager, String, Status> newValueHandler = this.getNewValueHandler(textDescription.getInitialOperation());

        // @formatter:off
        return TextfieldDescription.newTextfieldDescription(this.identifierProvider.getIdentifier(textDescription))
                .idProvider(new WidgetIdProvider())
                .labelProvider(labelProvider)
                .valueProvider(valueProvider)
                .newValueHandler(newValueHandler)
                .diagnosticsProviders((variableManager) -> List.of())
                .kindProvider((object) -> "") //$NON-NLS-1$
                .messageProvider((object) -> "") //$NON-NLS-1$
                .build();
        // @formatter:on

    }

    private TextareaDescription convertTextarea(org.eclipse.sirius.properties.TextAreaDescription textAreaDescription) {
        String labelExpression = Optional.ofNullable(textAreaDescription.getLabelExpression()).orElse(""); //$NON-NLS-1$
        StringValueProvider labelProvider = new StringValueProvider(this.interpreter, labelExpression);

        String valueExpression = Optional.ofNullable(textAreaDescription.getValueExpression()).orElse(""); //$NON-NLS-1$
        StringValueProvider valueProvider = new StringValueProvider(this.interpreter, valueExpression);

        BiFunction<VariableManager, String, Status> newValueHandler = this.getNewValueHandler(textAreaDescription.getInitialOperation());

        // @formatter:off
        return TextareaDescription.newTextareaDescription(this.identifierProvider.getIdentifier(textAreaDescription))
                .idProvider(new WidgetIdProvider())
                .labelProvider(labelProvider)
                .valueProvider(valueProvider)
                .newValueHandler(newValueHandler)
                .diagnosticsProviders((variableManager) -> List.of())
                .kindProvider((object) -> "") //$NON-NLS-1$
                .messageProvider((object) -> "") //$NON-NLS-1$
                .build();
        // @formatter:on

    }

    private BiFunction<VariableManager, String, Status> getNewValueHandler(InitialOperation initialOperation) {
        BiFunction<VariableManager, String, Status> newValueHandler = (variableManager, newValue) -> {
            VariableManager childVariableManager = variableManager.createChild();
            childVariableManager.put(NEW_VALUE, newValue);

            ModelOperation modelOperation = initialOperation.getFirstModelOperations();

            var modelOperationHandlerSwitch = this.modelOperationHandlerSwitchProvider.getModelOperationHandlerSwitch(this.interpreter);
            Optional<IModelOperationHandler> optionalModelOperationHandler = modelOperationHandlerSwitch.apply(modelOperation);
            return optionalModelOperationHandler.map(handler -> {
                return handler.handle(childVariableManager.getVariables());
            }).orElse(Status.ERROR);
        };
        return newValueHandler;
    }

    private RadioDescription convertRadio(org.eclipse.sirius.properties.RadioDescription radioDescription) {
        String labelExpression = Optional.ofNullable(radioDescription.getLabelExpression()).orElse(""); //$NON-NLS-1$
        StringValueProvider labelProvider = new StringValueProvider(this.interpreter, labelExpression);

        Function<VariableManager, String> optionIdProvider = variableManager -> {
            Object candidate = variableManager.getVariables().get(RadioComponent.CANDIDATE_VARIABLE);
            return this.objectService.getId(candidate);
        };

        Function<VariableManager, Boolean> optionSelectedProvider = variableManager -> {
            Optional<Object> optionalResult = this.interpreter.evaluateExpression(variableManager.getVariables(), radioDescription.getValueExpression()).asObject();
            Object candidate = variableManager.getVariables().get(RadioComponent.CANDIDATE_VARIABLE);

            return optionalResult.map(candidate::equals).orElse(Boolean.FALSE);
        };

        Function<VariableManager, List<Object>> optionsProvider = variableManager -> {
            Optional<List<Object>> optional = this.interpreter.evaluateExpression(variableManager.getVariables(), radioDescription.getCandidatesExpression()).asObjects();
            return optional.orElse(Collections.emptyList());
        };

        String candidateDisplayExpression = Optional.ofNullable(radioDescription.getCandidateDisplayExpression()).orElse(""); //$NON-NLS-1$
        StringValueProvider optionLabelProvider = new StringValueProvider(this.interpreter, candidateDisplayExpression);

        BiFunction<VariableManager, String, Status> newValueHandler = (variableManager, newValue) -> {
            VariableManager childVariableManager = variableManager.createChild();
            var optionalEditingContext = variableManager.get(IEditingContext.EDITING_CONTEXT, IEditingContext.class);

            Optional<Object> optionalObject = optionalEditingContext.flatMap(context -> this.objectService.getObject(context, newValue));
            if (optionalObject.isPresent()) {
                childVariableManager.put(NEW_VALUE, optionalObject.get());
            } else {
                childVariableManager.put(NEW_VALUE, newValue);
            }

            InitialOperation initialOperation = radioDescription.getInitialOperation();
            ModelOperation modelOperation = initialOperation.getFirstModelOperations();

            var modelOperationHandlerSwitch = this.modelOperationHandlerSwitchProvider.getModelOperationHandlerSwitch(this.interpreter);
            Optional<IModelOperationHandler> optionalModelOperationHandler = modelOperationHandlerSwitch.apply(modelOperation);
            return optionalModelOperationHandler.map(handler -> {
                return handler.handle(childVariableManager.getVariables());
            }).orElse(Status.ERROR);
        };

        // @formatter:off
        return RadioDescription.newRadioDescription(this.identifierProvider.getIdentifier(radioDescription))
                .idProvider(new WidgetIdProvider())
                .labelProvider(labelProvider)
                .optionIdProvider(optionIdProvider)
                .optionLabelProvider(optionLabelProvider)
                .optionSelectedProvider(optionSelectedProvider)
                .optionsProvider(optionsProvider)
                .newValueHandler(newValueHandler)
                .diagnosticsProviders((variableManager) -> List.of())
                .kindProvider((object) -> "") //$NON-NLS-1$
                .messageProvider((object) -> "") //$NON-NLS-1$
                .build();
        // @formatter:on

    }

    private SelectDescription convertSelect(org.eclipse.sirius.properties.SelectDescription selectDescription) {
        // @formatter:off
        StringValueProvider labelProvider = new StringValueProvider(this.interpreter, selectDescription.getLabelExpression());
        Function<VariableManager, String> valueProvider = variableManager -> {
            String valueExpression = selectDescription.getValueExpression();
            return this.interpreter.evaluateExpression(variableManager.getVariables(), valueExpression).asObject().map(this.objectService::getId).orElse(null);
        };
        // @formatter:on

        Function<VariableManager, List<Object>> optionsProvider = (variableManager) -> {
            String candidateExpression = selectDescription.getCandidatesExpression();
            return this.interpreter.evaluateExpression(variableManager.getVariables(), candidateExpression).asObjects().orElse(new ArrayList<>());
        };

        String candidateDisplayExpression = Optional.ofNullable(selectDescription.getCandidateDisplayExpression()).orElse(""); //$NON-NLS-1$
        StringValueProvider optionLabelProvider = new StringValueProvider(this.interpreter, candidateDisplayExpression);

        Function<VariableManager, String> optionIdProvider = variableManager -> {
            Object candidate = variableManager.getVariables().get(SelectComponent.CANDIDATE_VARIABLE);
            return this.objectService.getId(candidate);
        };

        BiFunction<VariableManager, String, Status> newValueHandler = (variableManager, newValue) -> {
            Map<String, Object> variables = variableManager.getVariables();
            variables.put(NEW_VALUE, newValue);

            InitialOperation initialOperation = selectDescription.getInitialOperation();
            ModelOperation modelOperation = initialOperation.getFirstModelOperations();

            var modelOperationHandlerSwitch = this.modelOperationHandlerSwitchProvider.getModelOperationHandlerSwitch(this.interpreter);
            Optional<IModelOperationHandler> optionalModelOperationHandler = modelOperationHandlerSwitch.apply(modelOperation);
            return optionalModelOperationHandler.map(handler -> {
                return handler.handle(variables);
            }).orElse(Status.ERROR);
        };

        // @formatter:off
        return SelectDescription.newSelectDescription(this.identifierProvider.getIdentifier(selectDescription))
                .idProvider(new WidgetIdProvider())
                .labelProvider(labelProvider)
                .valueProvider(valueProvider)
                .optionsProvider(optionsProvider)
                .optionIdProvider(optionIdProvider)
                .optionLabelProvider(optionLabelProvider)
                .newValueHandler(newValueHandler)
                .diagnosticsProviders((variableManager) -> List.of())
                .kindProvider((object) -> "") //$NON-NLS-1$
                .messageProvider((object) -> "") //$NON-NLS-1$
                .build();
        // @formatter:on

    }

    private CheckboxDescription convertCheckbox(org.eclipse.sirius.properties.CheckboxDescription checkboxDescription) {
        StringValueProvider labelProvider = new StringValueProvider(this.interpreter, checkboxDescription.getLabelExpression());

        BiFunction<VariableManager, Boolean, Status> newValueHandler = (variableManager, newValue) -> {
            Map<String, Object> variables = variableManager.getVariables();
            variables.put(NEW_VALUE, newValue);

            InitialOperation initialOperation = checkboxDescription.getInitialOperation();
            ModelOperation modelOperation = initialOperation.getFirstModelOperations();

            var modelOperationHandlerSwitch = this.modelOperationHandlerSwitchProvider.getModelOperationHandlerSwitch(this.interpreter);
            Optional<IModelOperationHandler> optionalModelOperationHandler = modelOperationHandlerSwitch.apply(modelOperation);
            return optionalModelOperationHandler.map(handler -> {
                return handler.handle(variables);
            }).orElse(Status.ERROR);
        };

        String valueExpression = Optional.ofNullable(checkboxDescription.getValueExpression()).orElse(""); //$NON-NLS-1$
        Function<VariableManager, Boolean> valueProvider = new BooleanValueProvider(this.interpreter, valueExpression);

        //@formatter:off
        return CheckboxDescription.newCheckboxDescription(this.identifierProvider.getIdentifier(checkboxDescription))
                .idProvider(new WidgetIdProvider())
                .labelProvider(labelProvider)
                .valueProvider(valueProvider)
                .newValueHandler(newValueHandler)
                .diagnosticsProviders((variableManager) -> List.of())
                .kindProvider((object) -> "") //$NON-NLS-1$
                .messageProvider((object) -> "") //$NON-NLS-1$
                .build();
        // @formatter:on
    }
}
