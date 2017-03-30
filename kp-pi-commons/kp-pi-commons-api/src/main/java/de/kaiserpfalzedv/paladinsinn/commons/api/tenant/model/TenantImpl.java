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

package de.kaiserpfalzedv.paladinsinn.commons.api.tenant.model;

import java.util.UUID;

import de.kaiserpfalzedv.paladinsinn.commons.api.persistence.IdentifiableAbstractImpl;
import de.kaiserpfalzedv.paladinsinn.commons.api.persistence.Nameable;

/**
 * @author rlichti {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 2016-03-20
 */
public class TenantImpl extends IdentifiableAbstractImpl implements Tenant, Nameable {
    private static final long serialVersionUID = 111969478658417259L;
    
    private String key;


    TenantImpl(final UUID uniqueId, final String key, final String name) {
        super(uniqueId, name);

        this.key = key;
    }


    public String getKey() {
        return key;
    }

    @Deprecated
    public void setKey(final String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return new StringBuilder("TenantImpl@").append(System.identityHashCode(this)).append("{")
                                               .append(getUniqueId())
                                               .append(", ").append(key)
                                               .append(", ").append(getName())
                                               .append("}").toString();
    }
}
