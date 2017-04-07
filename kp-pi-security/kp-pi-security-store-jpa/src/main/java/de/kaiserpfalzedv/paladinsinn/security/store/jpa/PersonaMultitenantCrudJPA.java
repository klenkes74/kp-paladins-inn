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
import de.kaiserpfalzedv.paladinsinn.commons.api.service.MultiTenant;
import de.kaiserpfalzedv.paladinsinn.commons.api.service.WorkerService;
import de.kaiserpfalzedv.paladinsinn.commons.api.tenant.model.Tenant;
import de.kaiserpfalzedv.paladinsinn.security.api.model.Persona;
import de.kaiserpfalzedv.paladinsinn.security.api.store.PersonaMultitenantCrudService;
import de.kaiserpfalzedv.paladinsinn.security.store.jpa.model.PersonaJPA;
import de.kaiserpfalzedv.paladinsinn.security.store.jpa.model.PersonaJPABuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2017-03-26
 */
@Alternative
@RequestScoped
@MultiTenant
@WorkerService
public class PersonaMultitenantCrudJPA implements PersonaMultitenantCrudService {
    private static final Logger LOG = LoggerFactory.getLogger(PersonaMultitenantCrudJPA.class);


    @PersistenceContext(name = "SECURITY_PU")
    private EntityManager em;


    @SuppressWarnings("unused")
    public PersonaMultitenantCrudJPA() {}

    /**
     * Another constructor for testing the impementation. We need to be able to inject the entity manager ...
     *
     * @param em The entity manager (or mock for testing purposes)
     */
    @Inject
    public PersonaMultitenantCrudJPA(
            final EntityManager em
    ) {
        this.em = em;
    }

    @Override
    public PersonaJPA create(final Tenant tenant, final Persona persona) throws DuplicateUniqueIdException, DuplicateUniqueNameException {
        try {
            Persona data = createPersonaJPA(tenant, persona);

            em.persist(data);

            return em.find(PersonaJPA.class, persona.getUniqueId());
        } catch (EntityExistsException e) {
            LOG.warn(e.getClass().getSimpleName() + ": " + e.getMessage(), e);

            throw new DuplicateUniqueIdException(PersonaJPA.class, persona);
        } catch (IllegalArgumentException | TransactionRequiredException e) {
            LOG.warn(e.getClass().getSimpleName() + ": " + e.getMessage(), e);

            throw new PersistenceRuntimeException(PersonaJPA.class, e.getMessage(), e);
        } catch (BuilderValidationException e) {
            LOG.error(e.getClass().getSimpleName() + " caught: " + e.getMessage(), e);

            throw new PersistenceRuntimeException(
                    PersonaJPA.class,
                    e.getClass().getSimpleName() + " caught: " + e.getMessage(),
                    e
            );
        }
    }

    private PersonaJPA createPersonaJPA(final Tenant tenant, final Persona persona) throws BuilderValidationException {
        PersonaJPA result;

        try {
            result = (PersonaJPA) persona;
        } catch (ClassCastException e) {
            result = new PersonaJPABuilder()
                    .withPersona(persona)
                    .withTenantId(tenant.getUniqueId())
                    .build();
        }

        return result;
    }

    @Override
    public Optional<PersonaJPA> retrieve(final Tenant tenant, final UUID uniqueId) {
        PersonaJPA result;

        try {
            result = em.find(PersonaJPA.class, uniqueId);
        } catch (IllegalArgumentException e) {
            LOG.warn(e.getClass().getSimpleName() + ": " + e.getMessage(), e);

            throw new PersistenceRuntimeException(PersonaJPA.class, e.getMessage(), e);
        }

        if (result != null && result.getTenantId().equals(tenant.getUniqueId())) {
            return Optional.of(result);
        }

        return Optional.empty();
    }

    @Override
    public Optional<PersonaJPA> retrieve(final Tenant tenant, final String uniqueName) {
        PersonaJPA result = null;

        try {
            result = em
                    .createNamedQuery("persona-by-name", PersonaJPA.class)
                    .setParameter("tenant", tenant.getUniqueId())
                    .setParameter("name", uniqueName)
                    .getSingleResult();
        } catch (NoResultException | NonUniqueResultException | IllegalStateException e) {
            LOG.warn(e.getClass().getSimpleName() + " during retrieval: " + e.getMessage(), e);

            // Do nothing. Will return null.
        } catch (PersistenceException e) {
            LOG.error(e.getClass().getSimpleName() + " during retrieval: " + e.getMessage(), e);

            throw new PersistenceRuntimeException(PersonaJPA.class, "Caught a " + e.getClass()
                                                                                   .getSimpleName() + ": " + e
                    .getMessage());
        }

        return Optional.ofNullable(result);
    }

