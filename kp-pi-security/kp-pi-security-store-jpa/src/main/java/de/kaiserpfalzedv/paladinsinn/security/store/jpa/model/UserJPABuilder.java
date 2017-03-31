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

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import de.kaiserpfalzedv.paladinsinn.commons.api.BuilderValidationException;
import de.kaiserpfalzedv.paladinsinn.commons.api.persistence.MultiTenantable;
import de.kaiserpfalzedv.paladinsinn.commons.api.persistence.NameableAbstractBuilder;
import de.kaiserpfalzedv.paladinsinn.commons.api.person.Email;
import de.kaiserpfalzedv.paladinsinn.commons.api.tenant.model.DefaultTenant;
import de.kaiserpfalzedv.paladinsinn.security.api.model.Entitlement;
import de.kaiserpfalzedv.paladinsinn.security.api.model.Persona;
import de.kaiserpfalzedv.paladinsinn.security.api.model.Role;
import de.kaiserpfalzedv.paladinsinn.security.api.model.User;
import org.mindrot.jbcrypt.BCrypt;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2017-03-31
 */
public class UserJPABuilder extends NameableAbstractBuilder<UserJPA> {
    private UUID tenantId;
    private String password;
    private Persona persona;
    private Email email;
    private boolean locked;

    private final HashSet<Role> roles = new HashSet<>();
    private final HashSet<Entitlement> entitlements = new HashSet<>();


    @SuppressWarnings("deprecation")
    @Override
    public UserJPA build() throws BuilderValidationException {
        setDefaultsIfNeeded();

        PersonaJPA jpaPersona = new PersonaJPABuilder().withPersona(persona).build();
        EmailJPA emailAddress = new EmailJPA(email.getAddress());

        UserJPA result = new UserJPA(
                uniqueId, version,
                tenantId,
                name, password,
                jpaPersona,
                emailAddress, locked,
                created, modified
        );

        result.setRoles(convertRoles(roles));
        result.setEntitlements(convertEntitlements(entitlements));

        return result;
    }

    @Override
    public void validateDuringBuild() {
        // TODO klenkes Auto defined stub for: de.kaiserpfalzedv.paladinsinn.security.store.jpa.model.UserJPABuilder.validateDuringBuild
    }

    @Override
    public boolean validate() {
        // TODO klenkes Auto defined stub for: de.kaiserpfalzedv.paladinsinn.security.store.jpa.model.UserJPABuilder.validate
        return true;
    }


    private Set<RoleJPA> convertRoles(Collection<? extends Role> data) {
        HashSet<RoleJPA> result = new HashSet<>(data.size());

        data.forEach(d -> result.add(new RoleJPABuilder().withRole(d).build()));

        return result;
    }

    private Set<EntitlementJPA> convertEntitlements(final Collection<? extends Entitlement> data) {
        HashSet<EntitlementJPA> result = new HashSet<>(data.size());

        data.forEach(d -> result.add(new EntitlementJPABuilder().withEntitlement(d).build()));

        return result;
    }

    public UserJPABuilder withUser(final User user) {
        withPersona(user.getPerson());
        password = user.getPassword();  // already hashed
        withEmail(user.getEmailAddress());
        withLocked(user.isLocked());
        withUniqueId(user.getUniqueId());


        if (user instanceof MultiTenantable) {
            withTenantId(((MultiTenantable) user).getTenantId());
        } else {
            withTenantId(DefaultTenant.INSTANCE.getUniqueId());
        }

        if (user instanceof UserJPA) {
            UserJPA p = (UserJPA) user;

            withVersion(p.getVersion());
            withCreated(p.getCreated());
            withModified(p.getModified());
        }

        return this;
    }

    public UserJPABuilder withTenantId(final UUID tenantId) {
        this.tenantId = tenantId;
        return this;
    }

    public UserJPABuilder withPassword(final String password) {
        this.password = BCrypt.hashpw(password, BCrypt.gensalt());
        return this;
    }

    public UserJPABuilder withPersona(final Persona persona) {
        this.persona = persona;
        return this;
    }

    public UserJPABuilder withEmail(final Email email) {
        this.email = email;
        return this;
    }

    public UserJPABuilder withLocked(final boolean locked) {
        this.locked = locked;
        return this;
    }
}
