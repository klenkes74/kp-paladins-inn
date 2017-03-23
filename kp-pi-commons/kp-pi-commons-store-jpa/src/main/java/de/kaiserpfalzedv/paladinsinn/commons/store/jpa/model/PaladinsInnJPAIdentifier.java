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

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2017-03-23
 */
@Embeddable
public class PaladinsInnJPAIdentifier implements Serializable {
    private static final long serialVersionUID = 2128841070049719224L;


    @Column(name = "UNIQUE_ID", unique = true, nullable = false)
    private UUID uniqueId;

    @Column(name = "NAME", length = 100, unique = true, nullable = false)
    private String Name;

    @Override
    public int hashCode() {
        return Objects.hash(getUniqueId(), getName());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PaladinsInnJPAIdentifier)) return false;
        PaladinsInnJPAIdentifier that = (PaladinsInnJPAIdentifier) o;
        return Objects.equals(getUniqueId(), that.getUniqueId()) &&
                Objects.equals(getName(), that.getName());
    }

    @Override
    public String toString() {
        return new StringBuilder("PaladinsInnJPAIdentifier{")
                .append(uniqueId)
                .append(", ").append(Name)
                .append('}').toString();
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(UUID uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
