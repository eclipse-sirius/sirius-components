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

import java.security.Principal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.eclipse.sirius.web.core.api.ErrorPayload;
import org.eclipse.sirius.web.core.api.IPayload;
import org.eclipse.sirius.web.persistence.entities.AccountEntity;
import org.eclipse.sirius.web.persistence.entities.ProjectEntity;
import org.eclipse.sirius.web.persistence.entities.VisibilityEntity;
import org.eclipse.sirius.web.persistence.repositories.IAccountRepository;
import org.eclipse.sirius.web.persistence.repositories.IProjectRepository;
import org.eclipse.sirius.web.services.api.projects.CreateProjectInput;
import org.eclipse.sirius.web.services.api.projects.CreateProjectSuccessPayload;
import org.eclipse.sirius.web.services.api.projects.IProjectService;
import org.eclipse.sirius.web.services.api.projects.Project;
import org.eclipse.sirius.web.services.api.projects.Visibility;
import org.eclipse.sirius.web.services.messages.IServicesMessageService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * Service used to manipulate projects.
 *
 * @author sbegaudeau
 * @author pcdavid
 */
@Service
public class ProjectService implements IProjectService {

    private final IServicesMessageService messageService;

    private final IProjectRepository projectRepository;

    private final IAccountRepository accountRepository;

    private final ProjectMapper projectMapper;

    public ProjectService(IServicesMessageService messageService, IProjectRepository projectRepository, IAccountRepository accountRepository) {
        this.messageService = Objects.requireNonNull(messageService);
        this.projectRepository = Objects.requireNonNull(projectRepository);
        this.accountRepository = Objects.requireNonNull(accountRepository);
        this.projectMapper = new ProjectMapper();
    }

    private String getCurrentUserName() {
        Authentication principal = SecurityContextHolder.getContext().getAuthentication();
        return Optional.ofNullable(principal).map(Principal::getName).orElse(""); //$NON-NLS-1$
    }

    @Override
    public Optional<Project> getProject(UUID projectId) {
        return this.projectRepository.findByIdIfVisibleBy(projectId, this.getCurrentUserName()).map(this.projectMapper::toDTO);
    }

    @Override
    public List<Project> getProjects() {
        // @formatter:off
        return this.projectRepository.findAllVisibleBy(this.getCurrentUserName()).stream()
                .map(this.projectMapper::toDTO)
                .collect(Collectors.toUnmodifiableList());
        // @formatter:on
    }

    @Override
    public IPayload createProject(CreateProjectInput input) {
        IPayload payload = null;
        String name = input.getName().trim();

        if (!this.isValidProjectName(name)) {
            payload = new ErrorPayload(input.getId(), this.messageService.invalidProjectName());
        } else {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            var optionalOwner = this.accountRepository.findByUsername(authentication.getName());
            if (!optionalOwner.isEmpty()) {
                ProjectEntity projectEntity = this.createProjectEntity(name, optionalOwner.get(), input.getVisibility());
                projectEntity = this.projectRepository.save(projectEntity);

                Project project = this.projectMapper.toDTO(projectEntity);
                payload = new CreateProjectSuccessPayload(input.getId(), project);
            }
        }
        return payload;
    }

    private ProjectEntity createProjectEntity(String projectName, AccountEntity owner, Visibility visibility) {
        ProjectEntity projectEntity = new ProjectEntity();
        projectEntity.setName(projectName);
        projectEntity.setOwner(owner);
        if (visibility == Visibility.PUBLIC) {
            projectEntity.setVisibility(VisibilityEntity.PUBLIC);
        } else {
            projectEntity.setVisibility(VisibilityEntity.PRIVATE);
        }
        return projectEntity;
    }

    private boolean isValidProjectName(String name) {
        return 3 <= name.length() && name.length() <= 20;
    }

    @Override
    public void delete(UUID projectId) {
        if (this.projectRepository.existsByIdAndIsVisibleBy(projectId, this.getCurrentUserName())) {
            this.projectRepository.deleteById(projectId);
        }
    }

    @Override
    public Optional<Project> renameProject(UUID projectId, String newName) {
        Optional<ProjectEntity> optionalProjectEntity = this.projectRepository.findByIdIfVisibleBy(projectId, this.getCurrentUserName());
        if (optionalProjectEntity.isPresent()) {
            ProjectEntity projectEntity = optionalProjectEntity.get();
            projectEntity.setName(newName);
            return Optional.of(this.projectRepository.save(projectEntity)).map(this.projectMapper::toDTO);
        }
        return Optional.empty();
    }

}
