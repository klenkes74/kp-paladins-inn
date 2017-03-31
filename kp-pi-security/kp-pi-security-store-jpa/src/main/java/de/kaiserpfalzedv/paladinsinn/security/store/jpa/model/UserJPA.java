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
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import de.kaiserpfalzedv.paladinsinn.commons.api.persistence.Modifiable;
import de.kaiserpfalzedv.paladinsinn.commons.api.persistence.MultiTenantable;
import de.kaiserpfalzedv.paladinsinn.commons.jpa.NamedTenantMetaData;
import de.kaiserpfalzedv.paladinsinn.security.api.model.Entitlement;
import de.kaiserpfalzedv.paladinsinn.security.api.model.Role;
import de.kaiserpfalzedv.paladinsinn.security.api.model.User;
import de.kaiserpfalzedv.paladinsinn.security.api.services.PasswordFailureException;
import de.kaiserpfalzedv.paladinsinn.security.api.services.UserIsLockedException;
import org.mindrot.jbcrypt.BCrypt;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2017-03-26
 */
@Entity(name = "user")
@Table(
        name = "USERS",
        uniqueConstraints = {
                @UniqueConstraint(name = "USERS_UUID_UK", columnNames = {"ID"}),
                @UniqueConstraint(name = "USERS_NAME_UK", columnNames = {"TENANT_ID", "NAME"}),
        }
)
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
public class UserJPA extends NamedTenantMetaData implements User, MultiTenantable, Modifiable {
    private static final long serialVersionUID = 6705906221399853461L;
    @Transient
    private final HashSet<Principal> allEntitlements = new HashSet<>();
    @Transient
    private final HashSet<Role> allRoles = new HashSet<>();
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "PERSONA_ID", nullable = false)
    private PersonaJPA persona;
    @Embedded
    private EmailJPA email;
    @Column(name = "LOCKED", nullable = false)
    private boolean locked;
    @Column(name = "PASSWORD", length = 200, nullable = false)
    private String password;
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
    private volatile Set<RoleJPA> roles;
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
    private volatile Set<EntitlementJPA> entitlements;


    @Deprecated
    public UserJPA() {}

    public UserJPA(
            final UUID uniqueId, final Long version,
            final UUID tenantId,
            final String name, final String password,
            final PersonaJPA fullName,
            final EmailJPA emailAddress, final boolean locked,
            final OffsetDateTime created, final OffsetDateTime modified
    ) {
        super(uniqueId, version, tenantId, name, created, modified);

        setPassword(password);
        setPerson(fullName);
        setEmailAddress(emailAddress);
        setLocked(locked);
    }

    @Override
    public String getPassword() {
        return password;
    }

    private void setPassword(final String password) {
        this.password = password;
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

    @Override
    public EmailJPA getEmailAddress() {
        return email;
    }

    @Override
    public void login(final String passwordToCheck) throws PasswordFailureException, UserIsLockedException {
        if (!BCrypt.checkpw(passwordToCheck, password)) {
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

    private void setLocked(final boolean locked) {
        this.locked = locked;
    }

    @Override
    public Set<? extends Role> getRoles() {
        return Collections.unmodifiableSet(roles);
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

    private void prepareEntitlementSet() {
        Set<EntitlementJPA> tmp = entitlements;
        if (tmp == null) {
            synchronized (this) {
                tmp = entitlements;
                if (tmp == null) {
                    entitlements = new HashSet<>();
                }
            }
        }
    }

    @Deprecated
    public void setRoles(final Set<RoleJPA> roles) {
        prepareRoleSet();

        this.roles.clear();

        if (roles != null) {
            this.roles.addAll(roles);
        }
    }

    public void setEmailAddress(final EmailJPA emailAddress) {
        this.email = emailAddress;
    }

    public void setLocale(final Locale locale) {
        persona.setLocale(locale);
    }

    public void addRole(final RoleJPA role) {
        prepareRoleSet();

        this.roles.add(role);
    }

    private void prepareRoleSet() {
        Set<RoleJPA> tmp = roles;
        if (tmp == null) {
            synchronized (this) {
                tmp = roles;
                if (tmp == null) {
                    roles = new HashSet<>();
                }
            }
        }
    }

    public void removeRole(final UserJPA role) {
        prepareRoleSet();

        this.roles.remove(role);
    }

    private void addRoleToAllRoles(final Role role) {
        allRoles.add(role);
        role.getIncludedRoles().forEach(this::addRoleToAllRoles);
    }

    private void addEntitlement(final EntitlementJPA entitlement) {
        prepareEntitlementSet();

        this.entitlements.add(entitlement);
    }

    private void removeEntitlement(final Entitlement entitlement) {
        prepareEntitlementSet();

        this.entitlements.remove(entitlement);
    }

    private void addEntitlementFromRoleToAllEntitlements(final Role role) {
        if (!role.getEntitlements().isEmpty()) {
            allEntitlements.addAll(role.getEntitlements());
        }

        role.getIncludedRoles().forEach(this::addEntitlementFromRoleToAllEntitlements);
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("UserJPA@")
                .append(System.identityHashCode(this)).append('{');

        sb.append(super.toString());

        return sb.append('}').toString();
    }
}
