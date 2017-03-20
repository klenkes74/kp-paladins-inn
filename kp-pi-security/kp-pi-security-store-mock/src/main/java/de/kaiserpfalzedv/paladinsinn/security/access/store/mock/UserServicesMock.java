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

import de.kaiserpfalzedv.paladinsinn.commons.person.Email;
import de.kaiserpfalzedv.paladinsinn.commons.person.Name;
import de.kaiserpfalzedv.paladinsinn.commons.service.MockService;
import de.kaiserpfalzedv.paladinsinn.security.access.model.Persona;
import de.kaiserpfalzedv.paladinsinn.security.access.model.User;
import de.kaiserpfalzedv.paladinsinn.security.access.model.impl.UserBuilder;
import de.kaiserpfalzedv.paladinsinn.security.access.services.*;
import de.kaiserpfalzedv.paladinsinn.security.tenant.model.Tenant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Collection;
import java.util.HashMap;

/**
 * The service mock for the user management services.
 * 
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2017-03-14
 */
@MockService
public class UserServicesMock implements LoginService, UserLoaderService, UserIdGenerator {
    private static final Logger LOG = LoggerFactory.getLogger(UserServicesMock.class);

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
    public User login(final Tenant tenant, final String userId, final String password)
            throws UserNotFoundException, PasswordFailureException, UserIsLockedException,
                   UserHasNoAccessToTenantException {
        checkExistingUser(userId);
        checkTenantForUser(tenant, userId);

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

    private void checkExistingUser(String userId) throws UserNotFoundException {
        if (! users.containsKey(userId)) {
            LOG.warn("User not found: {}", userId);

            throw new UserNotFoundException(userId);
        }
    }

    private void checkTenantForUser(Tenant tenant, String userId) throws UserHasNoAccessToTenantException {
        if (!tenantUsers.containsKey(tenant) || !tenantUsers.get(tenant).containsKey(userId)) {
            LOG.warn("User has no access to tenant or tenant does not exist: {}", userId);

            throw new UserHasNoAccessToTenantException(userId, tenant);
        }
    }


    @Override
    public synchronized void loadUsersForTenant(final Tenant tenant, final Collection<User> users) {
        HashMap<String, User> newTenantUsers = new HashMap<>(users.size());

        users.forEach(u -> {
            if (UserServicesMock.users.containsKey(u.getName())) {
                throw new IllegalArgumentException("The user id '" + u.getName()
                                                           + "' is assigned multiple times. Please check!");
            }

            UserServicesMock.users.put(u.getName(), u);
            newTenantUsers.put(u.getName(), u);
        });

        if (UserServicesMock.tenantUsers.containsKey(tenant)) {
            UserServicesMock.tenantUsers.get(tenant).clear();
            UserServicesMock.tenantUsers.get(tenant).putAll(newTenantUsers);
        } else {
            UserServicesMock.tenantUsers.put(tenant, newTenantUsers);
        }

        LOG.info("Loaded {} users for tenant {} into User Service MOCK.", users.size(), tenant);
    }


    @Override
    public String generateUserId(final Persona person, final Email emailAddress) {
        String result = calculateUserIdByName(person);

        if (result == null || result.isEmpty()) {
            result = calculateUserIdByEmailAddress(emailAddress);
        }

        result += fillZeros(result);
        result = result.replace(' ', '_');


        int counter = 0;
        while (users.containsKey(result)) {
            counter++;

            if (counter < 10) {
                result = result.substring(0, 7) + counter;
            } else if (counter < 100) {
                result = result.substring(0, 6) + counter;
            } else if (counter < 1000) {
                result = result.substring(0, 5) + counter;
            } else if (counter < 10000) {
                result = result.substring(0, 4) + counter;
            } else if (counter < 100000) {
                result = result.substring(0, 3) + counter;
            } else if (counter < 1000000) {
                result = result.substring(0, 2) + counter;
            } else if (counter < 10000000) {
                result = result.substring(0, 1) + counter;
            } else if (counter < 100000000) {
                result = "" + counter;
            } else {
                throw new IllegalStateException("No valid user id found!");
            }
        }

        return result;
    }

    private String calculateUserIdByName(final Persona person) {
        String result;

        Name name = person.getFullName();

        String sn = name.getSn();
        int snLength = sn.length();

        String givenName = name.getGivenName();
        int givenNameLength = givenName.length();

        if (snLength >= 7 && givenNameLength >= 1) {
            result = givenName.substring(0, 1) + sn.substring(0, 7);
        } else if (givenNameLength == 0) {
            result = sn.substring(0, 8);
        } else {
            result = givenName.substring(0, 8 - snLength) + sn;
        }

        return result;
    }

    private String calculateUserIdByEmailAddress(final Email emailAddress) {
        String result = "";

        if (emailAddress != null) {
            result = emailAddress.getAddress();
        }

        return result;
    }

    /**
     * Return a string consiting of 0s to fill up to 8 letters.
     *
     * @param userId The user id generated.
     *
     * @return string of 0s to fill the original userId to 8 letters.
     */
    private String fillZeros(final String userId) {
        int length = userId.length();

        if (length >= 8)
            return "";

        return String.format("%" + (7 - length) + "d", 0);
    }
}