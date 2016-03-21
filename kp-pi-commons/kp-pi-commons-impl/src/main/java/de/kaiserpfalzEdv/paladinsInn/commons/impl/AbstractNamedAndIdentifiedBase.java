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

import de.kaiserpfalzEdv.paladinsInn.commons.NameWritable;

import java.util.UUID;

/**
 * A base class for identifiable and nameable objects.
 *
 * @author rlichti {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 2016-03-20
 */
public class AbstractNamedAndIdentifiedBase extends AbstractIdentityBase implements NameWritable {
    private String name;


    public AbstractNamedAndIdentifiedBase() {
        super(UUID.randomUUID());
    }

    public AbstractNamedAndIdentifiedBase(final String name) {
        this(UUID.randomUUID(), name);
    }

    public AbstractNamedAndIdentifiedBase(final UUID id, final String name) {
        super(id);

        this.name = name;
    }


    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
