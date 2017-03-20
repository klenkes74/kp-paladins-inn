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

package de.kaiserpfalzedv.paladinsinn.commons.persistence;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import de.kaiserpfalzedv.paladinsinn.commons.paging.Page;
import de.kaiserpfalzedv.paladinsinn.commons.paging.PageRequest;
import de.kaiserpfalzedv.paladinsinn.commons.tenant.model.Tenant;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2017-03-20
 */
public interface TenantCrudService<T extends Identifiable> {
    /**
     * @param data the user data to be saved.
     *
     * @return the saved user data.
     */
    T create(Tenant tenant, T data) throws DuplicateEntityException;

    /**
     * Loads a single user by an unique id.
     *
     * @param uniqueId the user id of the user to be loaded.
     *
     * @return The selected user.
     */
    Optional<T> retrieve(Tenant tenant, UUID uniqueId);

    /**
     * Loads a single user by the user name.
     *
     * @param uniqueName the user name of the user to be loaded.
     *
     * @return The selected user.
     */
    Optional<T> retrieve(Tenant tenant, String uniqueName);

    /**
     * @return all users of the system.
     */
    Set<T> retrieve(Tenant tenant);

    /**
     * @param pageRequest the definition which part of the result to retrieve.
     *
     * @return the sublist of users given by the page request informatio.
     */
    Page<T> retrieve(Tenant tenant, PageRequest pageRequest);

    /**
     * changes the user data i the store.
     *
     * @param data new data to save.
     *
     * @return the saved user data.
     */
    T update(Tenant tenant, T data);

    /**
     * @param data the user to be deleted.
     */
    void delete(Tenant tenant, T data);

    /**
     * @param uniqueId the user id of the user to be deleted.
     */
    void delete(Tenant tenant, UUID uniqueId);

    /**
     * @param uniqueName the user name of the user to be dleted.
     */
    void delete(Tenant tenant, String uniqueName);
}
