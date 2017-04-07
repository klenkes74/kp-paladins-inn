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

import java.util.Optional;
import java.util.UUID;

import javax.inject.Inject;

import de.kaiserpfalzedv.paladinsinn.commons.api.service.MultiTenant;
import de.kaiserpfalzedv.paladinsinn.commons.api.service.WorkerService;
import de.kaiserpfalzedv.paladinsinn.commons.api.tenant.model.DefaultTenant;
import de.kaiserpfalzedv.paladinsinn.commons.api.tenant.model.Tenant;
import de.kaiserpfalzedv.paladinsinn.commons.api.tenant.service.TenantQueryService;
import de.kaiserpfalzedv.paladinsinn.security.api.model.User;
import de.kaiserpfalzedv.paladinsinn.security.api.services.UserMultitenantQueryService;
import de.kaiserpfalzedv.paladinsinn.security.api.store.UserMultitenantCrudService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2017-04-07
 */
@MultiTenant
@WorkerService
public class UserMultiTenantQueryServiceImpl implements UserMultitenantQueryService {
    private static final Logger LOG = LoggerFactory.getLogger(UserMultiTenantQueryServiceImpl.class);

    private TenantQueryService tenantService;
    private UserMultitenantCrudService crudService;


    @Inject
    public UserMultiTenantQueryServiceImpl(
            final UserMultitenantCrudService crudService,
            final TenantQueryService tenantService
    ) {
        this.crudService = crudService;
        this.tenantService = tenantService;

        LOG.debug("***** Created: {}", this);
        LOG.debug("*   *    crud user service: {}", this.crudService);
        LOG.debug("*   *    tenant query service: {}", this.tenantService);
    }


    @Override
    public Optional<? extends User> retrieve(UUID uniqueId) {
        return crudService.retrieve(DefaultTenant.INSTANCE, uniqueId);
    }

    @Override
    public Optional<? extends User> retrieve(UUID tenantId, String uniqueName) {
        Optional<? extends Tenant> wrappedTenant = tenantService.retrieve(tenantId);

        if (!wrappedTenant.isPresent()) {
            LOG.info("Tried to retrieve user '{}' from non-existing tenant (there is no tenant with unique id of {}",
                     uniqueName, tenantId
            );

            return Optional.empty();
        }

        return retrieve(wrappedTenant.get(), uniqueName);
    }

    @Override
    public Optional<? extends User> retrieve(Tenant tenant, String uniqueName) {
        return crudService.retrieve(tenant, uniqueName);
    }
}
