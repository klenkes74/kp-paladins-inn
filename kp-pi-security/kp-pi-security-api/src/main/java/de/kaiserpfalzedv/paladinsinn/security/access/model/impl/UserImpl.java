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

package de.kaiserpfalzedv.paladinsinn.security.access.model.impl;

import java.util.Locale;
import java.util.UUID;

import de.kaiserpfalzedv.paladinsinn.commons.person.Email;
import de.kaiserpfalzedv.paladinsinn.security.access.PasswordFailureException;
import de.kaiserpfalzedv.paladinsinn.security.access.model.User;
import de.kaiserpfalzedv.paladinsinn.security.access.model.Persona;
import de.kaiserpfalzedv.paladinsinn.commons.impl.IdentifiableAbstractImpl;
import de.kaiserpfalzedv.paladinsinn.security.access.UserIsLockedException;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2017-03-11
 */
class UserImpl extends IdentifiableAbstractImpl implements User {
    private static final long serialVersionUID = 7375303558202040469L;
    
    private Persona person;
    private Email emailAddress;

    private String password;

    private boolean locked;


    UserImpl(
            final UUID uniqueId,
            final String name,
            final Persona person,
            final Email emailAddress,
            final String password,
            final boolean locked
    ) {
        super(uniqueId, name);
        
        this.person = person;
        this.emailAddress = emailAddress;
        this.password = password;
        this.locked = locked;
    }
    

    public Persona getPerson() {
        return person;
    }

    @Override
    public Locale getLocale() {
        if (person == null) {
            return Locale.getDefault();
        }

        return person.getLocale();
    }


    @Override
    public Email getEmailAddress() {
        return emailAddress;
    }


    @Override
    public void login(final String passwordToCheck) throws PasswordFailureException, UserIsLockedException {
        if (password == null || ! password.equals(passwordToCheck)) {
            throw new PasswordFailureException(getName());
        }

        if (locked) {
            throw new UserIsLockedException(getName());
        }
    }

    @Override
    public boolean isLocked() {
        return locked;
    }
}
