/*******************************************************************************
 * Copyright (c) 2019, 2020 Obeo.
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

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import org.eclipse.acceleo.query.runtime.EvaluationResult;
import org.eclipse.acceleo.query.runtime.IQueryBuilderEngine;
import org.eclipse.acceleo.query.runtime.IQueryBuilderEngine.AstResult;
import org.eclipse.acceleo.query.runtime.IQueryEnvironment;
import org.eclipse.acceleo.query.runtime.IQueryEvaluationEngine;
import org.eclipse.acceleo.query.runtime.IService;
import org.eclipse.acceleo.query.runtime.Query;
import org.eclipse.acceleo.query.runtime.QueryEvaluation;
import org.eclipse.acceleo.query.runtime.QueryParsing;
import org.eclipse.acceleo.query.runtime.ServiceUtils;
import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.impl.EStringToStringMapEntryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An AQL interpreter used for tests.
 *
 * @author sbegaudeau
 */
public class AQLInterpreter {

    /**
     * The prefix used by AQL expressions.
     */
    private static final String AQL_PREFIX = "aql:"; //$NON-NLS-1$

    private final Logger logger = LoggerFactory.getLogger(AQLInterpreter.class);

    /**
     * The cache of the expressions parsed.
     */
    private LoadingCache<String, AstResult> parsedExpressions;

    /**
     * The query environment.
     */
    private IQueryEnvironment queryEnvironment;

    /**
     * The constructor.
     *
     * @param classes
     *            classes for java service that can called by AQLInterpreter
     * @param ePackages
     *            Additional meta-models. A typical use case will be to register semantic meta-models so that reference
     *            to classes, such as <semanticMM>::<AClass>, can be interpreted.
     */
    public AQLInterpreter(List<Class<?>> classes, List<EPackage> ePackages) {
        this.queryEnvironment = Query.newEnvironmentWithDefaultServices(new SimpleCrossReferenceProvider());
        this.queryEnvironment.registerEPackage(EcorePackage.eINSTANCE);
        this.queryEnvironment.registerCustomClassMapping(EcorePackage.eINSTANCE.getEStringToStringMapEntry(), EStringToStringMapEntryImpl.class);

        for (Class<?> aClass : classes) {
            Set<IService> services = ServiceUtils.getServices(this.queryEnvironment, aClass);
            ServiceUtils.registerServices(this.queryEnvironment, services);
        }

        ePackages.stream().filter(this::isValidEPackage).forEach(this.queryEnvironment::registerEPackage);

        this.initExpressionsCache();
    }

    private boolean isValidEPackage(EPackage ePackage) {
        return ePackage != null && ePackage.getName() != null && ePackage.getNsURI() != null;
    }

    /**
     * Initializes the cache of the expressions.
     */
    private void initExpressionsCache() {
        IQueryBuilderEngine builder = QueryParsing.newBuilder(this.queryEnvironment);
        int maxCacheSize = 500;

        this.parsedExpressions = CacheBuilder.newBuilder().maximumSize(maxCacheSize).build(new CacheLoader<String, AstResult>() {
            @Override
            public AstResult load(String key) throws Exception {
                return builder.build(key);
            }

        });
    }

    public Result evaluateExpression(Map<String, Object> variables, String expressionBody) {
        String expression = new ExpressionConverter().convertExpression(expressionBody);
        if (expression.startsWith(AQL_PREFIX)) {
            expression = expression.substring(AQL_PREFIX.length());
        }

        try {
            AstResult build = this.parsedExpressions.get(expression);
            IQueryEvaluationEngine evaluationEngine = QueryEvaluation.newEngine(this.queryEnvironment);
            EvaluationResult evalResult = evaluationEngine.eval(build, variables);

            BasicDiagnostic diagnostic = new BasicDiagnostic();
            if (Diagnostic.OK != build.getDiagnostic().getSeverity()) {
                diagnostic.merge(build.getDiagnostic());
            }
            if (Diagnostic.OK != evalResult.getDiagnostic().getSeverity()) {
                diagnostic.merge(evalResult.getDiagnostic());
            }

            this.log(expressionBody, diagnostic);

            return new Result(Optional.ofNullable(evalResult.getResult()), Status.getStatus(diagnostic.getSeverity()));
        } catch (ExecutionException exception) {
            this.logger.error(exception.getMessage(), exception);
        }
        return new Result(Optional.empty(), Status.ERROR);
    }

    private void log(String expression, Diagnostic diagnostic) {
        if (diagnostic.getMessage() != null) {
            if (Diagnostic.INFO == diagnostic.getSeverity()) {
                this.logger.info("An info has occurred with the expression '{}': {}", expression, diagnostic.getMessage()); //$NON-NLS-1$
            } else if (Diagnostic.WARNING == diagnostic.getSeverity()) {
                this.logger.warn("A warning has occurred with the expression '{}': {}", expression, diagnostic.getMessage()); //$NON-NLS-1$
            } else if (Diagnostic.ERROR == diagnostic.getSeverity() || Diagnostic.CANCEL == diagnostic.getSeverity()) {
                this.logger.error("An error has occurred with the expression '{}': {}", expression, diagnostic.getMessage()); //$NON-NLS-1$
            }
        }

        diagnostic.getChildren().forEach(childDiagnostic -> this.log(expression, childDiagnostic));
    }
}
