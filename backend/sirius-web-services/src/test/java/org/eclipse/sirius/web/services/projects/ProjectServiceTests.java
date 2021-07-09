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
package org.eclipse.sirius.web.services.projects;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.eclipse.sirius.web.core.api.ErrorPayload;
import org.eclipse.sirius.web.core.api.IPayload;
import org.eclipse.sirius.web.persistence.entities.AccountEntity;
import org.eclipse.sirius.web.persistence.entities.ProjectEntity;
import org.eclipse.sirius.web.persistence.repositories.IAccountRepository;
import org.eclipse.sirius.web.persistence.repositories.IProjectRepository;
import org.eclipse.sirius.web.services.api.projects.CreateProjectInput;
import org.eclipse.sirius.web.services.api.projects.CreateProjectSuccessPayload;
import org.eclipse.sirius.web.services.api.projects.Visibility;
import org.eclipse.sirius.web.services.messages.IServicesMessageService;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

/**
 * Unit tests of the project service.
 *
 * @author sbegaudeau
 */
public class ProjectServiceTests {

    private static final String NEW_PROJECT = "New Project"; //$NON-NLS-1$

    private static final String OWNER_NAME = "jdoe"; //$NON-NLS-1$

    private IServicesMessageService noOpMessageService = new NoOpServicesMessageService();

    private IProjectRepository noOpProjectRepository = new NoOpProjectRepository() {
        @Override
        public <S extends ProjectEntity> S save(S entity) {
            entity.setId(UUID.randomUUID());
            return entity;
        }
    };

    private IAccountRepository fakeAccountRepository = new NoOpAccountRepository() {
        @Override
        public Optional<AccountEntity> findByUsername(String userName) {
            if (Objects.equals(OWNER_NAME, userName)) {
                AccountEntity result = new AccountEntity();
                result.setId(UUID.randomUUID());
                result.setUsername(OWNER_NAME);
                return Optional.of(result);
            } else {
                return Optional.empty();
            }
        }
    };

    private ProjectService projectService = new ProjectService(this.noOpMessageService, this.noOpProjectRepository, this.fakeAccountRepository);

    @Test
    public void testProjectCreationWithInvalidName() {
        Object principal = new User(OWNER_NAME, "", List.of()); //$NON-NLS-1$
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(principal, new Object()));

        CreateProjectInput input = new CreateProjectInput(UUID.randomUUID(), "", Visibility.PRIVATE); //$NON-NLS-1$
        IPayload payload = this.projectService.createProject(input);
        assertThat(payload).isInstanceOf(ErrorPayload.class);

        SecurityContextHolder.getContext().setAuthentication(null);
    }

    @Test
    public void testProjectCreationSuccess() {
        Object principal = new User(OWNER_NAME, "", List.of()); //$NON-NLS-1$
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(principal, new Object()));

        CreateProjectInput input = new CreateProjectInput(UUID.randomUUID(), NEW_PROJECT, Visibility.PRIVATE);
        IPayload payload = this.projectService.createProject(input);
        assertThat(payload).isInstanceOf(CreateProjectSuccessPayload.class);

        SecurityContextHolder.getContext().setAuthentication(null);
    }

    @Test
    public void testCreateProjectsWithSameName() {
        Object principal = new User(OWNER_NAME, "", List.of()); //$NON-NLS-1$
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(principal, new Object()));

        CreateProjectInput firstInput = new CreateProjectInput(UUID.randomUUID(), NEW_PROJECT, Visibility.PRIVATE);
        IPayload payload = this.projectService.createProject(firstInput);
        assertThat(payload).isInstanceOf(CreateProjectSuccessPayload.class);

        CreateProjectInput secondInput = new CreateProjectInput(UUID.randomUUID(), NEW_PROJECT, Visibility.PRIVATE);
        IPayload secondPayload = this.projectService.createProject(secondInput);
        assertThat(secondPayload).isInstanceOf(CreateProjectSuccessPayload.class);

        SecurityContextHolder.getContext().setAuthentication(null);
    }

}
