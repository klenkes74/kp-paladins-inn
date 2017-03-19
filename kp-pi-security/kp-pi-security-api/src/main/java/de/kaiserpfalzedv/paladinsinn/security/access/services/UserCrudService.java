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

package de.kaiserpfalzedv.paladinsinn.security.access.services;

import java.util.Set;
import java.util.UUID;

import de.kaiserpfalzedv.paladinsinn.commons.paging.Page;
import de.kaiserpfalzedv.paladinsinn.commons.paging.PageRequest;
import de.kaiserpfalzedv.paladinsinn.security.access.model.User;

/**
 * This is the CRUD service for user data. It should be used by higher services to persist user data.
 *
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2017-03-18
 */
public interface UserCrudService {
    /**
     * @param user the user data to be saved.
     *
     * @return the saved user data.
     */
    User create(User user);


    /**
     * Loads a single user by an unique id.
     *
     * @param uniqueId the user id of the user to be loaded.
     *
     * @return The selected user.
     */
    User retrieve(UUID uniqueId);

    /**
     * Loads a single user by the user name.
     *
     * @param userName the user name of the user to be loaded.
     *
     * @return The selected user.
     */
    User retrieve(String userName);

    /**
     * @return all users of the system.
     */
    Set<User> retrieve();

    /**
     * @param pageRequest the definition which part of the result to retrieve.
     *
     * @return the sublist of users given by the page request informatio.
     */
    Page<User> retrieve(PageRequest pageRequest);


    /**
     * changes the user data i the store.
     *
     * @param user new user data to save.
     *
     * @return the saved user data.
     */
    User update(User user);


    /**
     * @param user the user to be deleted.
     */
    void delete(User user);

    /**
     * @param uniqueId the user id of the user to be deleted.
     */
    void delete(UUID uniqueId);

    /**
     * @param userName the user name of the user to be dleted.
     */
    void delete(String userName);
}
