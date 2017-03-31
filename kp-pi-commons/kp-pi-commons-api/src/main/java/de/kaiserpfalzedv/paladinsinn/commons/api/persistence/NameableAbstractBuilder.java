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

package de.kaiserpfalzedv.paladinsinn.commons.api.persistence;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.UUID;

import de.kaiserpfalzedv.paladinsinn.commons.api.BuilderValidationException;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2017-03-11
 */
public abstract class NameableAbstractBuilder<T extends Nameable> {
    protected final ArrayList<String> validationErrors = new ArrayList<>(5);
    protected UUID uniqueId;
    protected Long version;
    protected String name;

    protected OffsetDateTime created;
    protected OffsetDateTime modified;


    public abstract T build() throws BuilderValidationException;

    public abstract void validateDuringBuild();

    public abstract boolean validate();

    protected void setDefaultsIfNeeded() {
        if (uniqueId == null) {
            uniqueId = UUID.randomUUID();
        }

        if (name == null || name.isEmpty()) {
            name = uniqueId.toString();
        }

        if (created == null) {
            created = OffsetDateTime.now(ZoneId.of("UTC"));
        }

        if (modified == null) {
            modified = created;
        }
    }

    public NameableAbstractBuilder<T> withUniqueId(final UUID uniqueId) {
        this.uniqueId = uniqueId;
        return this;
    }

    public NameableAbstractBuilder<T> withName(final String name) {
        this.name = name;
        return this;
    }

    public NameableAbstractBuilder<T> withVersion(final Long version) {
        this.version = version;
        return this;
    }

    public NameableAbstractBuilder<T> withCreated(final OffsetDateTime created) {
        this.created = created;
        return this;
    }

    public NameableAbstractBuilder<T> withModified(final OffsetDateTime modified) {
        this.modified = modified;
        return this;
    }
}
