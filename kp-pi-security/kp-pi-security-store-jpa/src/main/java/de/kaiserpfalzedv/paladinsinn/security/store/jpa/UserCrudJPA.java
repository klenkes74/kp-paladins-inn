/*
 * Copyright 2017 Kaiserpfalz EDV-Service, Roland T. Lichti
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.kaiserpfalzedv.paladinsinn.security.store.jpa;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Alternative;
import javax.inject.Inject;

import de.kaiserpfalzedv.paladinsinn.commons.api.paging.Page;
import de.kaiserpfalzedv.paladinsinn.commons.api.paging.PageRequest;
import de.kaiserpfalzedv.paladinsinn.commons.api.persistence.DuplicateEntityException;
import de.kaiserpfalzedv.paladinsinn.commons.api.service.SingleTenant;
import de.kaiserpfalzedv.paladinsinn.commons.api.service.WorkerService;
import de.kaiserpfalzedv.paladinsinn.commons.api.tenant.model.DefaultTenant;
import de.kaiserpfalzedv.paladinsinn.commons.api.tenant.model.Tenant;
import de.kaiserpfalzedv.paladinsinn.security.api.model.User;
import de.kaiserpfalzedv.paladinsinn.security.api.store.UserCrudService;
import de.kaiserpfalzedv.paladinsinn.security.store.jpa.model.UserJPA;

/**
 * The non-multitenant version of the JPA CRUD implementation just delegates to the multitenant
 * {@link UserMultitenantCrudJPA} with the static default tenant as tenant parameter.
 *
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2017-03-26
 */
@Alternative
@RequestScoped
@SingleTenant
@WorkerService
public class UserCrudJPA implements UserCrudService {
    private static final Tenant DEFAULT_TENANT = DefaultTenant.INSTANCE;

    @Inject
    private UserMultitenantCrudJPA multitenantService;

    public UserCrudJPA() {}

    public UserCrudJPA(
            final UserMultitenantCrudJPA service
    ) {
        this.multitenantService = service;
    }

    @Override
    public UserJPA create(final User data) throws DuplicateEntityException {
        return multitenantService.create(DEFAULT_TENANT, data);
    }

    @Override
    public Optional<? extends User> retrieve(final UUID uniqueId) {
        return multitenantService.retrieve(DEFAULT_TENANT, uniqueId);
    }

    @Override
    public Optional<? extends User> retrieve(final String uniqueName) {
        return multitenantService.retrieve(DEFAULT_TENANT, uniqueName);
    }

    @Override
    public Set<? extends User> retrieve() {
        return multitenantService.retrieve(DEFAULT_TENANT);
    }

    @Override
    public Page<? extends User> retrieve(final PageRequest pageRequest) {
        return multitenantService.retrieve(DEFAULT_TENANT, pageRequest);
    }

    @Override
    public UserJPA update(final User data) throws DuplicateEntityException {
        return multitenantService.update(DEFAULT_TENANT, data);
    }

    @Override
    public void delete(final User data) {
        multitenantService.delete(DEFAULT_TENANT, data);
    }

    @Override
    public void delete(final UUID uniqueId) {
        multitenantService.delete(DEFAULT_TENANT, uniqueId);
    }

    @Override
    public void delete(final String uniqueName) {
        multitenantService.delete(DEFAULT_TENANT, uniqueName);
    }
}
