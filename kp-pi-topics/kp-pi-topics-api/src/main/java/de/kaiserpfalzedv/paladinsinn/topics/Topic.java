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

package de.kaiserpfalzedv.paladinsinn.topics;

import de.kaiserpfalzedv.paladinsinn.commons.Identifiable;
import de.kaiserpfalzedv.paladinsinn.commons.Nameable;
import de.kaiserpfalzedv.paladinsinn.security.Maintainable;
import de.kaiserpfalzedv.paladinsinn.security.tenant.TenantHolding;

import java.io.Serializable;
import java.util.List;

/**
 * A single topic.
 * @author rlichti {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 2016-03-18
 */
public interface Topic extends Identifiable, Nameable, TenantHolding, Maintainable, Serializable {
    /**
     * @return The parent topic.
     */
    Topic getParent();

    /**
     * @return list of children topics of the current topic.
     */
    List<Topic> getChildren();
}
