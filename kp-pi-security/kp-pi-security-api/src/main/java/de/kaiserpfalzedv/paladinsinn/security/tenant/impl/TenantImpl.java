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

package de.kaiserpfalzedv.paladinsinn.security.tenant.impl;

import java.util.UUID;

import de.kaiserpfalzedv.paladinsinn.commons.Identifiable;
import de.kaiserpfalzedv.paladinsinn.security.access.User;
import de.kaiserpfalzedv.paladinsinn.commons.impl.IdentifiableAbstractImpl;
import de.kaiserpfalzedv.paladinsinn.security.tenant.Tenant;

/**
 * @author rlichti {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 2016-03-20
 */
public class TenantImpl extends IdentifiableAbstractImpl implements Tenant, Identifiable {
    private User maintainer;

    public TenantImpl(final UUID id, final String name, final User maintainer) {
        super(id, name);

        this.maintainer = maintainer;
    }

    @Override
    public User getMaintainer() {
        return maintainer;
    }
}
