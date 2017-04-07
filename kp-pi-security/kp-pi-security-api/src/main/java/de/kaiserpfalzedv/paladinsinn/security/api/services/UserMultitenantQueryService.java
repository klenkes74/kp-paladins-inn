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

package de.kaiserpfalzedv.paladinsinn.security.api.services;

import java.util.Optional;
import java.util.UUID;

import de.kaiserpfalzedv.paladinsinn.commons.api.service.MultiTenant;
import de.kaiserpfalzedv.paladinsinn.commons.api.tenant.model.Tenant;
import de.kaiserpfalzedv.paladinsinn.security.api.model.User;

/**
 * The multitenant version of the user query service.
 *
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2017-04-07
 */
@MultiTenant
public interface UserMultitenantQueryService {
    /**
     * Retrieves the user for the given unique id.
     *
     * @param uniqueId The unique id of the user to be retrieved.
     *
     * @return The user with the given unique id.
     */
    Optional<? extends User> retrieve(UUID uniqueId);

    /**
     * Retrieves the user with the given user name. The user name is only unique within the same tenant so the tenant
     * is needed for this request.
     *
     * @param tenantId   The unique id of the tenant to find the user for.
     * @param uniqueName The unique user name of the user.
     *
     * @return The user with the unique name within the given tenant.
     */
    Optional<? extends User> retrieve(UUID tenantId, String uniqueName);

    /**
     * Retrieves the user with the given user name. The user name is only unique within the same tenant so the tenant
     * is needed for this request.
     *
     * @param tenant     The tenant to find the user for.
     * @param uniqueName The unique user name of the user.
     *
     * @return The user with the unique name within the given tenant.
     */
    Optional<? extends User> retrieve(Tenant tenant, String uniqueName);
}
