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

package de.kaiserpfalzEdv.paladinsInn.commons.impl;

import de.kaiserpfalzEdv.paladinsInn.commons.IdentityWritable;

import java.util.UUID;

/**
 * A base implementation for all identifable classes (including writable interface).
 *
 * @author rlichti {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 2016-03-20
 */
public class AbstractIdentityBase implements IdentityWritable {
    private UUID id;

    public AbstractIdentityBase() {
        this.id = UUID.randomUUID();
    }

    public AbstractIdentityBase(final UUID id) {
        this.id = id;
    }

    @Override
    public void setIdentity(UUID id) {
        this.id = id;
    }

    @Override
    public UUID getIdentifier() {
        return id;
    }
}
