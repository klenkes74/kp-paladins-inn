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

import de.kaiserpfalzedv.paladinsinn.commons.BuilderValidationException;
import de.kaiserpfalzedv.paladinsinn.commons.paging.Page;
import de.kaiserpfalzedv.paladinsinn.commons.paging.PageRequest;
import de.kaiserpfalzedv.paladinsinn.commons.paging.impl.PageBuilder;
import de.kaiserpfalzedv.paladinsinn.commons.service.MockService;
import de.kaiserpfalzedv.paladinsinn.security.access.model.User;
import de.kaiserpfalzedv.paladinsinn.security.access.model.impl.UserBuilder;
import de.kaiserpfalzedv.paladinsinn.security.access.store.DuplicateEntityException;
import de.kaiserpfalzedv.paladinsinn.security.access.store.SecurityPersistenceRuntimeException;
import de.kaiserpfalzedv.paladinsinn.security.access.store.TenantUserCrudService;
import de.kaiserpfalzedv.paladinsinn.security.tenant.model.Tenant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2017-03-19
 */
@MockService
public class TenantUserCrudMock implements TenantUserCrudService {
    private static final Logger LOG = LoggerFactory.getLogger(TenantUserCrudMock.class);

    private static final HashMap<Tenant, HashSet<User>> tenantUsers = new HashMap<>();
    private static final HashMap<Tenant, HashMap<UUID, User>> userUuidMap = new HashMap<>();
    private static final HashMap<Tenant, HashMap<String, User>> userUserIdMap = new HashMap<>();

    @Override
    public User create(Tenant tenant, User user) throws DuplicateEntityException {
        prepareTenant(tenant);

        if (userUuidMap.get(tenant).containsKey(user.getUniqueId()))
            throw new DuplicateEntityException(User.class, user);

        if (userUserIdMap.get(tenant).containsKey(user.getName()))
            throw new DuplicateEntityException(User.class, user);


        User data = new UserBuilder().withUser(user).build();

        tenantUsers.get(tenant).add(data);
        userUuidMap.get(tenant).put(user.getUniqueId(), data);
        userUserIdMap.get(tenant).put(user.getName(), data);

        return data;
    }

    private void prepareTenant(final Tenant tenant) {
        if (!tenantUsers.containsKey(tenant)) {
            LOG.info("Added new tenant: {}", tenant);

            tenantUsers.put(tenant, new HashSet<>());
        }

        if (!userUuidMap.containsKey(tenant)) {
            userUuidMap.put(tenant, new HashMap<>());
        }

        if (!userUserIdMap.containsKey(tenant)) {
            userUserIdMap.put(tenant, new HashMap<>());
        }
    }


    @Override
    public Optional<User> retrieve(Tenant tenant, UUID uniqueId) {
        prepareTenant(tenant);

        if (!userUuidMap.get(tenant).containsKey(uniqueId)) {
            LOG.warn("Tenant '{}' contains no user with UUID: {}", tenant.getKey(), uniqueId);

            return Optional.empty();
        }

        LOG.debug("Retrieving user from tenant '{}' with unique id: {}", tenant.getKey(), uniqueId);
        return Optional.ofNullable(userUuidMap.get(tenant).get(uniqueId));
    }

    @Override
    public Optional<User> retrieve(Tenant tenant, String userName) {
        prepareTenant(tenant);

        if (!userUserIdMap.get(tenant).containsKey(userName)) {
            LOG.warn("Tenant '{}' contains no user with user name: {}", tenant.getKey(), userName);

            return Optional.empty();
        }

        LOG.debug("Retrieving user from tenant '{}' with user name: {}", tenant.getKey(), userName);
        return Optional.ofNullable(userUserIdMap.get(tenant).get(userName));
    }

    @Override
    public Set<User> retrieve(Tenant tenant) {
        prepareTenant(tenant);

        return tenantUsers.get(tenant);
    }

    @Override
    public Page<User> retrieve(Tenant tenant, PageRequest pageRequest) {
        try {
            return new PageBuilder<User>()
                    .withPage(retrieve(tenant), pageRequest.getPageNumber(), pageRequest.getPageSize())
                    .build();
        } catch (BuilderValidationException e) {
            LOG.error("Can't build result page {} (with size {}) for tenant: " + tenant.getKey(), e);

            throw new SecurityPersistenceRuntimeException(User.class, e.getMessage());
        }

    }


    @Override
    public User update(Tenant tenant, User user) {
        prepareTenant(tenant);

        delete(tenant, user);

        try {
            return create(tenant, user);
        } catch (DuplicateEntityException e) {
            throw new SecurityPersistenceRuntimeException(User.class, "Invalid state: can't have a duplicate here!", e);
        }
    }


    @Override
    public void delete(Tenant tenant, User user) {
        prepareTenant(tenant);

        tenantUsers.get(tenant).remove(user);
        userUuidMap.get(tenant).remove(user.getUniqueId());
        userUserIdMap.get(tenant).remove(user.getName());
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
