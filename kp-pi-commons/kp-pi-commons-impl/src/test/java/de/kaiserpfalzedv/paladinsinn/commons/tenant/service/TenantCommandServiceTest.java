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

package de.kaiserpfalzedv.paladinsinn.commons.tenant.service;

import java.util.Optional;

import de.kaiserpfalzedv.paladinsinn.commons.api.persistence.DuplicateEntityException;
import de.kaiserpfalzedv.paladinsinn.commons.api.persistence.EntityNotFoundException;
import de.kaiserpfalzedv.paladinsinn.commons.api.tenant.model.Tenant;
import de.kaiserpfalzedv.paladinsinn.commons.api.tenant.model.TenantBuilder;
import de.kaiserpfalzedv.paladinsinn.commons.api.tenant.service.TenantCommandService;
import de.kaiserpfalzedv.paladinsinn.commons.api.tenant.store.TenantCrudService;
import de.kaiserpfalzedv.paladinsinn.commons.tenant.store.mock.TenantCrudMock;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2017-04-07
 */
public class TenantCommandServiceTest {
    private static final Logger LOG = LoggerFactory.getLogger(TenantCommandServiceTest.class);

    private TenantCommandService service;

    private TenantCrudService crudService;


    @Test
    public void shouldSaveATenant() throws DuplicateEntityException {
        Tenant tenant = new TenantBuilder()
                .withKey("ONE")
                .withName("one.tenant")
                .build();

        Tenant saved = service.create(tenant);
        Optional<? extends Tenant> check = crudService.retrieve(tenant.getUniqueId());

        assertTrue(check.isPresent());
        assertEquals(tenant, saved);
        assertEquals(tenant, check.get());
    }


    @Test
    public void shouldChangeTheTenantWhenANewKeyIsGiven() throws DuplicateEntityException, EntityNotFoundException {
        Tenant tenant = new TenantBuilder()
                .withKey("ONE")
                .withName("one.tenant")
                .build();
        service.create(tenant);

        service.changeKey(tenant.getUniqueId(), "NEWKEY");

        Optional<? extends Tenant> check = crudService.retrieve("NEWKEY");
        assertTrue(check.isPresent());
        assertNotEquals(tenant.getKey(), check.get().getKey());

        assertEquals(tenant.getName(), check.get().getName());
        assertEquals(tenant.getUniqueId(), check.get().getUniqueId());

    }


    @Test
    public void shouldChangeTheTenantWhenANewNameIsGiven() throws DuplicateEntityException, EntityNotFoundException {
        Tenant tenant = new TenantBuilder()
                .withKey("ONE")
                .withName("one.tenant")
                .build();
        service.create(tenant);

        service.changeName(tenant.getUniqueId(), "new.name");

        Optional<? extends Tenant> check = crudService.retrieve(tenant.getUniqueId());
        assertTrue(check.isPresent());
        assertNotEquals(tenant.getName(), check.get().getName());

        assertEquals(tenant.getKey(), check.get().getKey());
        assertEquals(tenant.getUniqueId(), check.get().getUniqueId());
    }


    @Test
    public void shouldDeleteAGivenTenantWhenIdIsCorrect() throws DuplicateEntityException {
        Tenant tenant = new TenantBuilder()
                .withKey("ONE")
                .withName("one.tenant")
                .build();
        service.create(tenant);

        service.delete(tenant.getUniqueId());

        Optional<? extends Tenant> check = crudService.retrieve(tenant.getUniqueId());
        assertFalse(check.isPresent());
    }


    @Before
    public void setupService() {
        crudService = new TenantCrudMock();

        service = new TenantCommandServiceImpl(crudService);
    }
}
