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
import java.util.Locale;
import java.util.UUID;

import de.kaiserpfalzedv.paladinsinn.commons.api.BuilderValidationException;
import de.kaiserpfalzedv.paladinsinn.commons.api.persistence.NameableAbstractBuilder;
import de.kaiserpfalzedv.paladinsinn.commons.api.person.Gender;
import de.kaiserpfalzedv.paladinsinn.commons.api.person.Name;
import de.kaiserpfalzedv.paladinsinn.commons.api.tenant.model.DefaultTenant;
import de.kaiserpfalzedv.paladinsinn.security.api.model.Persona;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2017-03-31
 */
public class PersonaJPABuilder extends NameableAbstractBuilder<PersonaJPA> {
    private UUID tenantId;

    private Name fullName;
    private Gender gender;
    private LocalDate dateOfBirth;
    private Locale country;
    private Locale language;

    @Override
    public PersonaJPA build() throws BuilderValidationException {
        setDefaultsIfNeeded();
        validate();

        NameJPA jpaName = new NameJPABuilder().withName(fullName).build();

        PersonaJPA result = new PersonaJPA(
                uniqueId, version,
                tenantId,
                name,
                created, modified,
                jpaName, gender, dateOfBirth,
                country, language
        );

        return result;
    }

    protected void setDefaultsIfNeeded() {
        super.setDefaultsIfNeeded();


        if (tenantId == null) {
            tenantId = DefaultTenant.INSTANCE.getUniqueId();
        }
    }

    @Override
    public void validateDuringBuild() {
    }

    @Override
    public boolean validate() {
        return true;
    }

    public PersonaJPABuilder withPersona(final Persona persona) {
        withTenantId(persona.getTenantId());
        withUniqueId(persona.getUniqueId());
        withFullName(persona.getFullName());
        withGender(persona.getGender());
        withDateOfBirth(persona.getDateOfBirth());
        withCountry(persona.getCountry());
        withLanguage(persona.getLocale());

        if (persona instanceof PersonaJPA) {
            PersonaJPA p = (PersonaJPA) persona;

            withVersion(p.getVersion());
            withCreated(p.getCreated());
            withModified(p.getModified());
        }

        return this;
    }


    public PersonaJPABuilder withTenantId(final UUID tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    public PersonaJPABuilder withFullName(final Name fullName) {
        this.fullName = fullName;
        return this;
    }

    public PersonaJPABuilder withGender(final Gender gender) {
        this.gender = gender;
        return this;
    }

    public PersonaJPABuilder withDateOfBirth(final LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
        return this;
    }

    public PersonaJPABuilder withCountry(final Locale country) {
        this.country = country;
        return this;
    }

    public PersonaJPABuilder withLanguage(final Locale language) {
        this.language = language;
        return this;
    }
}
