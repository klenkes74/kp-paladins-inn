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
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import de.kaiserpfalzedv.paladinsinn.commons.api.persistence.Modifiable;
import de.kaiserpfalzedv.paladinsinn.commons.api.persistence.MultiTenantable;
import de.kaiserpfalzedv.paladinsinn.commons.jpa.NamedTenantMetaData;
import de.kaiserpfalzedv.paladinsinn.security.api.model.Entitlement;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2017-03-26
 */
@Entity(name = "entitlement")
@Table(
        name = "ENTITLEMENTS",
        uniqueConstraints = {
                @UniqueConstraint(name = "ENTITLEMENTS_UUID_UK", columnNames = {"ID"}),
                @UniqueConstraint(name = "ENTITLEMENTS_NAME_UK", columnNames = {"TENANT_ID", "NAME"})
        }
)
@NamedQueries({
        @NamedQuery(
                name = "entitlement-by-name",
                query = "SELECT e FROM entitlement e WHERE e.tenantId=:tenant AND e.name=:name"
        ),
        @NamedQuery(
                name = "entitlements",
                query = "SELECT e FROM entitlement e WHERE e.tenantId=:tenant"
        )
})
public class EntitlementJPA extends NamedTenantMetaData implements Entitlement, MultiTenantable, Modifiable {
    private static final long serialVersionUID = -8358581565252632682L;

    @Deprecated
    public EntitlementJPA() {}

    public EntitlementJPA(
            final UUID uniqueId, final Long version,
            final UUID tenantId,
            final String name,
            final OffsetDateTime created, final OffsetDateTime modified
    ) {
        super(uniqueId, version, tenantId, name, created, modified);
    }
}
