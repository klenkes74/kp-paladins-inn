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
import java.time.ZoneId;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;

import de.kaiserpfalzedv.paladinsinn.commons.api.person.Gender;
import de.kaiserpfalzedv.paladinsinn.security.api.model.Persona;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2017-03-26
 */
@Entity(name = "persona")
@Table(name = "PERSONAS")
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
public class PersonaJPA implements Persona {
    public static final ZoneId UTC = ZoneId.of("UTC");
    private static final long serialVersionUID = 4358285403335206658L;
    @Id
    @Column(name = "ID", unique = true, nullable = false, updatable = false)
    private UUID uniqueId;
    @Column(name = "TENANT_ID", nullable = false)
    private UUID tenantId;
    @SuppressWarnings("unused") // managd by JPA
    @Version
    @Column(name = "VERSION", nullable = false)
    private long version;
    @Column(name = "CREATED", nullable = false)
    private OffsetDateTime created = OffsetDateTime.now(UTC);
    @Column(name = "MODIFIED", nullable = false)
    private OffsetDateTime modified = created;
    @Column(name = "NAME", length = 200, unique = true, nullable = false)
    private String name;
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
    private Set<UserJPA> users;

    @Override
    public UUID getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(UUID uniqueId) {
        this.uniqueId = uniqueId;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getTenantId() {
        return tenantId;
    }

    public void setTenantId(UUID tenantId) {
        this.tenantId = tenantId;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public OffsetDateTime getCreated() {
        return created;
    }

    public void setCreated(OffsetDateTime created) {
        this.created = created;
    }

    public OffsetDateTime getModified() {
        return modified;
    }

    public void setModified(OffsetDateTime modified) {
        this.modified = modified;
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
        if (birthDate == null)
            return -1;

        return LocalDate.ofEpochDay(LocalDate.now().toEpochDay() - birthDate.toEpochDay()).getYear() - 1970;
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
}
