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

import de.kaiserpfalzedv.paladinsinn.commons.api.paging.Page;
import de.kaiserpfalzedv.paladinsinn.commons.api.paging.PageRequest;
import de.kaiserpfalzedv.paladinsinn.commons.api.tenant.model.Tenant;
import de.kaiserpfalzedv.paladinsinn.commons.api.tenant.service.TenantQueryService;
import de.kaiserpfalzedv.paladinsinn.commons.api.tenant.store.TenantCrudService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2017-04-07
 */
public class TenantQueryServiceImpl implements TenantQueryService {
    private static final Logger LOG = LoggerFactory.getLogger(TenantQueryServiceImpl.class);


    private TenantCrudService crudService;


    public TenantQueryServiceImpl(final TenantCrudService crudService) {
        this.crudService = crudService;

        LOG.debug("***** Created: {}", this);
        LOG.debug("*   *    crud service: {}", this.crudService);
    }

    @Override
    public Optional<? extends Tenant> retrieve(UUID uniqueId) {
        return crudService.retrieve(uniqueId);
    }

    @Override
    public Optional<? extends Tenant> retrieve(String key) {
        return crudService.retrieve(key);
    }

    @Override
    public Optional<? extends Tenant> retrieveByFullName(String fullName) {
        return crudService.retrieveByFullName(fullName);
    }

    @Override
    public Page<? extends Tenant> retrieve(PageRequest pageRequest) {
        return crudService.retrieve(pageRequest);
    }
}
