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
import java.time.ZoneId;
import java.util.Objects;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.LockModeType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Version;

import de.kaiserpfalzedv.paladinsinn.commons.api.persistence.Modifiable;
import de.kaiserpfalzedv.paladinsinn.commons.api.persistence.MultiTenantable;
import de.kaiserpfalzedv.paladinsinn.security.api.model.Entitlement;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2017-03-26
 */
@Entity(name = "entitlement")
@Table(name = "ENTITLEMENTS")
@NamedQueries({
        @NamedQuery(
                name = "entitlement-by-name",
                query = "SELECT e FROM entitlement e WHERE e.tenantId=:tenant AND e.name=:name",
                lockMode = LockModeType.READ
        ),
        @NamedQuery(
                name = "entitlements",
                query = "SELECT e FROM entitlement e WHERE e.tenantId=:tenant",
                lockMode = LockModeType.READ
        )
})
public class EntitlementJPA implements Entitlement, MultiTenantable, Modifiable {
    public static final ZoneId UTC = ZoneId.of("UTC");
    private static final long serialVersionUID = -3923846511922278191L;
    @Id
    @Column(name = "ID", unique = true, nullable = false, updatable = false)
    private UUID uniqueId;

    @Column(name = "TENANT_ID", nullable = false)
    private UUID tenantId;

    @SuppressWarnings("unused") // managd by JPA
    @Version
    @Column(name = "VERSION", nullable = false)
    private long version;

    @Column(name = "NAME", length = 200, unique = true, nullable = false)
    private String name;

    @Column(name = "CREATED", nullable = false)
    private OffsetDateTime created = OffsetDateTime.now(UTC);
    @Column(name = "MODIFIED", nullable = false)
    private OffsetDateTime changed = created;

    @Override
    public long getVersion() {
        return version;
    }

    @Override
    public OffsetDateTime getCreated() {
        return created;
    }

    @Override
    public OffsetDateTime getChanged() {
        return changed;
    }

    @Deprecated
    public void setChanged(final OffsetDateTime changed) {
        this.changed = changed;
    }

    @Deprecated
    public void setCreated(final OffsetDateTime created) {
        this.created = created;
    }

    public UUID getTenantId() {
        return tenantId;
    }

    @Deprecated
    public void setTenantId(final UUID tenantId) {
        this.tenantId = tenantId;
    }

    @Deprecated
    @PrePersist
    public void updateChanged() {
        changed = OffsetDateTime.now(UTC);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUniqueId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EntitlementJPA)) return false;
        EntitlementJPA that = (EntitlementJPA) o;
        return Objects.equals(getUniqueId(), that.getUniqueId());
    }

    @Override
    public UUID getUniqueId() {
        return uniqueId;
    }

    @Override
    public String getName() {
        return name;
    }

    @Deprecated
    public void setName(final String name) {
        this.name = name;
    }

    @Deprecated
    public void setUniqueId(final UUID uniqueId) {
        this.uniqueId = uniqueId;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("EntitlementJPA@")
                .append(System.identityHashCode(this)).append('{');

        sb
                .append(uniqueId);

        if (version != 0) {
            sb.append('/').append(version);
        }

        if (tenantId != null) {
            sb.append(", tenant=").append(tenantId);
        }

        sb
                .append(name)
                .append(", created=").append(created);

        if (!created.equals(changed)) {
            sb.append(", changed=").append(changed);
        }

        return sb.append('}').toString();
    }
}
