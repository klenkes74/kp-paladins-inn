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
import de.kaiserpfalzedv.paladinsinn.security.api.model.Entitlement;
import de.kaiserpfalzedv.paladinsinn.security.api.model.EntitlementBuilder;
import de.kaiserpfalzedv.paladinsinn.security.store.jpa.model.EntitlementJPA;
import de.kaiserpfalzedv.paladinsinn.security.store.jpa.model.EntitlementJPABuilder;
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
public class EntitlementCrudJPATest {
    private static final UUID ENTITLEMENT_UNIQUE_ID_1 = UUID.randomUUID();
    private static final String ENTITLEMENT_NAME_1 = "tenant 1";
    private static final OffsetDateTime ENTITLEMENT_CREATED_1 = OffsetDateTime.parse("2017-01-01T00:00:00+01:00");
    private static final OffsetDateTime ENTITLEMENT_MODIFIED_1 = OffsetDateTime.parse("2017-01-01T00:00:00+01:00");
    private static final Entitlement ENTITLEMENT_1;
    private static final EntitlementJPA ENTITLEMENT_1_JPA;

    private static final UUID ENTITLEMENT_UNIQUE_ID_2 = UUID.randomUUID();
    private static final String ENTITLEMENT_NAME_2 = "tenant 2";
    private static final OffsetDateTime ENTITLEMENT_CREATED_2 = OffsetDateTime.parse("2017-01-02T00:00:00+01:00");
    private static final OffsetDateTime ENTITLEMENT_MODIFIED_2 = OffsetDateTime.parse("2017-01-03T14:00:00+01:00");
    private static final EntitlementJPA ENTITLEMENT_2_JPA;

    private static final UUID ENTITLEMENT_UNIQUE_ID_3 = UUID.randomUUID();
    private static final String ENTITLEMENT_NAME_3 = "tenant 3";
    private static final OffsetDateTime ENTITLEMENT_CREATED_3 = OffsetDateTime.parse("2017-01-03T14:00:00+01:00");
    private static final OffsetDateTime ENTITLEMENT_MODIFIED_3 = OffsetDateTime.parse("2017-01-03T14:00:00+01:00");
    private static final EntitlementJPA ENTITLEMENT_3_JPA;

    static {
        ENTITLEMENT_1 = new EntitlementBuilder()
                .withUniqueId(ENTITLEMENT_UNIQUE_ID_1)
                .withName(ENTITLEMENT_NAME_1)
                .build();

        ENTITLEMENT_1_JPA = new EntitlementJPABuilder()
                .withUniqueId(ENTITLEMENT_UNIQUE_ID_1)
                .withName(ENTITLEMENT_NAME_1)
                .withCreated(ENTITLEMENT_CREATED_1)
                .withModified(ENTITLEMENT_MODIFIED_1)
                .build();
        ENTITLEMENT_2_JPA = new EntitlementJPABuilder()
                .withUniqueId(ENTITLEMENT_UNIQUE_ID_2)
                .withName(ENTITLEMENT_NAME_2)
                .withCreated(ENTITLEMENT_CREATED_2)
                .withModified(ENTITLEMENT_MODIFIED_2)
                .build();
        ENTITLEMENT_3_JPA = new EntitlementJPABuilder()
                .withUniqueId(ENTITLEMENT_UNIQUE_ID_3)
                .withName(ENTITLEMENT_NAME_3)
                .withCreated(ENTITLEMENT_CREATED_3)
                .withModified(ENTITLEMENT_MODIFIED_3)
                .build();
    }

    @Mock
    private EntityManager em;

    @Mock
    private TypedQuery<EntitlementJPA> query;

    private EntitlementMultitenantCrudJPA multitenantService;
    private EntitlementCrudJPA service;


    @Test
    public void shouldSaveTenantWhenCalledWithValidTenant() throws PersistenceException {
        when(em.find(EntitlementJPA.class, ENTITLEMENT_UNIQUE_ID_1)).thenReturn(ENTITLEMENT_1_JPA);

        Entitlement result = service.create(ENTITLEMENT_1);

        assertEquals("The objects should be equal!", ENTITLEMENT_1, result);
        assertNotEquals("The objects should not be the same!",
                        System.identityHashCode(ENTITLEMENT_1), System.identityHashCode(result)
        );
    }

    @Test(expected = DuplicateEntityException.class)
    public void shouldFailWhenSavingATenantWithExistingUniqueId()
            throws PersistenceException, BuilderValidationException {

        doThrow(new EntityExistsException("Entity already exists!")).when(em).persist(ENTITLEMENT_1_JPA);

        service.create(ENTITLEMENT_1);
    }

