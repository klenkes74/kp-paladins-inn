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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import de.kaiserpfalzedv.paladinsinn.commons.person.Email;
import de.kaiserpfalzedv.paladinsinn.security.access.User;
import de.kaiserpfalzedv.paladinsinn.security.access.UserIdGenerator;
import de.kaiserpfalzedv.paladinsinn.security.access.Persona;
import de.kaiserpfalzedv.paladinsinn.commons.impl.IdentifiableAbstractBuilder;

/**
 * Builds the principal given the correct data.
 * 
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2017-03-11
 */
public class UserBuilder extends IdentifiableAbstractBuilder<User> {
    private Persona person;
    private Email emailAddress;
    private String password;


    private UserIdGenerator userIdGenerator;

    /**
     * A list of errors during validation.
     */
    private final ArrayList<String> errors = new ArrayList<>(2);


    public UserImpl build() {
        setDefaultValuesIfNeeded();

        validateDuringBuild();
        
        return new UserImpl(
                uniqueId, name,
                person,
                emailAddress,
                password
        );
    }

    private void setDefaultValuesIfNeeded() {
        if (uniqueId == null) {
            uniqueId = UUID.randomUUID();
        }

        if (name == null && (person != null || emailAddress != null)) {
            if (userIdGenerator == null) {
                userIdGenerator = new DefaultUserIdGenerator();
            }

            withName(userIdGenerator.generateUserId(person, emailAddress));
        }
    }

    public void validateDuringBuild() {
        if (! validate()) {
            StringBuilder error = new StringBuilder("The principal can't be build:\n");

            errors.forEach(e -> error.append("- ").append(e).append('\n'));

            throw new IllegalStateException(error.toString());
        }
    }

    public boolean validate() {
        boolean result = true;
        errors.clear();

        if (name == null || name.isEmpty()) {
            result = false;
            errors.add("The name (user id) is not set.");
        }

        return result;
    }



    public List<String> getValidationResults() {
        return Collections.unmodifiableList(errors);
    }


    /**
     * Sets the user id generator for generating an unique user id. If not set by the program, the
     * {@link DefaultUserIdGenerator} is used. You may provide a generator of your own to check for duplicates in the
     * curent user database and generate unique ones.
     *
     * @param userIdGenerator The generator to calculate the username if not given.
     * @return the principal builder itself
     */
    public UserBuilder withUserIdGenerator(final UserIdGenerator userIdGenerator) {
        this.userIdGenerator = userIdGenerator;
        return this;
    }


    public UserBuilder withPrincipal(final User user) {
        withUniqueId(user.getUniqueId());
        withName(user.getName());
        withPerson(user.getPerson());
        withEmailAddress(user.getEmailAddress());

        return this;
    }


    public UserBuilder withUniqueId(final UUID uniqueId) {
        return (UserBuilder) super.withUniqueId(uniqueId);
    }

    public UserBuilder withName(final String name) {
        return (UserBuilder) super.withName(name);
    }

    public UserBuilder withPerson(final Persona person) {
        this.person = person ;
        return this;
    }

    public UserBuilder withEmailAddress(final Email emailAddress) {
        this.emailAddress = emailAddress;
        return this;
    }

    public UserBuilder withPassword(final String password) {
        this.password = password;
        return this;
    }
}
