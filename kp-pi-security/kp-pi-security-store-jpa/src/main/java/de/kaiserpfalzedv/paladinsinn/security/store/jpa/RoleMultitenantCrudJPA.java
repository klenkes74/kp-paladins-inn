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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Alternative;
import javax.inject.Inject;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.TransactionRequiredException;
import javax.persistence.TypedQuery;

import de.kaiserpfalzedv.paladinsinn.commons.api.BuilderValidationException;
import de.kaiserpfalzedv.paladinsinn.commons.api.paging.Page;
import de.kaiserpfalzedv.paladinsinn.commons.api.paging.PageBuilder;
import de.kaiserpfalzedv.paladinsinn.commons.api.paging.PageRequest;
import de.kaiserpfalzedv.paladinsinn.commons.api.persistence.DuplicateUniqueIdException;
import de.kaiserpfalzedv.paladinsinn.commons.api.persistence.DuplicateUniqueNameException;
import de.kaiserpfalzedv.paladinsinn.commons.api.persistence.PersistenceRuntimeException;
import de.kaiserpfalzedv.paladinsinn.commons.api.service.SingleTenant;
import de.kaiserpfalzedv.paladinsinn.commons.api.service.WorkerService;
import de.kaiserpfalzedv.paladinsinn.commons.api.tenant.model.Tenant;
import de.kaiserpfalzedv.paladinsinn.security.api.model.Role;
import de.kaiserpfalzedv.paladinsinn.security.api.store.RoleMultitenantCrudService;
import de.kaiserpfalzedv.paladinsinn.security.store.jpa.model.RoleJPA;
import de.kaiserpfalzedv.paladinsinn.security.store.jpa.model.RoleJPABuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2017-03-26
 */
@Alternative
@RequestScoped
@SingleTenant
@WorkerService
public class RoleMultitenantCrudJPA implements RoleMultitenantCrudService {
    private static final Logger LOG = LoggerFactory.getLogger(RoleMultitenantCrudJPA.class);


    @PersistenceContext(name = "SECURITY_PU")
    private EntityManager em;


    @SuppressWarnings("unused")
    public RoleMultitenantCrudJPA() {}

    /**
     * Another constructor for testing the impementation. We need to be able to inject the entity manager ...
     *
     * @param em The entity manager (or mock for testing purposes)
     */
    @Inject
    public RoleMultitenantCrudJPA(
            final EntityManager em
    ) {
        this.em = em;
    }

    @Override
    public RoleJPA create(final Tenant tenant, final Role entitlement) throws DuplicateUniqueIdException, DuplicateUniqueNameException {
        Role data = createRoleJPA(tenant, entitlement);

        try {
            em.persist(data);

            return em.find(RoleJPA.class, entitlement.getUniqueId());
        } catch (EntityExistsException e) {
            LOG.warn(e.getClass().getSimpleName() + ": " + e.getMessage(), e);

            throw new DuplicateUniqueIdException(RoleJPA.class, entitlement);
        } catch (IllegalArgumentException | TransactionRequiredException e) {
            LOG.warn(e.getClass().getSimpleName() + ": " + e.getMessage(), e);

            throw new PersistenceRuntimeException(RoleJPA.class, e.getMessage(), e);
        }
    }

    private RoleJPA createRoleJPA(final Tenant tenant, final Role entitlement) {
        RoleJPA result;

        try {
            result = (RoleJPA) entitlement;
        } catch (ClassCastException e) {
            result = new RoleJPABuilder()
                    .withRole(entitlement)
                    .withTenant(tenant)
                    .build();
        }

        return result;
    }

    @Override
    public Optional<RoleJPA> retrieve(final Tenant tenant, final UUID uniqueId) {
        RoleJPA result;

        try {
            result = em.find(RoleJPA.class, uniqueId);
        } catch (IllegalArgumentException e) {
            LOG.warn(e.getClass().getSimpleName() + ": " + e.getMessage(), e);

            throw new PersistenceRuntimeException(RoleJPA.class, e.getMessage(), e);
        }

        if (result != null && result.getTenantId().equals(tenant.getUniqueId())) {
            return Optional.of(result);
        }

        return Optional.empty();
    }

    @Override
    public Optional<RoleJPA> retrieve(final Tenant tenant, final String uniqueName) {
        RoleJPA result = null;

        try {
            result = em
                    .createNamedQuery("entitlement-by-name", RoleJPA.class)
                    .setParameter("tenant", tenant.getUniqueId())
                    .setParameter("name", uniqueName)
                    .getSingleResult();
        } catch (NoResultException | NonUniqueResultException | IllegalStateException e) {
            LOG.warn(e.getClass().getSimpleName() + " during retrieval: " + e.getMessage(), e);

            // Do nothing. Will return null.
        } catch (PersistenceException e) {
            LOG.error(e.getClass().getSimpleName() + " during retrieval: " + e.getMessage(), e);

            throw new PersistenceRuntimeException(RoleJPA.class, "Caught a " + e.getClass()
                                                                                .getSimpleName() + ": " + e
                    .getMessage());
        }

        return Optional.ofNullable(result);
    }

