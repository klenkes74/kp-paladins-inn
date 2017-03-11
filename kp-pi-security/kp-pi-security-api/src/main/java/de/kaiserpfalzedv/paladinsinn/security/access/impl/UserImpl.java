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

package de.kaiserpfalzedv.paladinsinn.security.access.impl;

import java.util.Locale;
import java.util.UUID;

import de.kaiserpfalzedv.paladinsinn.security.access.Email;
import de.kaiserpfalzedv.paladinsinn.security.access.User;
import de.kaiserpfalzedv.paladinsinn.security.identity.Person;
import de.kaiserpfalzedv.paladinsinn.security.impl.IdentifiableAbstractImpl;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2017-03-11
 */
class UserImpl extends IdentifiableAbstractImpl implements User {
    private static final long serialVersionUID = 7375303558202040469L;
    
    private Person person;
    private Email emailAddress;

    private String password;


    UserImpl(
            final UUID uniqueId,
            final String name,
            final Person person,
            final Email emailAddress,
            final String password
    ) {
        super(uniqueId, name);
        
        this.person = person;
        this.emailAddress = emailAddress;
        this.password = password;
    }
    

    public Person getPerson() {
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
    public boolean checkPassword(final String passwordToCheck) {
        if (password == null)
            return false;

        return password.equals(passwordToCheck);
    }
}
