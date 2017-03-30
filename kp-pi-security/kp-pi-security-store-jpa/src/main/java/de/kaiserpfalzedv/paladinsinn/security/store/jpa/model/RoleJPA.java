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
import java.util.Set;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2017-03-26
 */
@Entity(name = "role")
@Table(
        name = "ROLES",
        uniqueConstraints = {
                @UniqueConstraint(name = "ROLES_UUID_UK", columnNames = {"ID"}),
                @UniqueConstraint(name = "ROLES_NAME_UK", columnNames = {"TENANT_ID", "NAME"})
        }
)
@NamedQueries({
        @NamedQuery(
                name = "role-by-name",
                query = "SELECT r FROM role r WHERE r.tenantId=:tenant AND r.name=:name"
        ),
        @NamedQuery(
                name = "roles",
                query = "SELECT r FROM role r WHERE r.tenantId=:tenant"
        )
})
public class RoleJPA extends NamedTenantMetaData implements Role, MultiTenantable, Modifiable {
    private static final long serialVersionUID = 628813761241485080L;
    @Transient
    final HashSet<Principal> allEntitlements = new HashSet<>();
    @Transient
    private final HashSet<Role> allRoles = new HashSet<>();
    @ManyToMany
    @JoinTable(
            name = "ROLES_ROLES",

            joinColumns = {@JoinColumn(name = "MASTER_ID", referencedColumnName = "ID")},
            foreignKey = @ForeignKey(name = "ROLES_ROLES_MASTER_FK"),

            inverseJoinColumns = {@JoinColumn(name = "SLAVE_ID", referencedColumnName = "ID")},
            inverseForeignKey = @ForeignKey(name = "ROLES_ROLES_SLAVE_FK"),

            indexes = {
                    @Index(name = "ROLES_ROLES_MASTER_IDX", columnList = "MASTER_ID"),
                    @Index(name = "ROLES_ROLES_SLAVE_IDX", columnList = "SLAVE_ID")
            }
    )
    private volatile Set<RoleJPA> roles;
    @ManyToMany
    @JoinTable(
            name = "ROLES_ENTITLEMENTS",

            joinColumns = {@JoinColumn(name = "ROLE_ID", referencedColumnName = "ID")},
            foreignKey = @ForeignKey(name = "ROLES_ENTITLEMENTS_ROLE_FK"),

            inverseJoinColumns = {@JoinColumn(name = "ENTITLEMENT_ID", referencedColumnName = "ID")},
            inverseForeignKey = @ForeignKey(name = "ROLES_ENTITLEMENTS_ENTITLEMENT_FK"),

            indexes = {
                    @Index(name = "ROLES_ENTITLEMENTS_ROLE_IDX", columnList = "ROLE_ID"),
                    @Index(name = "ROLES_ENTITLEMENTS_ENTITLEMENT_IDX", columnList = "ENTITLEMENT_ID")
            }
    )
    private volatile Set<EntitlementJPA> entitlements;


    @Deprecated
    public RoleJPA() {}

    public RoleJPA(
            final UUID uniqueId, final Long version,
            final UUID tenantId,
            final String name,
            final OffsetDateTime created, final OffsetDateTime modified
    ) {
        super(uniqueId, version, tenantId, name, created, modified);
    }

    @Override
    public boolean isInRole(Role role) {
        return getAllRoles().contains(role);
    }

    private Set<Role> getAllRoles() {
        if (allRoles.isEmpty()) {
            synchronized (this) {
                if (allRoles.isEmpty()) {
                    addRoleToAllRoles(this);
                }
            }
        }

        return allRoles;
    }

    private void addRoleToAllRoles(final Role role) {
        allRoles.add(role);

        role.getIncludedRoles().forEach(this::addRoleToAllRoles);
    }

    @Override
    public boolean isEntitled(Principal entitlement) {
        return getAllEntitlements().contains(entitlement);
    }

    @Override
    public Set<? extends Role> getIncludedRoles() {
        return Collections.unmodifiableSet(roles);
    }

    @Override
    public Set<? extends Entitlement> getEntitlements() {
        return Collections.unmodifiableSet(entitlements);
    }

    @Deprecated
    public void setEntitlements(final Set<EntitlementJPA> entitlements) {
        prepareEntitlementSet();

        this.entitlements.clear();

        if (entitlements != null) {
            this.entitlements.addAll(entitlements);
        }
    }

    private Set<Principal> getAllEntitlements() {
        if (allEntitlements.isEmpty()) {
            synchronized (this) {
                if (!allRoles.isEmpty()) {
                    addEntitlementFromRoleToAllEntitlements(this);
                }
            }
        }

        return allEntitlements;
    }

    private void addEntitlementFromRoleToAllEntitlements(final Role role) {
        if (!role.getEntitlements().isEmpty()) {
            allEntitlements.addAll(role.getEntitlements());
        }

        role.getIncludedRoles().forEach(this::addEntitlementFromRoleToAllEntitlements);
    }

    @Deprecated
    public void setRoles(final Set<RoleJPA> roles) {
        prepareRoleSet();

        this.roles.clear();

        if (roles != null) {
            this.roles.addAll(roles);
        }
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

    public void addRole(final RoleJPA role) {
        prepareRoleSet();

        this.roles.add(role);
    }

    public void removeRole(final RoleJPA role) {
        prepareRoleSet();

        this.roles.remove(role);
    }

    private void addEntitlement(final EntitlementJPA entitlement) {
        prepareEntitlementSet();

        this.entitlements.add(entitlement);
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

    private void removeEntitlement(final Entitlement entitlement) {
        prepareEntitlementSet();

        this.entitlements.remove(entitlement);
    }
}
