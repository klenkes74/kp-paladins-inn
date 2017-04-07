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

import de.kaiserpfalzedv.paladinsinn.commons.api.paging.Page;
import de.kaiserpfalzedv.paladinsinn.commons.api.paging.PageRequestBuilder;
import de.kaiserpfalzedv.paladinsinn.commons.api.persistence.DuplicateEntityException;
import de.kaiserpfalzedv.paladinsinn.commons.api.persistence.EntityNotFoundException;
import de.kaiserpfalzedv.paladinsinn.commons.api.tenant.model.Tenant;
import de.kaiserpfalzedv.paladinsinn.commons.api.tenant.model.TenantBuilder;
import de.kaiserpfalzedv.paladinsinn.commons.api.tenant.service.TenantQueryService;
import de.kaiserpfalzedv.paladinsinn.commons.api.tenant.store.TenantCrudService;
import de.kaiserpfalzedv.paladinsinn.commons.tenant.store.mock.TenantCrudMock;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2017-04-07
 */
public class TenantQueryServiceTest {
    private static final Logger LOG = LoggerFactory.getLogger(TenantQueryServiceTest.class);

    private TenantQueryService service;

    private TenantCrudService crudService;


    @Test
    public void shouldGiveTheTenantWhenTheUniqueIdIsGiven() throws DuplicateEntityException {
        Tenant tenant = new TenantBuilder()
                .withKey("ONE")
                .withName("one.tenant")
                .build();
        crudService.create(tenant);

        Optional<? extends Tenant> result = service.retrieve(tenant.getUniqueId());

        assertTrue(result.isPresent());
        assertEquals(tenant, result.get());
    }


    @Test
    public void shouldGiveTheTenantWhenTheNewKeyIsGiven() throws DuplicateEntityException, EntityNotFoundException {
        Tenant tenant = new TenantBuilder()
                .withKey("ONE")
                .withName("one.tenant")
                .build();
        crudService.create(tenant);

        Optional<? extends Tenant> result = service.retrieve("ONE");

        assertTrue(result.isPresent());
        assertEquals(tenant.getKey(), result.get().getKey());
        assertEquals(tenant.getName(), result.get().getName());
        assertEquals(tenant.getUniqueId(), result.get().getUniqueId());

    }


    @Test
    public void shouldReturnTheTenantWhenAskedWithTheFullNameGiven() throws DuplicateEntityException, EntityNotFoundException {
        Tenant tenant = new TenantBuilder()
                .withKey("ONE")
                .withName("one.tenant")
                .build();
        crudService.create(tenant);

        Optional<? extends Tenant> result = service.retrieveByFullName("one.tenant");
        assertTrue(result.isPresent());
        assertEquals(tenant.getName(), result.get().getName());
        assertEquals(tenant.getKey(), result.get().getKey());
        assertEquals(tenant.getUniqueId(), result.get().getUniqueId());
    }


    @Test
    public void shouldDeleteAGivenTenantWhenIdIsCorrect() throws DuplicateEntityException {
        Tenant tenant = new TenantBuilder()
                .withKey("ONE")
                .withName("one.tenant")
                .build();
        crudService.create(tenant);

        Page<? extends Tenant> result = service.retrieve(new PageRequestBuilder().build());

        assertEquals(1, result.getCurrentPageNumber());
        assertEquals(1, result.getTotalPages());
    }


    @Before
    public void setupService() {
        crudService = new TenantCrudMock();

        service = new TenantQueryServiceImpl(crudService);
    }
}
