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

package de.kaiserpfalzedv.paladinsinn.security.store.jpa.model;

import java.util.UUID;

import de.kaiserpfalzedv.paladinsinn.commons.api.persistence.NameableAbstractBuilder;
import de.kaiserpfalzedv.paladinsinn.commons.api.tenant.model.DefaultTenant;
import de.kaiserpfalzedv.paladinsinn.commons.api.tenant.model.Tenant;
import de.kaiserpfalzedv.paladinsinn.security.api.model.Entitlement;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2017-03-11
 */
public class EntitlementJPABuilder extends NameableAbstractBuilder<EntitlementJPA> {
    private Tenant tenant;
    private UUID tenantId;


    @SuppressWarnings("deprecation")
    @Override
    public EntitlementJPA build() {
        setDefaultsIfNeeded();
        validate();

        EntitlementJPA result = new EntitlementJPA(
                uniqueId, version,
                tenantId,
                name,
                created, modified
        );

        return result;
    }

    @Override
    public void validateDuringBuild() {
        validate();
    }

    protected void setDefaultsIfNeeded() {
        super.setDefaultsIfNeeded();

        if (tenant == null) {
            tenant = DefaultTenant.INSTANCE;
        }

        if (tenantId == null) {
            tenantId = tenant.getUniqueId();
        }
    }

    @Override
    public boolean validate() {
        return true;
    }

    public EntitlementJPABuilder withUniqueId(final UUID uniqueId) {
        return (EntitlementJPABuilder) super.withUniqueId(uniqueId);
    }

    public EntitlementJPABuilder withName(final String name) {
        return (EntitlementJPABuilder) super.withName(name);
    }

    public EntitlementJPABuilder withEntitlement(final Entitlement data) {
        withUniqueId(data.getUniqueId());
        withName(data.getName());

        if (data instanceof EntitlementJPA) {
            EntitlementJPA e = (EntitlementJPA) data;

            withVersion(e.getVersion());
            withTenantId(e.getTenantId());
            withCreated(e.getCreated());
            withModified(e.getModified());
        }

        return this;
    }

    public EntitlementJPABuilder withTenantId(final UUID tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    public EntitlementJPABuilder withTenant(final Tenant tenant) {
        this.tenant = tenant;
        this.tenantId = tenant.getUniqueId();
        return this;
    }
}
