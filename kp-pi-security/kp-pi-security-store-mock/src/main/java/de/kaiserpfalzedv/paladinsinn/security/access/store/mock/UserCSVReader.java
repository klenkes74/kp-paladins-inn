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

package de.kaiserpfalzedv.paladinsinn.security.access.store.mock;

import de.kaiserpfalzedv.paladinsinn.commons.DataReader;
import de.kaiserpfalzedv.paladinsinn.commons.person.Gender;
import de.kaiserpfalzedv.paladinsinn.commons.person.impl.NameBuilder;
import de.kaiserpfalzedv.paladinsinn.security.access.model.User;
import de.kaiserpfalzedv.paladinsinn.security.access.model.impl.PersonaBuilder;
import de.kaiserpfalzedv.paladinsinn.security.access.model.impl.UserBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URI;
import java.time.LocalDate;
import java.util.*;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2017-03-14
 */
public class UserCSVReader implements DataReader<User> {
    private static final Logger LOG = LoggerFactory.getLogger(UserCSVReader.class);


    private static final HashMap<String, Locale> languages = new HashMap<>(Locale.getAvailableLocales().length);
    private static final HashMap<String, Locale> countries = new HashMap<>(Locale.getAvailableLocales().length);

    static {
        for (Locale l : Locale.getAvailableLocales()) {
            try {
                languages.put(l.getISO3Language(), l);
                countries.put(l.getISO3Country(), l);

                LOG.trace("Available Locale and Country: {} -> (language: {}, country: {})", l, l.getISO3Language(), l.getISO3Country());
            } catch (MissingResourceException e) {
                LOG.warn("No 3-letter-code found for a locale.", e);
            }
        }
    }


    @Override
    public Set<User> read(URI uri) throws IOException {
        return read(uri.toURL().openStream());
    }


    @Override
    public Set<User> read(InputStream stream) {
        Scanner scanner = new Scanner(stream);
        scanner.useDelimiter(";");

        return readAllLines(scanner);
    }

    @Override
    public Set<User> read(String fileName) throws FileNotFoundException {
        return read(new File(fileName));
    }

    @Override
    public Set<User> read(File file) throws FileNotFoundException {
        return read(new FileInputStream(file));
    }

    private Set<User> readAllLines(Scanner scanner) {
        HashSet<User> users = new HashSet<>();

        while (scanner.hasNextLine()) {
            users.add(readSingleUser(scanner.nextLine()));
        }

        return users;
    }

    private User readSingleUser(final String line) {
        LOG.trace("Reading line: {}", line);

        Scanner scanner = new Scanner(line);
        scanner.useDelimiter(";");

        String uniqueId = scanner.hasNext() ? scanner.next() : UUID.randomUUID().toString();

        if (!scanner.hasNext()) {
            throw new IllegalArgumentException("Need to give an user id for user: " + uniqueId);
        }
        String userId = scanner.next();

        if (!scanner.hasNext()) {
            throw new IllegalArgumentException("Need to give password for user: " + uniqueId);
        }
        String password = scanner.next();

        if (!scanner.hasNext()) {
            throw new IllegalArgumentException("Need to give a name for the user: " + uniqueId);
        }
        String sn = scanner.next();

        if (!scanner.hasNext()) {
            throw new IllegalArgumentException("Need to give a given name for the user: " + uniqueId);
        }
        String givenName = scanner.next();

        String snPrefix = scanner.hasNext() ? scanner.next() : null;
        String snPostfix = scanner.hasNext() ? scanner.next() : null;
        String givenNamePrefix = scanner.hasNext() ? scanner.next() : null;
        String givenNamePostfix = scanner.hasNext() ? scanner.next() : null;

        String gender = scanner.hasNext() ? scanner.next() : null;
        String dateOfBirth = scanner.hasNext() ? scanner.next() : null;

        String country = scanner.hasNext() ? scanner.next() : null;
        String locale = scanner.hasNext() ? scanner.next() : null;

        String locked = scanner.hasNext() ? scanner.next() : "N";

        User result = buildUser(uniqueId, userId, password,
                                sn, givenName,
                                snPrefix, snPostfix,
                                givenNamePrefix, givenNamePostfix,
                                gender, dateOfBirth,
                                country, locale, locked
        );

        LOG.debug("User created: {} -> {}", line, result);
        return result;
    }

    private User buildUser(String uniqueId, String userId, String password, String sn, String givenName, String snPrefix, String snPostfix, String givenNamePrefix, String givenNamePostfix, String gender, String dateOfBirth, String country, String locale, String locked) {
        UserBuilder result = new UserBuilder()
                .withUniqueId(UUID.fromString(uniqueId))
                .withName(userId)
                .withPassword(password)

                .withPerson(
                        new PersonaBuilder()
                                .withUniqueId(UUID.fromString(uniqueId))
                                .withName(
                                        new NameBuilder()
                                                .withGivenNamePrefix(givenNamePrefix)
                                                .withGivenName(givenName)
                                                .withGivenNamePostfix(givenNamePostfix)
                                                .withSnPrefix(snPrefix)
                                                .withSn(sn)
                                                .withSnPostfix(snPostfix)
                                                .build()
                                )
                                .withGender(Gender.valueOf(gender))
                                .withDateOfBirth(LocalDate.parse(dateOfBirth))
                                .withCountry(countries.get(country))
                                .withLocale(languages.get(locale))
                                .build()
                );

        if ("Y".equalsIgnoreCase(locked) || "1".equals(locked) || "T".equals(locked)) {
            result.locked();
        }

        return result.build();
    }
}
