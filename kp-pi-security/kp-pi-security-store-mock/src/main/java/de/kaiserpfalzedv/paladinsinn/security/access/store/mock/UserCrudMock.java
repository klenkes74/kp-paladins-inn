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

package de.kaiserpfalzedv.paladinsinn.security.access.store.mock;

import de.kaiserpfalzedv.paladinsinn.commons.paging.Page;
import de.kaiserpfalzedv.paladinsinn.commons.paging.PageRequest;
import de.kaiserpfalzedv.paladinsinn.commons.service.MockService;
import de.kaiserpfalzedv.paladinsinn.security.access.model.User;
import de.kaiserpfalzedv.paladinsinn.security.access.store.DuplicateEntityException;
import de.kaiserpfalzedv.paladinsinn.security.access.store.TenantUserCrudService;
import de.kaiserpfalzedv.paladinsinn.security.access.store.UserCrudService;
import de.kaiserpfalzedv.paladinsinn.security.tenant.model.Tenant;
import de.kaiserpfalzedv.paladinsinn.security.tenant.model.impl.DefaultTenant;

import javax.inject.Inject;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * The tenant-less version of the mock service uses the multi-tenant {@link TenantUserCrudMock} with the
 * {@link DefaultTenant} as tenant.
 *
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2017-03-19
 */
@MockService
public class UserCrudMock implements UserCrudService {
    private final Tenant defaultTenant;
    private final TenantUserCrudService service;


    /**
     * @param defaultTenant         The tenant to use when calling the multi tenant service.
     * @param tenantUserCrudService The multi tenant user CRUD service used as backend to this single tenant
     *                              implementation.
     */
    @Inject
    public UserCrudMock(
            final Tenant defaultTenant,
            final TenantUserCrudService tenantUserCrudService
    ) {
        this.defaultTenant = defaultTenant;
        this.service = tenantUserCrudService;
    }


    @Override
    public User create(User user) throws DuplicateEntityException {
        return service.create(defaultTenant, user);
    }


    @Override
    public Optional<User> retrieve(UUID uniqueId) {
        return service.retrieve(defaultTenant, uniqueId);
    }

    @Override
    public Optional<User> retrieve(String userName) {
        return service.retrieve(defaultTenant, userName);
    }

    @Override
    public Set<User> retrieve() {
        return service.retrieve(defaultTenant);
    }

    @Override
    public Page<User> retrieve(PageRequest pageRequest) {
        return service.retrieve(defaultTenant, pageRequest);
    }


    @Override
    public User update(User user) {
        return service.update(defaultTenant, user);
    }


    @Override
    public void delete(User user) {
        service.delete(defaultTenant, user);
    }

    @Override
    public void delete(UUID uniqueId) {
        service.delete(defaultTenant, uniqueId);
    }

    @Override
    public void delete(String userName) {
        service.delete(defaultTenant, userName);
    }
}
