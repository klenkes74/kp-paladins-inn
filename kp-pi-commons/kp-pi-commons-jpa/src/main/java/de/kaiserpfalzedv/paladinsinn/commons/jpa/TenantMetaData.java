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

package de.kaiserpfalzedv.paladinsinn.commons.jpa;

import java.time.OffsetDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import de.kaiserpfalzedv.paladinsinn.commons.api.persistence.MultiTenantable;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2017-03-30
 */
@MappedSuperclass
public class TenantMetaData extends MetaData implements MultiTenantable {
    private static final long serialVersionUID = -6696944422940938945L;

    @Column(name = "TENANT_ID", nullable = false)
    private UUID tenantId;


    @Deprecated
    public TenantMetaData() {}

    public TenantMetaData(
            final UUID uniqueId, final Long version,
            final UUID tenantId,
            final OffsetDateTime created, final OffsetDateTime modified
    ) {
        super(uniqueId, version, created, modified);

        setTenantId(tenantId);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("[");

        result
                .append(getTenantId()).append(':').append(getUniqueId()).append('[').append(getVersion()).append(']')
                .append(", created=").append(getCreated());

        if (!getCreated().equals(getModified())) {
            result.append(", changed=").append(getModified());
        }

        return result
                .append(']').toString();
    }

    @Override
    public UUID getTenantId() {
        return tenantId;
    }

    protected void setTenantId(final UUID tenantId) {
        this.tenantId = tenantId;
    }
}
