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

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import de.kaiserpfalzedv.paladinsinn.commons.BuilderValidationException;
import de.kaiserpfalzedv.paladinsinn.commons.paging.Page;
import de.kaiserpfalzedv.paladinsinn.commons.paging.PageRequest;
import de.kaiserpfalzedv.paladinsinn.commons.paging.impl.PageBuilder;
import de.kaiserpfalzedv.paladinsinn.commons.persistence.DuplicateEntityException;
import de.kaiserpfalzedv.paladinsinn.commons.persistence.Identifiable;
import de.kaiserpfalzedv.paladinsinn.commons.persistence.PersistenceRuntimeException;
import de.kaiserpfalzedv.paladinsinn.commons.service.MockService;
import de.kaiserpfalzedv.paladinsinn.commons.tenant.model.Tenant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2017-03-19
 */
@MockService
public abstract class AbstractTenantCrudMock<T extends Identifiable> implements de.kaiserpfalzedv.paladinsinn.commons.persistence.TenantCrudService<T> {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractTenantCrudMock.class);

    private final HashMap<Tenant, HashSet<T>> tenantMap = new HashMap<>();
    private final HashMap<Tenant, HashMap<UUID, T>> uuidMap = new HashMap<>();
    private final HashMap<Tenant, HashMap<String, T>> uniqueNameMap = new HashMap<>();

    private final Class<?> clasz;


    public AbstractTenantCrudMock(final Class<?> clasz) {
        this.clasz = clasz;
    }

    public abstract T copy(final T data);

    @Override
    public T create(Tenant tenant, T user) throws DuplicateEntityException {
        prepareTenant(tenant);

        if (uuidMap.get(tenant).containsKey(user.getUniqueId()))
            throw new DuplicateEntityException(clasz, user);

        if (uniqueNameMap.get(tenant).containsKey(user.getName()))
            throw new DuplicateEntityException(clasz, user);


        T data = copy(user);

        tenantMap.get(tenant).add(data);
        uuidMap.get(tenant).put(user.getUniqueId(), data);
        uniqueNameMap.get(tenant).put(user.getName(), data);

        return data;
    }

    private void prepareTenant(final Tenant tenant) {
        if (!tenantMap.containsKey(tenant)) {
            LOG.info("Added new tenant: {}", tenant);

            tenantMap.put(tenant, new HashSet<>());
        }

        if (!uuidMap.containsKey(tenant)) {
            uuidMap.put(tenant, new HashMap<>());
        }

        if (!uniqueNameMap.containsKey(tenant)) {
            uniqueNameMap.put(tenant, new HashMap<>());
        }
    }


    @Override
    public Optional<T> retrieve(Tenant tenant, UUID uniqueId) {
        prepareTenant(tenant);

        if (!uuidMap.get(tenant).containsKey(uniqueId)) {
            LOG.warn("Tenant '{}' contains no {} with UUID: {}", clasz.getSimpleName(), tenant.getKey(), uniqueId);

            return Optional.empty();
        }

        LOG.debug("Retrieving {} from tenant '{}' with unique id: {}", clasz.getSimpleName(), tenant.getKey(), uniqueId);
        return Optional.ofNullable(uuidMap.get(tenant).get(uniqueId));
    }

    @Override
    public Optional<T> retrieve(Tenant tenant, String userName) {
        prepareTenant(tenant);

        if (!uniqueNameMap.get(tenant).containsKey(userName)) {
            LOG.warn("Tenant '{}' contains no {} with user name: {}", clasz.getSimpleName(), tenant.getKey(), userName);

            return Optional.empty();
        }

        LOG.debug("Retrieving {} from tenant '{}' with user name: {}", clasz.getSimpleName(), tenant.getKey(), userName);
        return Optional.ofNullable(uniqueNameMap.get(tenant).get(userName));
    }

    @Override
    public Set<T> retrieve(Tenant tenant) {
        prepareTenant(tenant);

        return Collections.unmodifiableSet(tenantMap.get(tenant));
    }

    @Override
    public Page<T> retrieve(Tenant tenant, PageRequest pageRequest) {
        try {
            return new PageBuilder<T>()
                    .withPage(retrieve(tenant), pageRequest.getPageNumber(), pageRequest.getPageSize())
                    .build();
        } catch (BuilderValidationException e) {
            LOG.error("Can't build result page {} (with size {}) for tenant: " + tenant.getKey(), e);

            throw new PersistenceRuntimeException(clasz, e.getMessage());
        }

    }


    @Override
    public T update(Tenant tenant, T user) {
        prepareTenant(tenant);

        delete(tenant, user);

        try {
            return create(tenant, user);
        } catch (DuplicateEntityException e) {
            throw new PersistenceRuntimeException(clasz, "Invalid state: can't have a duplicate here!", e);
        }
    }


    @Override
    public void delete(Tenant tenant, T user) {
        prepareTenant(tenant);

        tenantMap.get(tenant).remove(user);
        uuidMap.get(tenant).remove(user.getUniqueId());
        uniqueNameMap.get(tenant).remove(user.getName());
    }

    @Override
    public void delete(Tenant tenant, UUID uniqueId) {
        retrieve(tenant, uniqueId).ifPresent(u -> delete(tenant, u));
    }

    @Override
    public void delete(Tenant tenant, String userName) {
        retrieve(tenant, userName).ifPresent(u -> delete(tenant, u));
    }
}
