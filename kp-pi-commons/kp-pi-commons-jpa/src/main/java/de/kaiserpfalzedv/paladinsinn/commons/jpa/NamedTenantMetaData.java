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

import de.kaiserpfalzedv.paladinsinn.commons.api.persistence.Nameable;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2017-03-30
 */
@MappedSuperclass
public class NamedTenantMetaData extends TenantMetaData implements Nameable {
    private static final long serialVersionUID = -8136187553822517852L;


    @Column(name = "NAME", length = 200, nullable = false)
    private String name;


    @Deprecated
    public NamedTenantMetaData() {}

    public NamedTenantMetaData(
            final UUID uniqueId, final Long version,
            final UUID tenantId,
            final String name,
            final OffsetDateTime created, final OffsetDateTime modified
    ) {
        super(uniqueId, version, tenantId, created, modified);

        setName(name);
    }

    public String toString() {
        return super.toString()
                + ", name='"
                + getName()
                + "'";
    }

    @Override
    public String getName() {
        return name;
    }

    protected void setName(final String name) {
        this.name = name;
    }
}
