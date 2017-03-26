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

package de.kaiserpfalzedv.paladinsinn.security.store.jpa;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.TypedQuery;

import de.kaiserpfalzedv.paladinsinn.commons.api.BuilderValidationException;
import de.kaiserpfalzedv.paladinsinn.commons.api.paging.Page;
import de.kaiserpfalzedv.paladinsinn.commons.api.paging.PageRequestImpl;
import de.kaiserpfalzedv.paladinsinn.commons.api.persistence.DuplicateEntityException;
import de.kaiserpfalzedv.paladinsinn.commons.api.persistence.PersistenceException;
import de.kaiserpfalzedv.paladinsinn.security.api.model.Role;
import de.kaiserpfalzedv.paladinsinn.security.api.model.RoleBuilder;
import de.kaiserpfalzedv.paladinsinn.security.store.jpa.model.RoleJPA;
import de.kaiserpfalzedv.paladinsinn.security.store.jpa.model.RoleJPABuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2017-03-18
 */
@RunWith(MockitoJUnitRunner.class)
public class RoleCrudJPATest {
    private static final UUID ROLE_UNIQUE_ID_1 = UUID.randomUUID();
    private static final String ROLE_NAME_1 = "tenant 1";
    private static final OffsetDateTime ROLE_CREATED_1 = OffsetDateTime.parse("2017-01-01T00:00:00+01:00");
    private static final OffsetDateTime ROLE_MODIFIED_1 = OffsetDateTime.parse("2017-01-01T00:00:00+01:00");
    private static final Role ROLE_1;
    private static final RoleJPA ROLE_1_JPA;

    private static final UUID ROLE_UNIQUE_ID_2 = UUID.randomUUID();
    private static final String ROLE_NAME_2 = "tenant 2";
    private static final OffsetDateTime ROLE_CREATED_2 = OffsetDateTime.parse("2017-01-02T00:00:00+01:00");
    private static final OffsetDateTime ROLE_MODIFIED_2 = OffsetDateTime.parse("2017-01-03T14:00:00+01:00");
    private static final RoleJPA ROLE_2_JPA;

    private static final UUID ROLE_UNIQUE_ID_3 = UUID.randomUUID();
    private static final String ROLE_NAME_3 = "tenant 3";
    private static final OffsetDateTime ROLE_CREATED_3 = OffsetDateTime.parse("2017-01-03T14:00:00+01:00");
    private static final OffsetDateTime ROLE_MODIFIED_3 = OffsetDateTime.parse("2017-01-03T14:00:00+01:00");
    private static final RoleJPA ROLE_3_JPA;

    static {
        ROLE_1 = new RoleBuilder()
                .withUniqueId(ROLE_UNIQUE_ID_1)
                .withName(ROLE_NAME_1)
                .build();

        ROLE_1_JPA = new RoleJPABuilder()
                .withUniqueId(ROLE_UNIQUE_ID_1)
                .withName(ROLE_NAME_1)
                .withCreated(ROLE_CREATED_1)
                .withModified(ROLE_MODIFIED_1)
                .build();
        ROLE_2_JPA = new RoleJPABuilder()
                .withUniqueId(ROLE_UNIQUE_ID_2)
                .withName(ROLE_NAME_2)
                .withCreated(ROLE_CREATED_2)
                .withModified(ROLE_MODIFIED_2)
                .build();
        ROLE_3_JPA = new RoleJPABuilder()
                .withUniqueId(ROLE_UNIQUE_ID_3)
                .withName(ROLE_NAME_3)
                .withCreated(ROLE_CREATED_3)
                .withModified(ROLE_MODIFIED_3)
                .build();
    }

    @Mock
    private EntityManager em;

    @Mock
    private TypedQuery<RoleJPA> query;

    private RoleMultitenantCrudJPA multitenantService;
    private RoleCrudJPA service;


    @Test
    public void shouldSaveTenantWhenCalledWithValidTenant() throws PersistenceException {
        when(em.find(RoleJPA.class, ROLE_UNIQUE_ID_1)).thenReturn(ROLE_1_JPA);

        Role result = service.create(ROLE_1);

        assertEquals("The objects should be equal!", ROLE_1, result);
        assertNotEquals("The objects should not be the same!",
                        System.identityHashCode(ROLE_1), System.identityHashCode(result)
        );
    }

    @Test(expected = DuplicateEntityException.class)
    public void shouldFailWhenSavingATenantWithExistingUniqueId()
            throws PersistenceException, BuilderValidationException {

        doThrow(new EntityExistsException("Entity already exists!")).when(em).persist(ROLE_1_JPA);

        service.create(ROLE_1);
    }

