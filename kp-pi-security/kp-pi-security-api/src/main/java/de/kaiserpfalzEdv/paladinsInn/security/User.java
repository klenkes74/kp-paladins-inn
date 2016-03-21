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

package de.kaiserpfalzEdv.paladinsInn.security;

import de.kaiserpfalzEdv.paladinsInn.commons.Identifiable;
import de.kaiserpfalzEdv.paladinsInn.commons.Nameable;

import java.security.Principal;

/**
 * A registered user.
 *
 * @author rlichti {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 2016-03-20
 */
public interface User extends Identifiable, Nameable, Principal {
    /**
     * @param password The new password.
     */
    void setPassword(final String password);

    /**
     * @param passwordToCheck The password to check against the saved password.
     * @return if both passwords checked.
     */
    boolean checkPassword(final String passwordToCheck);
}
