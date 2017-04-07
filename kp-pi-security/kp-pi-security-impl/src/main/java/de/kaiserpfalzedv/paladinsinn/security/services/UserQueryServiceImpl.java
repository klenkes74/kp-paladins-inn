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

import de.kaiserpfalzedv.paladinsinn.commons.api.service.SingleTenant;
import de.kaiserpfalzedv.paladinsinn.commons.api.service.WorkerService;
import de.kaiserpfalzedv.paladinsinn.commons.api.tenant.model.DefaultTenant;
import de.kaiserpfalzedv.paladinsinn.security.api.model.User;
import de.kaiserpfalzedv.paladinsinn.security.api.services.UserMultitenantQueryService;
import de.kaiserpfalzedv.paladinsinn.security.api.services.UserQueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This implementation of the single tenant query service only is a decorator around the multi tenant version
 * {@link UserMultitenantQueryService} using the {@link DefaultTenant#INSTANCE} as the tenant.
 *
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2017-04-07
 */
@WorkerService
@SingleTenant
public class UserQueryServiceImpl implements UserQueryService {
    private static final Logger LOG = LoggerFactory.getLogger(UserQueryServiceImpl.class);

    private UserMultitenantQueryService multitenantService;

    @Inject
    public UserQueryServiceImpl(
            final UserMultitenantQueryService multitenantService
    ) {
        this.multitenantService = multitenantService;

        LOG.debug("***** Created: {}", this);
        LOG.debug("*   *    multitenant service: {}", this.multitenantService);
    }


    @Override
    public Optional<? extends User> retrieve(UUID uniqueId) {
        return multitenantService.retrieve(uniqueId);
    }

    @Override
    public Optional<? extends User> retrieve(String uniqueName) {
        return multitenantService.retrieve(DefaultTenant.INSTANCE, uniqueName);
    }
}
