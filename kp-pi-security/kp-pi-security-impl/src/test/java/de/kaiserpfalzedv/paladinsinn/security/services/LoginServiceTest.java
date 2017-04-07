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

import java.util.UUID;

import de.kaiserpfalzedv.paladinsinn.commons.api.persistence.DuplicateEntityException;
import de.kaiserpfalzedv.paladinsinn.commons.api.persistence.DuplicateUniqueIdException;
import de.kaiserpfalzedv.paladinsinn.commons.api.persistence.DuplicateUniqueNameException;
import de.kaiserpfalzedv.paladinsinn.commons.api.person.Email;
import de.kaiserpfalzedv.paladinsinn.commons.api.tenant.model.DefaultTenant;
import de.kaiserpfalzedv.paladinsinn.commons.api.tenant.model.Tenant;
import de.kaiserpfalzedv.paladinsinn.commons.api.tenant.model.TenantBuilder;
import de.kaiserpfalzedv.paladinsinn.commons.api.tenant.service.TenantCommandService;
import de.kaiserpfalzedv.paladinsinn.commons.api.tenant.service.TenantQueryService;
import de.kaiserpfalzedv.paladinsinn.commons.api.tenant.store.TenantCrudService;
import de.kaiserpfalzedv.paladinsinn.commons.tenant.service.TenantCommandServiceImpl;
import de.kaiserpfalzedv.paladinsinn.commons.tenant.service.TenantQueryServiceImpl;
import de.kaiserpfalzedv.paladinsinn.commons.tenant.store.mock.TenantCrudMock;
import de.kaiserpfalzedv.paladinsinn.security.api.model.User;
import de.kaiserpfalzedv.paladinsinn.security.api.model.UserBuilder;
import de.kaiserpfalzedv.paladinsinn.security.api.services.LoginService;
import de.kaiserpfalzedv.paladinsinn.security.api.services.PasswordFailureException;
import de.kaiserpfalzedv.paladinsinn.security.api.services.UserMultitenantQueryService;
import de.kaiserpfalzedv.paladinsinn.security.api.services.UserNotFoundException;
import de.kaiserpfalzedv.paladinsinn.security.api.store.UserMultitenantCrudService;
import de.kaiserpfalzedv.paladinsinn.security.store.mock.UserMultitenantCrudMock;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2017-04-07
 */
public class LoginServiceTest {
    private static final Logger LOG = LoggerFactory.getLogger(LoginServiceTest.class);

    private LoginService service;

    private TenantCrudService crudService = new TenantCrudMock();
    private UserMultitenantCrudService userCrudService = new UserMultitenantCrudMock();

    private TenantQueryService tenantQueryService = new TenantQueryServiceImpl(crudService);
    private TenantCommandService tenantCommandService = new TenantCommandServiceImpl(crudService);
    private UserMultitenantQueryService userMultitenantQueryService = new UserMultiTenantQueryServiceImpl(
            userCrudService,
            tenantQueryService
    );


    @Test(expected = UserNotFoundException.class)
    public void shouldThrowAnExceptionWhenAskedWithUnknownUsername() throws PasswordFailureException {
        service.login("abc", "password");
    }


    @Test
    public void shouldReturnTheCorrectUserWhenTheUserNameAndPasswordIsGiven()
            throws DuplicateUniqueNameException, DuplicateUniqueIdException, PasswordFailureException {
        User orig1 = new UserBuilder()
                .withName("user")
                .withPassword("pass")
                .withEmailAddress(new Email("user@domain.tld"))
                .build();

        User orig2 = new UserBuilder()
                .withName("otherUser")
                .withPassword("pass2")
                .withEmailAddress(new Email("otheruser@domain.tld"))
                .build();

        userCrudService.create(DefaultTenant.INSTANCE, orig1);
        userCrudService.create(DefaultTenant.INSTANCE, orig2);

        User result = service.login("user", "pass");
        LOG.trace("Result: {}", result);

        assertEquals(orig1, result);
    }


    @Test(expected = PasswordFailureException.class)
    public void shouldThrowAnExceptionWhenTheWrongPasswordIsGiven()
            throws DuplicateUniqueNameException, DuplicateUniqueIdException, PasswordFailureException {
        User orig1 = new UserBuilder()
                .withName("wp-user")
                .withPassword("pass")
                .withEmailAddress(new Email("user@domain.tld"))
                .build();
        userCrudService.create(DefaultTenant.INSTANCE, orig1);

        service.login("wp-user", "wrong password");
    }


    @Test
    public void shouldReturnTheCorrectUserWhenTenantUserAndPasswordIsGiven() throws PasswordFailureException, DuplicateUniqueNameException, DuplicateUniqueIdException {
        User orig1 = new UserBuilder()
                .withName("tenant-wp-user")
                .withPassword("password")
                .withEmailAddress(new Email("user@domain.tld"))
                .build();

        userCrudService.create(DefaultTenant.INSTANCE, orig1);

        User result = service.login(DefaultTenant.INSTANCE, "tenant-wp-user", "password");
        LOG.trace("Result: {}", result);

        assertEquals(orig1, result);
    }


    @Test(expected = UserNotFoundException.class)
    public void shouldThrowAnExceptionWhenTheWrongTenantIsGiven()
            throws DuplicateEntityException, PasswordFailureException {
        User orig1 = new UserBuilder()
                .withName("wp-user")
                .withPassword("pass")
                .withEmailAddress(new Email("user@domain.tld"))
                .build();
        Tenant tenant = new TenantBuilder()
                .withUniqueId(UUID.randomUUID())
                .withKey("SECOND")
                .withName("second tenant")
                .build();
        tenantCommandService.create(tenant);
        userCrudService.create(DefaultTenant.INSTANCE, orig1);

        service.login(tenant, "wp-user", "wrong password");
    }


    @Test
    public void shouldGiveTheUserWhenTheCorrectTenantUniqueIdGiven()
            throws DuplicateEntityException, PasswordFailureException {
        User orig1 = new UserBuilder()
                .withName("tenant-unique-id-wp-user")
                .withPassword("pass")
                .withEmailAddress(new Email("user@domain.tld"))
                .build();
        Tenant tenant = new TenantBuilder()
                .withUniqueId(UUID.randomUUID())
                .withKey("THIRD")
                .withName("third tenant")
                .build();
        tenantCommandService.create(tenant);
        userCrudService.create(tenant, orig1);

        User result = service.login(tenant.getUniqueId(), "tenant-unique-id-wp-user", "pass");
        LOG.trace("Result: {}", result);

        assertEquals(result, orig1);
    }


    @Test
    public void shouldGiveTheUserWhenTheCorrectTenantKeyGiven()
            throws DuplicateEntityException, PasswordFailureException {
        User orig1 = new UserBuilder()
                .withName("tenant-key-wp-user")
                .withPassword("pass")
                .withEmailAddress(new Email("user@domain.tld"))
                .build();
        Tenant tenant = new TenantBuilder()
                .withUniqueId(UUID.randomUUID())
                .withKey("FOURTH")
                .withName("fourth tenant")
                .build();
        tenantCommandService.create(tenant);
        userCrudService.create(tenant, orig1);

        User result = service.login(tenant.getKey(), "tenant-key-wp-user", "pass");
        LOG.trace("Result: {}", result);

        assertEquals(result, orig1);
    }


    @Before
    public void setupService() {
        service = new LoginServiceImpl(tenantQueryService, userMultitenantQueryService);
    }
}
