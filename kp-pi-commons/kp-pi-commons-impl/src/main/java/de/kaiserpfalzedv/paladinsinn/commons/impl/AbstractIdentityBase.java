/*
 * Copyright 2016 Kaiserpfalz EDV-Service, Roland T. Lichti
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

package de.kaiserpfalzedv.paladinsinn.commons.impl;

import java.util.UUID;

import de.kaiserpfalzedv.paladinsinn.commons.Identifiable;

/**
 * A base implementation for all identifable classes (including writable interface).
 *
 * @author rlichti {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 2016-03-20
 */
public abstract class AbstractIdentityBase implements Identifiable {
    private UUID id;
    private String name;

    public AbstractIdentityBase() {
        this(UUID.randomUUID());
    }


    public AbstractIdentityBase(final UUID id) {
        this(id, id.toString());
    }

    public AbstractIdentityBase(final UUID id, final String name) {
        this.id = id;
        this.name = name;
    }

    
    @Override
    public UUID getUniqueId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }
}