    @Test
    public void shouldRetrieveTenantWhenGivenAValidUniqueId() throws PersistenceException {
        when(em.find(RoleJPA.class, ROLE_UNIQUE_ID_3)).thenReturn(ROLE_3_JPA);

        Optional<? extends Role> result = service.retrieve(ROLE_UNIQUE_ID_3);

        assertTrue(result.isPresent());
    }

    @Test
    public void shouldRetrieveNoTenantWhenGivenAnInvalidUniqueId() {
        when(em.find(any(), any(UUID.class))).thenReturn(null);

        Optional<? extends Role> result = service.retrieve(UUID.randomUUID());

        assertFalse(result.isPresent());
    }


    @Test
    public void shouldGetTheSecondPageWhenTryingToLoadIt() {
        ArrayList<RoleJPA> tenantList = new ArrayList<>(3);
        tenantList.add(ROLE_1_JPA);
        tenantList.add(ROLE_2_JPA);
        tenantList.add(ROLE_3_JPA);

        when(em.createNamedQuery("entitlements", RoleJPA.class)).thenReturn(query);
        when(query.setParameter(eq("tenant"), any())).thenReturn(query);
        when(query.setFirstResult(3)).thenReturn(query);
        when(query.setMaxResults(3)).thenReturn(query);
        when(query.setLockMode(LockModeType.NONE)).thenReturn(query);
        when(query.getResultList()).thenReturn(tenantList);
        when(query.getMaxResults()).thenReturn(20);

        Page<? extends Role> result = service.retrieve(new PageRequestImpl(2, 3));

        assertEquals("Should contain 3 elements", 3, result.getCurrentPageSize());
        assertEquals("Should report a total number of elements of 20", 20, result.getTotalElements());
    }

    @Test
    public void shouldUpdateDataWhenUsingAJPAObject() throws BuilderValidationException, PersistenceException {
        when(em.merge(ROLE_1_JPA)).thenReturn(new RoleJPABuilder().withRole(ROLE_1_JPA)
                                                                  .build());

        Role result = service.update(ROLE_1_JPA);

        assertEquals("Should be equal objects", ROLE_1_JPA, result);
        assertNotEquals("Shoud not be identical",
                        System.identityHashCode(ROLE_1_JPA), System.identityHashCode(result)
        );
    }

    @Test
    public void shouldCreateDataWhenTheDataExists() throws BuilderValidationException, PersistenceException {
        when(em.merge(any())).thenReturn(
                new RoleJPABuilder().withRole(ROLE_1_JPA)
                                    .withModified(OffsetDateTime.now())
                                    .build()
        );

        RoleJPA result = service.update(ROLE_1);

        assertEquals("Should be equal objects", ROLE_1_JPA, result);
        assertNotEquals("Shoud not be identical",
                        System.identityHashCode(ROLE_1_JPA), System.identityHashCode(result)
        );
        assertEquals("Creation time should not differ", ROLE_1_JPA.getCreated(), result.getCreated());
        assertNotEquals("Modification time should differ", ROLE_1_JPA.getModified(), result.getModified());
    }

    @Test
    public void shouldCreateDataWhenTheDataNotExists() throws BuilderValidationException, PersistenceException {
        when(em.merge(any())).thenReturn(
                new RoleJPABuilder().withRole(ROLE_1_JPA)
                                    .withCreated(OffsetDateTime.now())
                                    .withModified(OffsetDateTime.now())
                                    .build()
        );

        RoleJPA result = service.update(ROLE_1);

        assertEquals("Should be equal objects", ROLE_1_JPA, result);
        assertNotEquals("Shoud not be identical",
                        System.identityHashCode(ROLE_1_JPA), System.identityHashCode(result)
        );
        assertNotEquals("Creation time should differ", ROLE_1_JPA.getCreated(), result.getCreated());
        assertNotEquals("Modification time should differ", ROLE_1_JPA.getModified(), result.getModified());
    }

    @Test
    public void shouldDeleteWhenGiventheUniqueId() throws PersistenceException {
        when(em.find(RoleJPA.class, ROLE_UNIQUE_ID_1)).thenReturn(ROLE_1_JPA);

        service.delete(ROLE_UNIQUE_ID_1);
    }

    @Before
    public void setUpService() throws PersistenceException {
        multitenantService = new RoleMultitenantCrudJPA(em);
        service = new RoleCrudJPA(multitenantService);
    }
}