    @Test
    public void shouldRetrieveTenantWhenGivenAValidUniqueId() throws PersistenceException {
        when(em.find(EntitlementJPA.class, ENTITLEMENT_UNIQUE_ID_3)).thenReturn(ENTITLEMENT_3_JPA);

        Optional<? extends Entitlement> result = service.retrieve(ENTITLEMENT_UNIQUE_ID_3);

        assertTrue(result.isPresent());
    }

    @Test
    public void shouldRetrieveNoTenantWhenGivenAnInvalidUniqueId() {
        when(em.find(any(), any(UUID.class))).thenReturn(null);

        Optional<? extends Entitlement> result = service.retrieve(UUID.randomUUID());

        assertFalse(result.isPresent());
    }


    @Test
    public void shouldGetTheSecondPageWhenTryingToLoadIt() {
        ArrayList<EntitlementJPA> tenantList = new ArrayList<>(3);
        tenantList.add(ENTITLEMENT_1_JPA);
        tenantList.add(ENTITLEMENT_2_JPA);
        tenantList.add(ENTITLEMENT_3_JPA);

        when(em.createNamedQuery("entitlements", EntitlementJPA.class)).thenReturn(query);
        when(query.setParameter(eq("tenant"), any())).thenReturn(query);
        when(query.setFirstResult(3)).thenReturn(query);
        when(query.setMaxResults(3)).thenReturn(query);
        when(query.setLockMode(LockModeType.NONE)).thenReturn(query);
        when(query.getResultList()).thenReturn(tenantList);
        when(query.getMaxResults()).thenReturn(20);

        Page<? extends Entitlement> result = service.retrieve(new PageRequestImpl(2, 3));

        assertEquals("Should contain 3 elements", 3, result.getCurrentPageSize());
        assertEquals("Should report a total number of elements of 20", 20, result.getTotalElements());
    }

    @Test
    public void shouldUpdateDataWhenUsingAJPAObject() throws BuilderValidationException, PersistenceException {
        when(em.merge(ENTITLEMENT_1_JPA)).thenReturn(new EntitlementJPABuilder().withEntitlement(ENTITLEMENT_1_JPA)
                                                                                .build());

        Entitlement result = service.update(ENTITLEMENT_1_JPA);

        assertEquals("Should be equal objects", ENTITLEMENT_1_JPA, result);
        assertNotEquals("Shoud not be identical",
                        System.identityHashCode(ENTITLEMENT_1_JPA), System.identityHashCode(result)
        );
    }

    @Test
    public void shouldCreateDataWhenTheDataExists() throws BuilderValidationException, PersistenceException {
        when(em.merge(any())).thenReturn(
                new EntitlementJPABuilder().withEntitlement(ENTITLEMENT_1_JPA)
                                           .withModified(OffsetDateTime.now())
                                           .build()
        );

        EntitlementJPA result = service.update(ENTITLEMENT_1);

        assertEquals("Should be equal objects", ENTITLEMENT_1_JPA, result);
        assertNotEquals("Shoud not be identical",
                        System.identityHashCode(ENTITLEMENT_1_JPA), System.identityHashCode(result)
        );
        assertEquals("Creation time should not differ", ENTITLEMENT_1_JPA.getCreated(), result.getCreated());
        assertNotEquals("Modification time should differ", ENTITLEMENT_1_JPA.getModified(), result.getModified());
    }

    @Test
    public void shouldCreateDataWhenTheDataNotExists() throws BuilderValidationException, PersistenceException {
        when(em.merge(any())).thenReturn(
                new EntitlementJPABuilder().withEntitlement(ENTITLEMENT_1_JPA)
                                           .withCreated(OffsetDateTime.now())
                                           .withModified(OffsetDateTime.now())
                                           .build()
        );

        EntitlementJPA result = service.update(ENTITLEMENT_1);

        assertEquals("Should be equal objects", ENTITLEMENT_1_JPA, result);
        assertNotEquals("Shoud not be identical",
                        System.identityHashCode(ENTITLEMENT_1_JPA), System.identityHashCode(result)
        );
        assertNotEquals("Creation time should differ", ENTITLEMENT_1_JPA.getCreated(), result.getCreated());
        assertNotEquals("Modification time should differ", ENTITLEMENT_1_JPA.getModified(), result.getModified());
    }

    @Test
    public void shouldDeleteWhenGiventheUniqueId() throws PersistenceException {
        when(em.find(EntitlementJPA.class, ENTITLEMENT_UNIQUE_ID_1)).thenReturn(ENTITLEMENT_1_JPA);

        service.delete(ENTITLEMENT_UNIQUE_ID_1);
    }

    @Before
    public void setUpService() throws PersistenceException {
        multitenantService = new EntitlementMultitenantCrudJPA(em);
        service = new EntitlementCrudJPA(multitenantService);
    }
}