    @Override
    public Set<Persona> retrieve(final Tenant tenant) {
        final HashSet<Persona> result = new HashSet<>();

        try {
            em
                    .createNamedQuery("personas", PersonaJPA.class)
                    .setParameter("tenant", tenant.getUniqueId())
                    .getResultList()
                    .forEach(result::add);
        } catch (IllegalStateException e) {
            LOG.warn(e.getClass().getSimpleName() + " during retrieval: " + e.getMessage(), e);

            // do nothing more. Will return an empty result.
        } catch (PersistenceException e) {
            LOG.error(e.getClass().getSimpleName() + " during retrieval: " + e.getMessage(), e);

            throw new PersistenceRuntimeException(PersonaJPA.class, "Caught a " + e.getClass()
                                                                                   .getSimpleName() + ": " + e
                    .getMessage());
        }

        //noinspection unchecked
        return result;
    }

    @Override
    public Page<Persona> retrieve(final Tenant tenant, final PageRequest pageRequest) {
        final ArrayList<Persona> result = new ArrayList<>((int) pageRequest.getPageSize());

        TypedQuery<PersonaJPA> query = em
                .createNamedQuery("personas", PersonaJPA.class)
                .setParameter("tenant", tenant.getUniqueId());

        int elementCount = query.getMaxResults();

        if (elementCount > 0) {
            retrieveResultsFromJPA(tenant, pageRequest, result);
        }

        try {
            return new PageBuilder<Persona>()
                    .withData(result)
                    .withRequest(pageRequest)
                    .withTotalElements(elementCount)
                    .build();

        } catch (BuilderValidationException e) {
            LOG.error(e.getClass().getSimpleName() + " caught: " + e.getMessage(), e);

            throw new PersistenceRuntimeException(PersonaJPA.class, e.getClass()
                                                                     .getSimpleName() + " caught: " + e.getMessage());
        }
    }

    private void retrieveResultsFromJPA(final Tenant tenant, final PageRequest pageRequest, ArrayList<Persona> result) {
        try {
            em
                    .createNamedQuery("personas", PersonaJPA.class)
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

            throw new PersistenceRuntimeException(PersonaJPA.class, "Caught a " + e.getClass()
                                                                                   .getSimpleName() + ": " + e
                    .getMessage());
        }
    }

    private int calculateStartPosition(final PageRequest pageRequest) {
        return (int) ((pageRequest.getPageNumber() - 1) * pageRequest.getPageSize());
    }

    @Override
    public PersonaJPA update(final Tenant tenant, Persona data) throws DuplicateUniqueNameException, DuplicateUniqueIdException {
        try {
            return update(tenant, createPersonaJPA(tenant, data));
        } catch (BuilderValidationException e) {
            LOG.error(e.getClass().getSimpleName() + " caught: " + e.getMessage(), e);

            throw new PersistenceRuntimeException(
                    PersonaJPA.class,
                    e.getClass().getSimpleName() + " caught: " + e.getMessage(),
                    e
            );
        }
    }


    private PersonaJPA update(final Tenant tenant, final PersonaJPA data) throws DuplicateUniqueNameException, DuplicateUniqueIdException {
        try {
            return em.merge(data);
        } catch (IllegalArgumentException e) {
            return create(tenant, data);
        } catch (TransactionRequiredException e) {
            LOG.error(e.getClass().getSimpleName() + " caught: " + e.getMessage(), e);

            throw new PersistenceRuntimeException(PersonaJPA.class, e.getClass()
                                                                     .getSimpleName() + " caught: " + e.getMessage());
        }
    }

    @Override
    public void delete(final Tenant tenant, final Persona data) {
        delete(tenant, data.getUniqueId());
    }


    @Override
    public void delete(final Tenant tenant, final UUID uniqueId) {
        retrieve(tenant, uniqueId).ifPresent(data -> delete(data, data.getUniqueId()));
    }

    private void delete(PersonaJPA data, final Object errorMessageData) {
        if (data != null) {
            try {
                em.remove(data);

                LOG.info("Deleted persona: {}", data);
            } catch (IllegalArgumentException e) {
                LOG.warn("Tried to delete persona. But it did not exist: {}", data);
            } catch (TransactionRequiredException e) {
                LOG.error(e.getClass().getSimpleName() + " caught: " + e.getMessage(), e);

                throw new PersistenceRuntimeException(
                        PersonaJPA.class,
                        e.getClass().getSimpleName() + " caught: " + e.getMessage()
                );
            }
        } else {
            LOG.warn("Tried to delete persona. But its unique id is not in the database: {}", errorMessageData);
        }
    }

    @Override
    public void delete(final Tenant tenant, final String uniqueName) {
        retrieve(tenant, uniqueName).ifPresent(data -> delete(data, data.getUniqueId()));
    }
}
