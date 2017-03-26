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

import java.security.Principal;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

import de.kaiserpfalzedv.paladinsinn.commons.api.persistence.Modifiable;
import de.kaiserpfalzedv.paladinsinn.commons.api.persistence.MultiTenantable;
import de.kaiserpfalzedv.paladinsinn.security.api.model.Entitlement;
import de.kaiserpfalzedv.paladinsinn.security.api.model.Role;
import de.kaiserpfalzedv.paladinsinn.security.api.model.User;
import de.kaiserpfalzedv.paladinsinn.security.api.services.PasswordFailureException;
import de.kaiserpfalzedv.paladinsinn.security.api.services.UserIsLockedException;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2017-03-26
 */
@Entity(name = "user")
@Table(name = "USERS")
@NamedQueries({
        @NamedQuery(
                name = "user-by-name",
                query = "SELECT u FROM user u WHERE u.tenantId=:tenant AND u.name=:name"
        ),
        @NamedQuery(
                name = "users",
                query = "SELECT u FROM user u WHERE u.tenantId=:tenant"
        )
})
public class UserJPA implements User, MultiTenantable, Modifiable {
    public static final ZoneId UTC = ZoneId.of("UTC");
    private static final long serialVersionUID = 3051192195680467875L;
    @Transient
    final HashSet<Principal> allEntitlements = new HashSet<>();
    @Transient
    private final HashSet<Role> allRoles = new HashSet<>();

    @Id
    @Column(name = "ID", unique = true, nullable = false, updatable = false)
    private UUID uniqueId;
    @Column(name = "TENANT_ID", nullable = false)
    private UUID tenantId;
    @SuppressWarnings("unused") // managd by JPA
    @Version
    @Column(name = "VERSION", nullable = false)
    private long version;
    @Column(name = "NAME", length = 200, unique = true, nullable = false)
    private String name;

    @ManyToOne(optional = false)
    @JoinColumn(name = "PERSONA_ID", nullable = false)
    private PersonaJPA persona;

    @Embedded
    private EmailJPA email;

    @Column(name = "LOCKED", nullable = false)
    private boolean locked;
    @Column(name = "PASSWORD", length = 200, nullable = false)
    private String password;


    @Column(name = "CREATED", nullable = false)
    private OffsetDateTime created = OffsetDateTime.now(UTC);
    @Column(name = "MODIFIED", nullable = false)
    private OffsetDateTime modified = created;
    @ManyToMany
    @JoinTable(
            name = "USERS_ROLES",

            joinColumns = {@JoinColumn(name = "USER_ID", referencedColumnName = "ID")},
            foreignKey = @ForeignKey(name = "USERS_ROLES_USER_FK"),

            inverseJoinColumns = {@JoinColumn(name = "ROLE_ID", referencedColumnName = "ID")},
            inverseForeignKey = @ForeignKey(name = "ROLES_ROLES_ROLE_FK"),

            indexes = {
                    @Index(name = "USERS_ROLES_USER_IDX", columnList = "USER_ID"),
                    @Index(name = "USERS_ROLES_ROLE_IDX", columnList = "ROLE_ID")
            }
    )
    private Set<RoleJPA> roles;

    @ManyToMany
    @JoinTable(
            name = "USERS_ENTITLEMENTS",

            joinColumns = {@JoinColumn(name = "USER_ID", referencedColumnName = "ID")},
            foreignKey = @ForeignKey(name = "USERS_ENTITLEMENTS_USER_FK"),

            inverseJoinColumns = {@JoinColumn(name = "ENTITLEMENT_ID", referencedColumnName = "ID")},
            inverseForeignKey = @ForeignKey(name = "USERS_ENTITLEMENTS_ENTITLEMENT_FK"),

            indexes = {
                    @Index(name = "USERS_ENTITLEMENTS_USER_IDX", columnList = "USER_ID"),
                    @Index(name = "USERS_ENTITLEMENTS_ENTITLEMENT_IDX", columnList = "ENTITLEMENT_ID")
            }
    )
    private Set<EntitlementJPA> entitlements;

    @Override
    public long getVersion() {
        return version;
    }

    @Override
    public OffsetDateTime getCreated() {
        return created;
    }

    public OffsetDateTime getModified() {
        return modified;
    }

    @Deprecated
    public void setModified(final OffsetDateTime modified) {
        this.modified = modified;
    }

    @Deprecated
    public void setCreated(final OffsetDateTime created) {
        this.created = created;
    }

    public UUID getTenantId() {
        return tenantId;
    }

    @Deprecated
    public void setTenantId(final UUID tenantId) {
        this.tenantId = tenantId;
    }

