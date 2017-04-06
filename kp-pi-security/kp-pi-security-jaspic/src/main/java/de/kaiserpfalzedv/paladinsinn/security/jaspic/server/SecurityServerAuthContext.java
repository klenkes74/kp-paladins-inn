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

package de.kaiserpfalzedv.paladinsinn.security.jaspic.server;

import java.util.Set;

import javax.security.auth.Subject;
import javax.security.auth.message.AuthException;
import javax.security.auth.message.AuthStatus;
import javax.security.auth.message.MessageInfo;
import javax.security.auth.message.config.ServerAuthContext;

import de.kaiserpfalzedv.paladinsinn.security.api.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2017-04-01
 */
public class SecurityServerAuthContext implements ServerAuthContext {
    private static final Logger LOG = LoggerFactory.getLogger(SecurityServerAuthContext.class);


    @Override
    public AuthStatus validateRequest(MessageInfo messageInfo, Subject clientSubject, Subject serviceSubject) throws AuthException {
        LOG.debug("{}:validateRequest called: messageInfo={}, clientSubject={}, serviceSubject={}",
                  this, messageInfo, clientSubject, serviceSubject
        );


        // TODO klenkes Auto defined stub for: de.kaiserpfalzedv.paladinsinn.security.jaspic.server.SecurityServerAuthContext.validateRequest

        return AuthStatus.SEND_CONTINUE;
    }

    @Override
    public AuthStatus secureResponse(MessageInfo messageInfo, Subject serviceSubject) throws AuthException {
        LOG.debug("{}:secureResponse called: messageInfo={}, serviceSubject={}",
                  this, messageInfo, serviceSubject
        );

        // TODO klenkes Auto defined stub for: de.kaiserpfalzedv.paladinsinn.security.jaspic.server.SecurityServerAuthContext.secureResponse

        return AuthStatus.SEND_CONTINUE;
    }

    @Override
    public void cleanSubject(MessageInfo messageInfo, Subject subject) throws AuthException {
        LOG.debug("{}:cleanSubject called: messageInfo={}, subject={}", this, messageInfo, subject);
        LOG.trace("      working on subject with principals: {}", subject.getPrincipals());
        LOG.trace("      Is request: {}", messageInfo.getRequestMessage() != null);
        messageInfo.getMap().forEach((k, v) -> LOG.trace("      messageInfo[{}]: {}", k, v));

        removeAllUsersFromPrincipals(subject);
    }

    private void removeAllUsersFromPrincipals(Subject subject) {
        Set<User> principalsToRemove = subject.getPrincipals(User.class);
        LOG.debug("      Removing principals: {}", principalsToRemove);
        subject.getPrincipals().removeAll(principalsToRemove);
    }
}
