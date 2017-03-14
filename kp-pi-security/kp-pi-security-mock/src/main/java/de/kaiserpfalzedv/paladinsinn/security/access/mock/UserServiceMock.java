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

import java.util.Collection;
import java.util.HashMap;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import de.kaiserpfalzedv.paladinsinn.commons.service.MockService;
import de.kaiserpfalzedv.paladinsinn.security.access.services.LoginService;
import de.kaiserpfalzedv.paladinsinn.security.access.PasswordFailureException;
import de.kaiserpfalzedv.paladinsinn.security.access.model.User;
import de.kaiserpfalzedv.paladinsinn.security.access.UserHasNoAccessToTenantException;
import de.kaiserpfalzedv.paladinsinn.security.access.UserIsLockedException;
import de.kaiserpfalzedv.paladinsinn.security.access.services.UserLoaderService;
import de.kaiserpfalzedv.paladinsinn.security.access.UserNotFoundException;
import de.kaiserpfalzedv.paladinsinn.security.access.model.impl.UserBuilder;
import de.kaiserpfalzedv.paladinsinn.security.tenant.model.Tenant;
import de.kaiserpfalzedv.paladinsinn.security.tenant.impl.NullTenant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The service mock for the user management services.
 * 
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2017-03-14
 */
@MockService
public class UserServiceMock implements LoginService, UserLoaderService {
    private static final Logger LOG = LoggerFactory.getLogger(UserServiceMock.class);

    private static final HashMap<Tenant, HashMap<String, User>> tenantUsers = new HashMap<>();
    private static final HashMap<String, User> users = new HashMap<>();


    @PostConstruct
    public void init() {
        LOG.info("{} started.", getClass().getSimpleName());
    }

    @PreDestroy
    public void close() {
        LOG.info("{} stopping (purging {} tenants with {} users).",
                 getClass().getSimpleName(), tenantUsers.size(), users.size());

        users.clear();
        tenantUsers.clear();
    }

    
    @Override
    public User login(String userId, String password)
            throws UserNotFoundException, PasswordFailureException, UserIsLockedException {
        try {
            return login(new NullTenant(), userId, password);
        } catch (UserHasNoAccessToTenantException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public User login(Tenant tenant, String userId, String password)
            throws UserNotFoundException, PasswordFailureException, UserIsLockedException,
                   UserHasNoAccessToTenantException {
        checkTenantForUser(tenant, userId);
        checkExistingUser(userId);

        User user = users.get(userId);

        try { // we want to log the error so we catch the exceptions, log them and rethrow them.
            user.login(password);
        } catch (PasswordFailureException e) {
            LOG.warn("Wrong password for user: {}", user);

            throw e;
        } catch (UserIsLockedException e) {
            LOG.warn("The user is locked: {}", user);

            throw e;
        }

        LOG.info("User logged in: {}", user);
        return new UserBuilder().withUser(user).build();
    }

    private void checkTenantForUser(Tenant tenant, String userId) throws UserHasNoAccessToTenantException {
        if (! tenantUsers.containsKey(tenant) || ! tenantUsers.get(tenant).containsKey(userId)) {
            LOG.warn("User has no access to tenant or tenant does not exist: {}", userId);

            throw new UserHasNoAccessToTenantException(userId, tenant);
        }
    }

    private void checkExistingUser(String userId) throws UserNotFoundException {
        if (! users.containsKey(userId)) {
            LOG.warn("User not found: {}", userId);

            throw new UserNotFoundException(userId);
        }
    }


    @Override
    public void loadUsers(final Collection<User> users) {
        loadUsersForTenant(new NullTenant(), users);
    }

    @Override
    public synchronized void loadUsersForTenant(final Tenant tenant, final Collection<User> users) {
        HashMap<String, User> newTenantUsers = new HashMap<>(users.size());

        users.forEach(u -> { UserServiceMock.users.put(u.getName(), u); newTenantUsers.put(u.getName(), u);});

        if (UserServiceMock.tenantUsers.containsKey(tenant)) {
            UserServiceMock.tenantUsers.get(tenant).clear();
            UserServiceMock.tenantUsers.get(tenant).putAll(newTenantUsers);
        } else {
            UserServiceMock.tenantUsers.put(tenant, newTenantUsers);
        }

        LOG.info("Loaded {} users for tenant {} into User Service MOCK.", users.size(), tenant);
    }
}