    @Override
    public Set<Role> retrieve(final Tenant tenant) {
        final HashSet<Role> result = new HashSet<>();

        try {
            em
                    .createNamedQuery("entitlements", RoleJPA.class)
                    .setParameter("tenant", tenant.getUniqueId())
                    .getResultList()
                    .forEach(result::add);
        } catch (IllegalStateException e) {
            LOG.warn(e.getClass().getSimpleName() + " during retrieval: " + e.getMessage(), e);

            // do nothing more. Will return an empty result.
        } catch (PersistenceException e) {
            LOG.error(e.getClass().getSimpleName() + " during retrieval: " + e.getMessage(), e);

            throw new PersistenceRuntimeException(RoleJPA.class, "Caught a " + e.getClass()
                                                                                .getSimpleName() + ": " + e
                    .getMessage());
        }

        //noinspection unchecked
        return result;
    }

    @Override
    public Page<Role> retrieve(final Tenant tenant, final PageRequest pageRequest) {
        final ArrayList<Role> result = new ArrayList<>((int) pageRequest.getPageSize());

        TypedQuery<RoleJPA> query = em
                .createNamedQuery("entitlements", RoleJPA.class)
                .setParameter("tenant", tenant.getUniqueId());

        int elementCount = query.getMaxResults();

        if (elementCount > 0) {
            retrieveResultsFromJPA(tenant, pageRequest, result);
        }

        try {
            return new PageBuilder<Role>()
                    .withData(result)
                    .withRequest(pageRequest)
                    .withTotalElements(elementCount)
                    .build();

        } catch (BuilderValidationException e) {
            LOG.error(e.getClass().getSimpleName() + " caught: " + e.getMessage(), e);

            throw new PersistenceRuntimeException(RoleJPA.class, e.getClass()
                                                                  .getSimpleName() + " caught: " + e.getMessage());
        }
    }

    private void retrieveResultsFromJPA(final Tenant tenant, final PageRequest pageRequest, ArrayList<Role> result) {
        try {
            em
                    .createNamedQuery("entitlements", RoleJPA.class)
                    .setParameter("tenant", tenant.getUniqueId())
                    .setFirstResult(calculateStartPosition(pageRequest))
                    .setMaxResults((int) pageRequest.getPageSize())
                    .setLockMode(LockModeType.NONE)
                    .getResultList()
                    .forEach(result::add);
        } catch (IllegalStateException e) {
            LOG.warn(e.getClass().getSimpleName() + " during retrieval: " + e.getMessage(), e);

            // do nothing more. Will return an empty result.
        } catch (PersistenceException e) {
            LOG.error(e.getClass().getSimpleName() + " during retrieval: " + e.getMessage(), e);

            throw new PersistenceRuntimeException(RoleJPA.class, "Caught a " + e.getClass()
                                                                                .getSimpleName() + ": " + e
                    .getMessage());
        }
    }

    private int calculateStartPosition(final PageRequest pageRequest) {
        return (int) ((pageRequest.getPageNumber() - 1) * pageRequest.getPageSize());
    }

    @Override
    public RoleJPA update(final Tenant tenant, Role data) throws DuplicateUniqueNameException, DuplicateUniqueIdException {
        return update(tenant, createRoleJPA(tenant, data));
    }


    private RoleJPA update(final Tenant tenant, RoleJPA data) throws DuplicateUniqueNameException, DuplicateUniqueIdException {
        try {
            return em.merge(data);
        } catch (IllegalArgumentException e) {
            return create(tenant, data);
        } catch (TransactionRequiredException e) {
            LOG.error(e.getClass().getSimpleName() + " caught: " + e.getMessage(), e);

            throw new PersistenceRuntimeException(RoleJPA.class, e.getClass()
                                                                  .getSimpleName() + " caught: " + e.getMessage());
        }
    }

    @Override
    public void delete(final Tenant tenant, Role data) {
        delete(tenant, data.getUniqueId());
    }


    @Override
    public void delete(final Tenant tenant, UUID uniqueId) {
        retrieve(tenant, uniqueId).ifPresent(data -> delete(data, data.getUniqueId()));
    }

    private void delete(RoleJPA data, final Object errorMessageData) {
        if (data != null) {
            try {
                em.remove(data);

                LOG.info("Deleted entitlement: {}", data);
            } catch (IllegalArgumentException e) {
                LOG.warn("Tried to delete entitlement. But it did not exist: {}", data);
            } catch (TransactionRequiredException e) {
                LOG.error(e.getClass().getSimpleName() + " caught: " + e.getMessage(), e);

                throw new PersistenceRuntimeException(
                        RoleJPA.class,
                        e.getClass().getSimpleName() + " caught: " + e.getMessage()
                );
            }
        } else {
            LOG.warn("Tried to delete entitlement. But its unique id is not in the database: {}", errorMessageData);
        }
    }

    @Override
    public void delete(final Tenant tenant, String uniqueName) {
        retrieve(tenant, uniqueName).ifPresent(data -> delete(data, data.getUniqueId()));
    }
}
