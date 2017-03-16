package de.kaiserpfalzedv.paladinsinn.security.access.services;

import de.kaiserpfalzedv.paladinsinn.security.access.PasswordFailureException;
import de.kaiserpfalzedv.paladinsinn.security.access.UserHasNoAccessToTenantException;
import de.kaiserpfalzedv.paladinsinn.security.access.UserIsLockedException;
import de.kaiserpfalzedv.paladinsinn.security.access.UserNotFoundException;
import de.kaiserpfalzedv.paladinsinn.security.access.model.User;
import de.kaiserpfalzedv.paladinsinn.security.tenant.model.Tenant;

/**
 * @author rlichti {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 2017-03-15
 */
public class LoginServiceImpl implements LoginService {
    @Override
    public User login(String userId, String password) throws UserNotFoundException, PasswordFailureException, UserIsLockedException {
        return null;
    }

    @Override
    public User login(Tenant tenant, String userId, String password) throws UserNotFoundException, PasswordFailureException, UserIsLockedException, UserHasNoAccessToTenantException {
        return null;
    }
}
