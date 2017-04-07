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
import java.util.UUID;

import de.kaiserpfalzedv.paladinsinn.commons.api.persistence.DuplicateEntityException;
import de.kaiserpfalzedv.paladinsinn.commons.api.persistence.EntityNotFoundException;
import de.kaiserpfalzedv.paladinsinn.commons.api.tenant.model.Tenant;
import de.kaiserpfalzedv.paladinsinn.commons.api.tenant.model.TenantBuilder;
import de.kaiserpfalzedv.paladinsinn.commons.api.tenant.service.TenantCommandService;
import de.kaiserpfalzedv.paladinsinn.commons.api.tenant.store.TenantCrudService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2017-04-07
 */
public class TenantCommandServiceImpl implements TenantCommandService {
    private static final Logger LOG = LoggerFactory.getLogger(TenantCommandServiceImpl.class);

    private TenantCrudService crudService;


    public TenantCommandServiceImpl(final TenantCrudService crudService) {
        this.crudService = crudService;

        LOG.debug("***** Created: {}", this);
        LOG.debug("*   *   tenant crud service: {}", this.crudService);
    }


    @Override
    public Tenant create(final Tenant tenant) throws DuplicateEntityException {
        return crudService.create(tenant);
    }

    @Override
    public void changeKey(final UUID uniqueId, final String key) throws EntityNotFoundException, DuplicateEntityException {
        Tenant tenant = loadTenant(uniqueId);

        Tenant changed = new TenantBuilder().withTenant(tenant).withKey(key).build();

        crudService.update(changed);
        LOG.info("Changed key to '{}': {} -> {}", key, tenant, changed);
    }

    private Tenant loadTenant(final UUID uniqueId) throws EntityNotFoundException {
        Optional<? extends Tenant> wrappedTenant = crudService.retrieve(uniqueId);

        if (!wrappedTenant.isPresent()) {
            LOG.warn("No tenant found with unique id: {}", uniqueId);

            throw new EntityNotFoundException(Tenant.class, uniqueId);
        }

        return wrappedTenant.get();
    }

    @Override
    public void changeName(final UUID uniqueId, final String name) throws EntityNotFoundException, DuplicateEntityException {
        Tenant tenant = loadTenant(uniqueId);

        Tenant changed = new TenantBuilder().withTenant(tenant).withName(name).build();

        crudService.update(changed);
        LOG.info("Changed name to '{}': {} -> {}", name, tenant, changed);
    }

    @Override
    public void delete(final UUID uniqueId) {
        crudService.delete(uniqueId);

        LOG.info("Deleted tenant with id: {}", uniqueId);
    }
}
