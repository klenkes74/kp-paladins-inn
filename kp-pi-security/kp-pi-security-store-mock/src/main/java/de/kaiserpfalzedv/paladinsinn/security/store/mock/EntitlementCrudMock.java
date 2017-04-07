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

package de.kaiserpfalzedv.paladinsinn.security.store.mock;

import javax.enterprise.inject.Alternative;
import javax.inject.Inject;

import de.kaiserpfalzedv.paladinsinn.commons.api.service.MockService;
import de.kaiserpfalzedv.paladinsinn.commons.api.service.SingleTenant;
import de.kaiserpfalzedv.paladinsinn.commons.api.tenant.model.DefaultTenant;
import de.kaiserpfalzedv.paladinsinn.commons.api.tenant.model.Tenant;
import de.kaiserpfalzedv.paladinsinn.commons.mock.AbstractCrudMock;
import de.kaiserpfalzedv.paladinsinn.security.api.model.Entitlement;
import de.kaiserpfalzedv.paladinsinn.security.api.store.EntitlementCrudService;
import de.kaiserpfalzedv.paladinsinn.security.api.store.EntitlementMultitenantCrudService;

/**
 * The tenant-less version of the mock service uses the multi-tenant {@link UserMultitenantCrudMock} with the
 * {@link DefaultTenant} as tenant.
 *
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2017-03-19
 */
@Alternative
@MockService
@SingleTenant
public class EntitlementCrudMock extends AbstractCrudMock<Entitlement> implements EntitlementCrudService {

    /**
     * @param defaultTenant         The tenant to use when calling the multi tenant service.
     * @param tenantCrudService The multi tenant user CRUD service used as backend to this single tenant
     *                              implementation.
     */
    @Inject
    public EntitlementCrudMock(
            final Tenant defaultTenant,
            final EntitlementMultitenantCrudService tenantCrudService
    ) {
        super(defaultTenant, tenantCrudService);
    }
}