    @Deprecated
    @PrePersist
    public void updateChanged() {
        modified = OffsetDateTime.now(UTC);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUniqueId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserJPA)) return false;
        UserJPA that = (UserJPA) o;
        return Objects.equals(getUniqueId(), that.getUniqueId());
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("EntitlementJPA@")
                .append(System.identityHashCode(this)).append('{');

        sb
                .append(uniqueId);

        if (version != 0) {
            sb.append('/').append(version);
        }

        if (tenantId != null) {
            sb.append(", tenant=").append(tenantId);
        }

        sb
                .append(name)
                .append(", created=").append(created);

        if (!created.equals(modified)) {
            sb.append(", changed=").append(modified);
        }

        return sb.append('}').toString();
    }

    @Override
    public UUID getUniqueId() {
        return uniqueId;
    }

    @Override
    public String getName() {
        return name;
    }

    @Deprecated
    public void setName(final String name) {
        this.name = name;
    }

    @Deprecated
    public void setUniqueId(final UUID uniqueId) {
        this.uniqueId = uniqueId;
    }

    public void addRole(final RoleJPA role) {
        prepareRoleSet();

        this.roles.add(role);
    }

    public void removeRole(final UserJPA role) {
        prepareRoleSet();

        this.roles.remove(role);
    }

    private void addEntitlement(final EntitlementJPA entitlement) {
        prepareEntitlementSet();

        this.entitlements.add(entitlement);
    }

    private synchronized void prepareEntitlementSet() {
        if (this.entitlements == null) {
            this.entitlements = new HashSet<>();
        }
    }

    private void removeEntitlement(final Entitlement entitlement) {
        prepareEntitlementSet();

        this.entitlements.remove(entitlement);
    }

    private void addRoleToAllRoles(final Role role) {
        allRoles.add(role);

        role.getIncludedRoles().forEach(this::addRoleToAllRoles);
    }

    private void addEntitlementFromRoleToAllEntitlements(final Role role) {
        if (!role.getEntitlements().isEmpty()) {
            allEntitlements.addAll(role.getEntitlements());
        }

        role.getIncludedRoles().forEach(this::addEntitlementFromRoleToAllEntitlements);
    }

    @Override
    public PersonaJPA getPerson() {
        return persona;
    }

    public void setPerson(final PersonaJPA persona) {
        this.persona = persona;
    }

    @Override
    public Locale getLocale() {
        return persona.getLocale();
    }

    public void setLocale(final Locale locale) {
        persona.setLocale(locale);
    }

    @Override
    public EmailJPA getEmailAddress() {
        return email;
    }

    @Override
    public void login(String passwordToCheck) throws PasswordFailureException, UserIsLockedException {
        if (!password.equals(passwordToCheck)) {
            throw new PasswordFailureException(name);
        }

        if (locked) {
            throw new UserIsLockedException(name);
        }
    }

    @Override
    public boolean isLocked() {
        return locked;
    }

    @Override
    public Set<? extends Role> getRoles() {
        return Collections.unmodifiableSet(roles);
    }

    @Deprecated
    public void setRoles(final Set<RoleJPA> roles) {
        prepareRoleSet();

        this.roles.clear();

        if (roles != null) {
            this.roles.addAll(roles);
        }
    }

    private synchronized void prepareRoleSet() {
        if (this.roles == null) {
            this.roles = new HashSet<>();
        }
    }

    @Override
    public boolean isInRole(Role role) {
        return getAllRoles().contains(role);
    }

    private Set<Role> getAllRoles() {
        if (allRoles.isEmpty()) {
            synchronized (this) {
                if (allRoles.isEmpty()) {
                    addRolesToAllRoles();
                }
            }
        }

        return allRoles;
    }

    private void addRolesToAllRoles() {
        this.roles.forEach(this::addRoleToAllRoles);
    }

    @Override
    public Set<? extends Entitlement> getEntitlements() {
        return Collections.unmodifiableSet(entitlements);
    }

    @Override
    public boolean isEntitled(Entitlement entitlement) {
        return getAllEntitlements().contains(entitlement);
    }

    private Set<Principal> getAllEntitlements() {
        if (allEntitlements.isEmpty()) {
            synchronized (this) {
                if (!allRoles.isEmpty()) {
                    addEntitlementFromRolesToAllEntitlements();
                }
            }
        }

        return allEntitlements;
    }

    private void addEntitlementFromRolesToAllEntitlements() {
        this.roles.forEach(this::addEntitlementFromRoleToAllEntitlements);
    }

    @Deprecated
    public void setEntitlements(final Set<EntitlementJPA> entitlements) {
        prepareEntitlementSet();

        this.entitlements.clear();

        if (entitlements != null) {
            this.entitlements.addAll(entitlements);
        }
    }
}
