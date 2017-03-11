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

package de.kaiserpfalzedv.paladinsinn.security.access;

import java.util.Locale;
import java.util.UUID;

import de.kaiserpfalzedv.paladinsinn.security.Identifiable;
import de.kaiserpfalzedv.paladinsinn.security.identity.Person;

/**
 * Paladins Inn uses an extended principal containing some additional data of the user.
 *
 * At first registration every user gets a {@link UUID} assigned. This ID may be used for everything else later.
 * 
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2017-03-11
 */
public interface Principal extends Identifiable {
    /**
     * @return the person of the user.
     */
    Person getPerson();

    /**
     * @return the preferred language of the user. If not set returns default {@link Locale} of the JVM runtime.
     */
    Locale getLocale();

    /**
     * @return an email address of the user.
     */
    Email getEmailAddress();
}
