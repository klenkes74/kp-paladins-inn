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

package de.kaiserpfalzEdv.paladinsInn.events;

import de.kaiserpfalzEdv.paladinsInn.commons.Identifiable;
import de.kaiserpfalzEdv.paladinsInn.commons.Nameable;
import de.kaiserpfalzEdv.paladinsInn.security.Maintainable;
import de.kaiserpfalzEdv.paladinsInn.security.TenantHolding;
import de.kaiserpfalzEdv.paladinsInn.topics.Taggable;

import java.io.Serializable;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * The event data for the events in the database.
 *
 * @author rlichti {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 2016-03-20
 */
public interface Event extends Identifiable, Nameable, Taggable, TenantHolding, Maintainable, Serializable {
    /**
     * @return The start date of the event.
     */
    LocalDateTime getDate();

    /**
     * @return The duration of the event.
     */
    Duration getDuration();

    /**
     * @return The descriptional text of the event.
     */
    String getDescription();

    /**
     * @return an optionional URL for the event home page.
     */
    URL getEventLink();

    /**
     * @return The name of the organization hosting the event.
     */
    String getOrganizationName();

    /**
     * @return an optional URL for the hosting organization.
     */
    URL getOrganizationLink();
}
