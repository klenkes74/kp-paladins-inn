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

package de.kaiserpfalzedv.paladinsinn.security.services;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import javax.inject.Inject;

import de.kaiserpfalzedv.paladinsinn.commons.api.paging.Page;
import de.kaiserpfalzedv.paladinsinn.commons.api.paging.PageRequest;
import de.kaiserpfalzedv.paladinsinn.commons.api.paging.PageRequestBuilder;
import de.kaiserpfalzedv.paladinsinn.commons.api.tenant.model.DefaultTenant;
import de.kaiserpfalzedv.paladinsinn.commons.api.tenant.model.Tenant;
import de.kaiserpfalzedv.paladinsinn.commons.api.tenant.service.TenantQueryService;
import de.kaiserpfalzedv.paladinsinn.security.api.model.User;
import de.kaiserpfalzedv.paladinsinn.security.api.services.LoginService;
import de.kaiserpfalzedv.paladinsinn.security.api.services.PasswordFailureException;
import de.kaiserpfalzedv.paladinsinn.security.api.services.UserHasNoAccessToTenantException;
import de.kaiserpfalzedv.paladinsinn.security.api.services.UserIsLockedException;
import de.kaiserpfalzedv.paladinsinn.security.api.services.UserMultitenantQueryService;
import de.kaiserpfalzedv.paladinsinn.security.api.services.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author rlichti {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 2017-03-15
 */
public class LoginServiceImpl implements LoginService {
    private static final Logger LOG = LoggerFactory.getLogger(LoginServiceImpl.class);
    private static final int TENANT_PAGE_SIZE = 50;

    /**
     * The service for handling all tenant related data.
     */
    private TenantQueryService tenantService;

    /**
     * The service for handling all user related data.
     */
    private UserMultitenantQueryService userService;

    @Inject
    public LoginServiceImpl(
            final TenantQueryService tenantService,
            final UserMultitenantQueryService userService
    ) {
        this.tenantService = tenantService;
        this.userService = userService;
    }


    @Override
    public User login(final Tenant tenant, final String userId, final String password)
            throws UserNotFoundException, PasswordFailureException,
                   UserIsLockedException, UserHasNoAccessToTenantException {
        Optional<? extends User> wrappedUser = userService.retrieve(tenant, userId);
        if (!wrappedUser.isPresent()) {
            throw new UserNotFoundException(userId);
        }

        User user = wrappedUser.get();

        user.login(password);

        return user;
    }

    @Override
    public User login(final UUID tenantId, final String userId, final String password)
            throws UserNotFoundException, PasswordFailureException,
                   UserIsLockedException, UserHasNoAccessToTenantException {
        Optional<? extends Tenant> wrappedTenant = tenantService.retrieve(tenantId);
        if (!wrappedTenant.isPresent()) {
            throw new UserNotFoundException(userId);
        }

        return login(wrappedTenant.get(), userId, password);
    }

    @Override
    public User login(final String tenantKey, final String userId, final String password)
            throws UserNotFoundException, PasswordFailureException,
                   UserIsLockedException, UserHasNoAccessToTenantException {
        Optional<? extends Tenant> wrappedTenant = tenantService.retrieve(tenantKey);

        if (!wrappedTenant.isPresent()) {
            throw new UserNotFoundException(userId);
        }

        return login(wrappedTenant.get(), userId, password);
    }

    @Override
    public User login(final String userId, final String password)
            throws UserNotFoundException, PasswordFailureException,
                   UserIsLockedException, UserHasNoAccessToTenantException {
        Tenant tenant = DefaultTenant.INSTANCE;
        String userName = userId;

        if (userId.contains("@")) {
            String[] userAndTenant = userId.split("@", 2);
            userName = userAndTenant[0];

            Optional<? extends Tenant> wrappedTenant = tenantService.retrieveByFullName(userAndTenant[1]);
            if (!wrappedTenant.isPresent()) {
                throw new UserNotFoundException(userId);
            }

            tenant = wrappedTenant.get();
        }

        if (userId.contains("\\")) {
            String[] userAndTenant = userId.split("\\\\", 2);
            userName = userAndTenant[1];

            Optional<? extends Tenant> wrappedTenant = tenantService.retrieve(userAndTenant[0]);
            if (!wrappedTenant.isPresent()) {
                throw new UserNotFoundException(userId);
            }

            tenant = wrappedTenant.get();
        }

        return login(tenant, userName, password);
    }

    @Override
    public Set<? extends Tenant> availableTenants() {
        HashSet<Tenant> result = new HashSet<>(TENANT_PAGE_SIZE);

        Page<? extends Tenant> answer;
        PageRequest pageRequest = new PageRequestBuilder()
                .withSize(TENANT_PAGE_SIZE)
                .build();

        do {
            answer = tenantService.retrieve(pageRequest);
            LOG.trace("Retrieving {} of {} total tenants (page {} of {}).",
                      answer.getCurrentPageSize(), answer.getTotalElements(),
                      answer.getCurrentPageNumber(), answer.getTotalPages()
            );

            result.addAll(answer.getData());

            pageRequest = answer.getNextPageRequest();
        }
        while (answer.getCurrentPageNumber() <= answer.getTotalPages());

        LOG.debug("Loaded {} tenants.", result.size());
        return Collections.unmodifiableSet(result);
    }
}
