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
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import de.kaiserpfalzedv.paladinsinn.commons.tenant.model.Tenant;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2017-03-23
 */
@Entity(name = "tenant")
@Table(name = "TENANTS", uniqueConstraints = {
        @UniqueConstraint(name = "TENANT_UUID_UK", columnNames = "UNIQUE_ID"),
        @UniqueConstraint(name = "TENANT_KEY_UK", columnNames = "KEY"),
        @UniqueConstraint(name = "TENANT_NAME_UK", columnNames = "NAME")
})
@NamedQueries({
        @NamedQuery(name = "tenant-all", query = "select t from tenant t"),
        @NamedQuery(name = "tenant-by-uniqueid", query = "select t from tenant t where t.identifier.uniqueId=:uniqueId"),
        @NamedQuery(name = "tenant-by-name", query = "select t from tenant t where t.identifier.name=:name"),
        @NamedQuery(name = "tenant-by-key", query = "select t from tenant t where t.key=:key")
})
public class TenantJPA implements Tenant {

    @Id
    @GeneratedValue
    @SequenceGenerator(name = "tenant_seq", allocationSize = 1)
    private Long id;

    @Embedded
    private PaladinsInnJPAMetaData metaData;


    @Embedded
    private PaladinsInnJPAIdentifier identifier;

    @Column(name = "KEY", length = 25, unique = true, nullable = false)
    private String key;


    @Override
    public UUID getUniqueId() {
        return identifier.getUniqueId();
    }

    public void setUniqueId(UUID uniqueId) {
        checkIdentifier();
        identifier.setUniqueId(uniqueId);
    }

    private synchronized void checkIdentifier() {
        if (identifier == null) {
            identifier = new PaladinsInnJPAIdentifier();
        }
    }

    @Override
    public String getName() {
        checkIdentifier();
        return identifier.getName();
    }

    public void setName(String name) {
        identifier.setName(name);
    }

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
        return Objects.hash(metaData, identifier, getKey());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TenantJPA)) return false;
        TenantJPA tenantJPA = (TenantJPA) o;
        return Objects.equals(metaData, tenantJPA.metaData) &&
                Objects.equals(identifier, tenantJPA.identifier) &&
                Objects.equals(getKey(), tenantJPA.getKey());
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TenantJPA@").append(System.identityHashCode(this)).append("{");

        sb
                .append("id=").append(id)
                .append(", metaData=").append(metaData)
                .append(", identifier=").append(identifier)
                .append(", key='").append(key).append('\'');

        return sb.append('}').toString();
    }

    @Override
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
