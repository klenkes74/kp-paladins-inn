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

package de.kaiserpfalzedv.paladinsinn.security.access.mock;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import de.kaiserpfalzedv.paladinsinn.commons.paging.Page;
import de.kaiserpfalzedv.paladinsinn.commons.paging.PageRequest;
import de.kaiserpfalzedv.paladinsinn.security.access.DuplicateEntityException;
import de.kaiserpfalzedv.paladinsinn.security.access.model.User;
import de.kaiserpfalzedv.paladinsinn.security.access.services.TenantUserCrudService;
import de.kaiserpfalzedv.paladinsinn.security.access.services.UserCrudService;
import de.kaiserpfalzedv.paladinsinn.security.tenant.model.Tenant;
import de.kaiserpfalzedv.paladinsinn.security.tenant.model.impl.DefaultTenant;

/**
 * The tenant-less version of the mock service uses the multi-tenant {@link TenantUserCrudMock} with the
 * {@link DefaultTenant} as tenant.
 *
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2017-03-19
 */
public class UserCrudMock implements UserCrudService {
    private static final Tenant TENANT = new DefaultTenant();
    private TenantUserCrudService service = new TenantUserCrudMock();

    @Override
    public User create(User user) throws DuplicateEntityException {
        return service.create(TENANT, user);
    }

    @Override
    public Optional<User> retrieve(UUID uniqueId) {
        return service.retrieve(TENANT, uniqueId);
    }

    @Override
    public Optional<User> retrieve(String userName) {
        return service.retrieve(TENANT, userName);
    }

    @Override
    public Set<User> retrieve() {
        return service.retrieve(TENANT);
    }

    @Override
    public Page<User> retrieve(PageRequest pageRequest) {
        return service.retrieve(TENANT, pageRequest);
    }

    @Override
    public User update(User user) {
        return service.update(TENANT, user);
    }

    @Override
    public void delete(User user) {
        service.delete(TENANT, user);
    }

    @Override
    public void delete(UUID uniqueId) {
        service.delete(TENANT, uniqueId);
    }

    @Override
    public void delete(String userName) {
        service.delete(TENANT, userName);
    }
}
