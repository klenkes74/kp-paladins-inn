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

package de.kaiserpfalzedv.paladinsinn.security.store.jpa.model;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import de.kaiserpfalzedv.paladinsinn.commons.api.person.Gender;
import de.kaiserpfalzedv.paladinsinn.commons.jpa.NamedTenantMetaData;
import de.kaiserpfalzedv.paladinsinn.security.api.model.Persona;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2017-03-26
 */
@Entity(name = "persona")
@Table(
        name = "PERSONAS",
        uniqueConstraints = {
                @UniqueConstraint(name = "PERSONAS_UUID_UK", columnNames = {"ID"}),
                @UniqueConstraint(name = "PERSONAS_NAME_UK", columnNames = {"TENANT_ID", "NAME"}),
        }
)
@NamedQueries({
        @NamedQuery(
                name = "persona-by-name",
                query = "SELECT p FROM persona p WHERE p.tenantId=:tenant AND p.name=:name"
        ),
        @NamedQuery(
                name = "personas",
                query = "SELECT p FROM persona p WHERE p.tenantId=:tenant"
        )
})
public class PersonaJPA extends NamedTenantMetaData implements Persona {
    private static final long serialVersionUID = -7301902434340985965L;

    @Enumerated(EnumType.STRING)
    @Column(name = "GENDER", length = 50)
    private Gender gender;

    @Column(name = "BIRTHDATE")
    private LocalDate birthDate;

    @Column(name = "LANGUAGE")
    private Locale language;

    @Column(name = "COUNTRY")
    private Locale country;

    @Embedded
    private NameJPA fullName;

    @OneToMany(mappedBy = "PERSONA_ID", orphanRemoval = true)
    private volatile Set<UserJPA> users;


    @Deprecated
    public PersonaJPA() {}


    public PersonaJPA(
            final UUID uniqueId, final Long version,
            final UUID tenantId,
            final String name,
            final OffsetDateTime created, final OffsetDateTime modified,
            final NameJPA fullName,
            final Gender gender,
            final LocalDate dateOfBirth,
            final Locale country, final Locale language
    ) {
        super(uniqueId, version, tenantId, name, created, modified);

        setFullName(fullName);
        setGender(gender);
        setDateOfBirth(dateOfBirth);
        setCountry(country);
        setLocale(language);
    }


    @Override
    public NameJPA getFullName() {
        return fullName;
    }

    public void setFullName(final NameJPA fullName) {
        this.fullName = fullName;
    }


    @Override
    public Gender getGender() {
        return this.gender;
    }

    public void setGender(final Gender gender) {
        this.gender = gender;
    }


    @Override
    public LocalDate getDateOfBirth() {
        return birthDate;
    }

    public void setDateOfBirth(final LocalDate date) {
        this.birthDate = date;
    }

    @Override
    public int getAge() {
        if (getDateOfBirth() == null)
            return -1;

        return LocalDate.ofEpochDay(LocalDate.now().toEpochDay() - getDateOfBirth().toEpochDay()).getYear() - 1970;
    }


    @Override
    public Locale getCountry() {
        return country;
    }

    public void setCountry(final Locale locale) {
        this.country = country;
    }


    @Override
    public Locale getLocale() {
        return language;
    }

    public void setLocale(final Locale locale) {
        this.language = locale;
    }


    public Set<UserJPA> getUsers() {
        Set<UserJPA> tmp = users;

        if (tmp == null) {
            synchronized (this) {
                tmp = users;

                if (tmp == null) {
                    users = tmp = new HashSet<>();
                }
            }
        }

        return tmp;
    }

    public void setUsers(final Set<UserJPA> users) {
        this.users = users;
    }
}
