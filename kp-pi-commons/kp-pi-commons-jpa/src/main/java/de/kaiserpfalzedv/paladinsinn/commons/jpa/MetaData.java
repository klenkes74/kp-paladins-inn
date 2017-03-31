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
import java.time.ZoneId;
import java.util.Objects;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.Version;

import de.kaiserpfalzedv.paladinsinn.commons.api.persistence.Identifiable;
import de.kaiserpfalzedv.paladinsinn.commons.api.persistence.Modifiable;
import de.kaiserpfalzedv.paladinsinn.commons.api.persistence.Versionable;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2017-03-23
 */
@MappedSuperclass
public class MetaData implements Identifiable, Versionable, Modifiable {
    private static final long serialVersionUID = 2143714435726971357L;


    /**
     * The default {@link ZoneId} for which all date times are created.
     * That is the <a href="https://en.wikipedia.org/wiki/Coordinated_Universal_Time">UTC</a> as currently defined in
     * document ITU-R TF.460-6.
     */
    public static final ZoneId ZONE_ID = ZoneId.of("UTC");


    @Id
    @Column(name = "ID", nullable = false, updatable = false)
    private volatile UUID uniqueId;

    @Version
    @Column(name = "VERSION")
    private Long version;


    @Column(name = "CREATED", nullable = false, updatable = false)
    private volatile OffsetDateTime created;

    @Column(name = "MODIFIED", nullable = false)
    private volatile OffsetDateTime modified;


    @Deprecated
    public MetaData() {}


    /**
     * The full constructor providing all data.
     *
     * @param uniqueId The unique Id of this entity.
     * @param version  The JPA version of this entity.
     * @param created  The timestamp of the creation of this entity.
     * @param modified The timestamp of the last modification of this entity.
     */
    public MetaData(
            final UUID uniqueId, final Long version,
            final OffsetDateTime created, final OffsetDateTime modified
    ) {
        setUniqueId(uniqueId);
        setVersion(version);
        setCreated(created);
        setModified(modified);
    }


    @PrePersist
    public void updateData() {
        getUniqueId();                                  // initialize the unique id
        getCreated();                                   // initialize the creation timestamp if not set already
        setModified(OffsetDateTime.now(ZONE_ID));       // save new modification timestamp
    }


    @Override
    public UUID getUniqueId() {
        UUID tmp = uniqueId;

        if (tmp == null) {
            synchronized (this) {
                tmp = uniqueId;
                if (tmp == null) {
                    uniqueId = tmp = UUID.randomUUID();
                }
            }
        }

        return tmp;
    }

    protected synchronized void setUniqueId(final UUID uniqueId) {
        this.uniqueId = uniqueId;
    }


    @Override
    public Long getVersion() {
        return version;
    }

    protected void setVersion(final Long version) {
        this.version = version;
    }

    
    @Override
    public OffsetDateTime getCreated() {
        OffsetDateTime tmp = created;

        if (tmp == null) {
            synchronized (this) {
                tmp = created;
                if (tmp == null) {
                    this.created = tmp = OffsetDateTime.now(ZONE_ID);
                }
            }
        }
        return tmp;
    }

    protected void setCreated(final OffsetDateTime created) {
        this.created = created;
    }

    @Override
    public OffsetDateTime getModified() {
        OffsetDateTime tmp = modified;

        if (tmp == null) {
            synchronized (this) {
                tmp = modified;
                if (tmp == null) {
                    this.modified = getCreated();
                }
            }
        }
        return tmp;
    }

    protected void setModified(final OffsetDateTime modified) {
        this.modified = modified;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCreated(), getModified());
    }

    @Override
    public boolean equals(Object o) {
        return
                this == o
                        || o instanceof MetaData
                        && Objects.equals(uniqueId, ((MetaData) o).getUniqueId());
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("[");

        result
                .append(getUniqueId()).append('[').append(getVersion()).append(']')
                .append(", created=").append(getCreated());

        if (!getCreated().equals(getModified())) {
            result.append(", changed=").append(getModified());
        }

        return result
                .append(']').toString();
    }
}
