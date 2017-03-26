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

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;

import de.kaiserpfalzedv.paladinsinn.commons.api.persistence.IdentifiableAbstractBuilder;
import de.kaiserpfalzedv.paladinsinn.commons.api.tenant.model.DefaultTenant;
import de.kaiserpfalzedv.paladinsinn.commons.api.tenant.model.Tenant;
import de.kaiserpfalzedv.paladinsinn.security.api.model.Entitlement;
import de.kaiserpfalzedv.paladinsinn.security.api.model.Role;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2017-03-11
 */
public class RoleJPABuilder extends IdentifiableAbstractBuilder<RoleJPA> {
    private final HashSet<Role> roles = new HashSet<>();
    private final HashSet<Entitlement> entitlements = new HashSet<>();
    private OffsetDateTime created;
    private OffsetDateTime changed;
    private Tenant tenant;
    private UUID tenantId;

    @SuppressWarnings("deprecation")
    @Override
    public RoleJPA build() {
        setDefaultsIfNeeded();
        validate();

        RoleJPA result = new RoleJPA();
        result.setUniqueId(uniqueId);
        result.setTenantId(tenantId);
        result.setName(name);
        result.setCreated(created);
        result.setModified(changed);
        result.setRoles(convertRoles());
        result.setEntitlements(convertEntitlements());

        return result;
    }

    @Override
    public void validateDuringBuild() {
        validate();
    }

    @Override
    public boolean validate() {
        if (created == null) {
            created = OffsetDateTime.now(EntitlementJPA.UTC);
        }

        if (changed == null) {
            changed = created;
        }

        if (tenant == null) {
            tenant = DefaultTenant.INSTANCE;
        }

        if (tenantId == null) {
            tenantId = tenant.getUniqueId();
        }

        return true;
    }

    public RoleJPABuilder withUniqueId(final UUID uniqueId) {
        return (RoleJPABuilder) super.withUniqueId(uniqueId);
    }

    public RoleJPABuilder withName(final String name) {
        return (RoleJPABuilder) super.withName(name);
    }

    private HashSet<RoleJPA> convertRoles() {
        HashSet<RoleJPA> result = new HashSet<>(roles.size());

        roles.forEach(r -> {
            try {
                result.add((RoleJPA) r);
            } catch (ClassCastException e) {
                result.add(new RoleJPABuilder().withRole(r).build());
            }
        });

        return result;
    }

    private HashSet<EntitlementJPA> convertEntitlements() {
        HashSet<EntitlementJPA> result = new HashSet<>(entitlements.size());

        entitlements.forEach(p -> {
            try {
                result.add((EntitlementJPA) p);
            } catch (ClassCastException e) {
                result.add(new EntitlementJPABuilder().withEntitlement(p).build());
            }
        });

        return result;
    }

    public RoleJPABuilder withRole(final Role data) {
        withUniqueId(data.getUniqueId());
        withName(data.getName());

        if (data instanceof RoleJPA) {
            withTenantId(((RoleJPA) data).getTenantId());
            withCreated(((RoleJPA) data).getCreated());
            withModified(((RoleJPA) data).getModified());
        }

        return this;
    }

    public RoleJPABuilder withCreated(final OffsetDateTime created) {
        this.created = created;
        return this;
    }

    public RoleJPABuilder withModified(final OffsetDateTime changed) {
        this.changed = changed;
        return this;
    }

    public RoleJPABuilder withTenantId(final UUID tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    public RoleJPABuilder withTenant(final Tenant tenant) {
        this.tenant = tenant;
        this.tenantId = tenant.getUniqueId();
        return this;
    }

    public RoleJPABuilder clearRoles() {
        this.roles.clear();
        return this;
    }

    public RoleJPABuilder addRoles(final Collection<? extends Role> roles) {
        if (roles != null) {
            this.roles.addAll(roles);
        }
        return this;
    }

    public RoleJPABuilder addRole(final Role role) {
        this.roles.add(role);
        return this;
    }

    public RoleJPABuilder removeRole(final Role role) {
        this.roles.remove(role);
        return this;
    }


    public RoleJPABuilder clearEntitlements() {
        this.entitlements.clear();
        return this;
    }

    public RoleJPABuilder addEntitlements(final Collection<? extends Entitlement> entitlements) {
        if (entitlements != null) {
            this.entitlements.addAll(entitlements);
        }
        return this;
    }

    public RoleJPABuilder addEntitlement(final Entitlement entitlement) {
        this.entitlements.add(entitlement);
        return this;
    }

    public RoleJPABuilder removeEntitlement(final Entitlement entitlement) {
        this.entitlements.remove(entitlement);
        return this;
    }
}
