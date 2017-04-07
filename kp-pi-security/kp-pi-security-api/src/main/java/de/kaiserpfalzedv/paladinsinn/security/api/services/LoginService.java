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

package de.kaiserpfalzedv.paladinsinn.security.api.services;

import java.util.Set;
import java.util.UUID;

import de.kaiserpfalzedv.paladinsinn.commons.api.tenant.model.Tenant;
import de.kaiserpfalzedv.paladinsinn.security.api.model.User;

/**
 * A service to log in an user. This is an internal service giving back very detailed failure information. For security
 * reasons the detailed descriptions should not be given to the users.
 *
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2017-03-14
 */
public interface LoginService {
    /**
     * Logs a user in for a special tenant.
     * 
     * @param tenant the tenant to log the user in.
     * @param userId the user id to log in.
     * @param password the password of the user to log in.
     * @return the user object.
     * @throws UserNotFoundException If there is no user with the id given.
     * @throws PasswordFailureException The password did not match the password of the user.
     * @throws UserIsLockedException The user is locked and can not log in.
     * @throws UserHasNoAccessToTenantException The user exists but is not entitled for the given tenant.
     */
    User login(Tenant tenant, String userId, String password)
            throws UserNotFoundException, PasswordFailureException, UserIsLockedException,
                   UserHasNoAccessToTenantException;

    /**
     * Logs a user in for a special tenant. The tenant is specified by its unique id.
     *
     * @param tenantId the tenant to log the user in.
     * @param userId   the user id to log in.
     * @param password the password of the user to log in.
     *
     * @return the user object.
     *
     * @throws UserNotFoundException            If there is no user with the id given.
     * @throws PasswordFailureException         The password did not match the password of the user.
     * @throws UserIsLockedException            The user is locked and can not log in.
     * @throws UserHasNoAccessToTenantException The user exists but is not entitled for the given tenant.
     */
    User login(UUID tenantId, String userId, String password)
            throws UserNotFoundException, PasswordFailureException, UserIsLockedException,
                   UserHasNoAccessToTenantException;

    /**
     * Logs a user in for a special tenant. The tenant is specified by its key.
     *
     * @param tenantKey the tenant to log the user in.
     * @param userId    the user id to log in.
     * @param password  the password of the user to log in.
     *
     * @return the user object.
     *
     * @throws UserNotFoundException            If there is no user with the id given.
     * @throws PasswordFailureException         The password did not match the password of the user.
     * @throws UserIsLockedException            The user is locked and can not log in.
     * @throws UserHasNoAccessToTenantException The user exists but is not entitled for the given tenant.
     */
    User login(String tenantKey, String userId, String password)
            throws UserNotFoundException, PasswordFailureException, UserIsLockedException,
                   UserHasNoAccessToTenantException;

    /**
     * Logs a user in without any tenant (that means with the
     * {@link de.kaiserpfalzedv.paladinsinn.commons.api.tenant.model.DefaultTenant}) or with the tenant specified by its
     * name.
     *
     * <p>The tenant can be specified within the userId:</p>
     *
     * <p>If the userId contains a sign <b>@</b>, the data is splitted and the first part is the real userId within the
     * tenant specified by its name after the @-sign.</p>
     *
     * <p>If the userId contains a sign <b>\</b>, the data is splitted and the first part is the {@link Tenant#getKey()}
     * of the tenant and the {@link User#getName()} is given after the |-sign.</p>
     *
     * @param userId   the user id to log in. May contain the tenant to be used either with the key or the full name.
     * @param password the password of the user to log in.
     *
     * @return the user object.
     *
     * @throws UserNotFoundException            If there is no user with the id given.
     * @throws PasswordFailureException         The password did not match the password of the user.
     * @throws UserIsLockedException            The user is locked and can not log in.
     * @throws UserHasNoAccessToTenantException The user exists but is not entitled for the given tenant.
     */
    User login(String userId, String password)
            throws UserNotFoundException, PasswordFailureException, UserIsLockedException,
                   UserHasNoAccessToTenantException;

    /**
     * @return an unmodifiable set of the valid tenants to be usable for login.
     */
    Set<? extends Tenant> availableTenants();
}
