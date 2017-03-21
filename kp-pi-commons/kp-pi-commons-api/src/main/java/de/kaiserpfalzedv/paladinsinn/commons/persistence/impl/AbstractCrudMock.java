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

package de.kaiserpfalzedv.paladinsinn.commons.persistence.impl;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import de.kaiserpfalzedv.paladinsinn.commons.paging.Page;
import de.kaiserpfalzedv.paladinsinn.commons.paging.PageRequest;
import de.kaiserpfalzedv.paladinsinn.commons.persistence.CrudService;
import de.kaiserpfalzedv.paladinsinn.commons.persistence.DuplicateEntityException;
import de.kaiserpfalzedv.paladinsinn.commons.persistence.Identifiable;
import de.kaiserpfalzedv.paladinsinn.commons.persistence.TenantCrudService;
import de.kaiserpfalzedv.paladinsinn.commons.tenant.model.Tenant;
import de.kaiserpfalzedv.paladinsinn.commons.tenant.model.impl.DefaultTenant;

/**
 * The tenant-less version of the mock service uses the multi-tenant {@link TenantCrudService<T>} with the
 * {@link DefaultTenant} as tenant.
 *
 * @param <T> The type of data to be persisted.
 *
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2017-03-19
 */
public abstract class AbstractCrudMock<T extends Identifiable> implements CrudService<T> {
    private final Tenant defaultTenant;
    private final TenantCrudService<T> service;


    /**
     * @param defaultTenant         The tenant to use when calling the multi tenant service.
     * @param userTenantCrudService The multi tenant user CRUD service used as backend to this single tenant
     *                              implementation.
     */
    public AbstractCrudMock(
            final Tenant defaultTenant,
            final TenantCrudService<T> userTenantCrudService
    ) {
        this.defaultTenant = defaultTenant;
        this.service = userTenantCrudService;
    }


    @Override
    public T create(T data) throws DuplicateEntityException {
        return service.create(defaultTenant, data);
    }


    @Override
    public Optional<T> retrieve(UUID uniqueId) {
        return service.retrieve(defaultTenant, uniqueId);
    }

    @Override
    public Optional<T> retrieve(String uniqueName) {
        return service.retrieve(defaultTenant, uniqueName);
    }

    @Override
    public Set<T> retrieve() {
        return service.retrieve(defaultTenant);
    }

    @Override
    public Page<T> retrieve(PageRequest pageRequest) {
        return service.retrieve(defaultTenant, pageRequest);
    }


    @Override
    public T update(T data) {
        return service.update(defaultTenant, data);
    }


    @Override
    public void delete(T data) {
        service.delete(defaultTenant, data);
    }

    @Override
    public void delete(UUID uniqueId) {
        service.delete(defaultTenant, uniqueId);
    }

    @Override
    public void delete(String uniqueName) {
        service.delete(defaultTenant, uniqueName);
    }
}
