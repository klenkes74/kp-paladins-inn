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

package de.kaiserpfalzEdv.paladinsInn.security.impl;

import de.kaiserpfalzEdv.paladinsInn.commons.IdentityWritable;
import de.kaiserpfalzEdv.paladinsInn.commons.NameWritable;
import de.kaiserpfalzEdv.paladinsInn.commons.impl.AbstractNamedAndIdentifiedBase;
import de.kaiserpfalzEdv.paladinsInn.security.Tenant;
import de.kaiserpfalzEdv.paladinsInn.security.User;

import java.util.UUID;

/**
 * @author rlichti {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 2016-03-20
 */
public class TenantImpl extends AbstractNamedAndIdentifiedBase implements Tenant, IdentityWritable, NameWritable {
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
