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

import java.time.OffsetDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import de.kaiserpfalzedv.paladinsinn.commons.api.tenant.model.Tenant;
import de.kaiserpfalzedv.paladinsinn.commons.jpa.NamedMetaData;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2017-03-23
 */
@Entity(name = "tenant")
@Table(
        name = "TENANTS",
        uniqueConstraints = {
                @UniqueConstraint(name = "TENANT_UUID_UK", columnNames = "ID"),
                @UniqueConstraint(name = "TENANT_KEY_UK", columnNames = "KEY"),
                @UniqueConstraint(name = "TENANT_NAME_UK", columnNames = "NAME")
        }
)
@NamedQueries({
        @NamedQuery(name = "tenant-all", query = "select t from tenant t"),
        @NamedQuery(name = "tenant-by-uniqueid", query = "select t from tenant t where t.uniqueId=:uniqueId"),
        @NamedQuery(name = "tenant-by-name", query = "select t from tenant t where t.name=:name"),
        @NamedQuery(name = "tenant-by-key", query = "select t from tenant t where t.key=:key")
})
public class TenantJPA extends NamedMetaData implements Tenant {
    private static final long serialVersionUID = -7394966656438554619L;

    @Column(name = "KEY", length = 5, unique = true, nullable = false)
    private String key;


    @Deprecated
    public TenantJPA() {}

    public TenantJPA(
            final UUID id, final Long version,
            final String key, final String name,
            final OffsetDateTime created, final OffsetDateTime modified
    ) {
        super(id, version, name, created, modified);

        setKey(key);
    }


    @Override
    public String getKey() {
        return key;
    }

    public void setKey(final String key) {
        this.key = key;
    }


    @Override
    public String toString() {
        return new StringBuilder("TenantJPA@").append(System.identityHashCode(this)).append('{')

                                              .append(super.toString())
                                              .append(", key='").append(getKey()).append('\'')

                                              .append('}').toString();
    }
}
