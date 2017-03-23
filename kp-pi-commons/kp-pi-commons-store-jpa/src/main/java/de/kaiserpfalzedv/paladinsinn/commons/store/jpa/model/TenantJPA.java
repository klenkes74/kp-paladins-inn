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

package de.kaiserpfalzedv.paladinsinn.commons.store.jpa.model;

import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;

import de.kaiserpfalzedv.paladinsinn.commons.tenant.model.Tenant;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2017-03-23
 */
@Entity(name = "tenant")
@Table(
        schema = "TENANT",
        catalog = "TENANT",
        name = "TENANTS",
        uniqueConstraints = {
                @UniqueConstraint(name = "TENANT_UUID_UK", columnNames = "UNIQUE_ID"),
                @UniqueConstraint(name = "TENANT_KEY_UK", columnNames = "KEY"),
                @UniqueConstraint(name = "TENANT_NAME_UK", columnNames = "NAME")
        }
)
@NamedQueries({
        @NamedQuery(name = "tenant-all", query = "select t from tenant t"),
        @NamedQuery(name = "tenant-by-uniqueid", query = "select t from tenant t where t.identifier.uniqueId=:uniqueId"),
        @NamedQuery(name = "tenant-by-name", query = "select t from tenant t where t.identifier.name=:name"),
        @NamedQuery(name = "tenant-by-key", query = "select t from tenant t where t.key=:key")
})
public class TenantJPA implements Tenant {
    private static final long serialVersionUID = 8991153230615920093L;


    @Id
    @Column(name = "ID", unique = true, nullable = false, updatable = false)
    private UUID uniqueId;

    @Column(name = "NAME", length = 200, unique = true, nullable = false)
    private String name;

    @Column(name = "KEY", length = 25, unique = true, nullable = false)
    private String key;


    @Version
    private Long version;

    @Embedded
    private PaladinsInnJPAMetaData metaData;

    public ZonedDateTime getCreated() {
        checkMetaData();
        return metaData.getCreated();
    }

    private synchronized void checkMetaData() {
        if (metaData == null) {
            metaData = new PaladinsInnJPAMetaData();
        }
    }

    public ZonedDateTime getChanged() {
        checkMetaData();
        return metaData.getChanged();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUniqueId(), getName(), getKey());
    }

    @Override
    public UUID getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(UUID uniqueId) {
        this.uniqueId = uniqueId;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TenantJPA)) return false;
        TenantJPA tenantJPA = (TenantJPA) o;
        return Objects.equals(getUniqueId(), tenantJPA.getUniqueId()) &&
                Objects.equals(getName(), tenantJPA.getName()) &&
                Objects.equals(getKey(), tenantJPA.getKey());
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TenantJPA@");

        sb
                .append(System.identityHashCode(this))
                .append("{id=").append(getUniqueId())
                .append(", name=").append(getName())
                .append(", key='").append(getKey()).append('\'')
                .append(", metaData=").append(metaData);

        return sb.append('}').toString();
    }
}
