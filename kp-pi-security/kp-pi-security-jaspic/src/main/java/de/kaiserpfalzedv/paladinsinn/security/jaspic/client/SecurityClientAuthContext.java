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

package de.kaiserpfalzedv.paladinsinn.security.jaspic.client;

import javax.security.auth.Subject;
import javax.security.auth.message.AuthException;
import javax.security.auth.message.AuthStatus;
import javax.security.auth.message.MessageInfo;
import javax.security.auth.message.config.ClientAuthContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2017-04-01
 */
public class SecurityClientAuthContext implements ClientAuthContext {
    private static final Logger LOG = LoggerFactory.getLogger(SecurityClientAuthContext.class);

    @Override
    public AuthStatus secureRequest(MessageInfo messageInfo, Subject clientSubject) throws AuthException {
        LOG.debug("{}:secureRequest called: messageInfo={}, clientSubjet={}", this, messageInfo, clientSubject);

        // TODO klenkes Auto defined stub for: de.kaiserpfalzedv.paladinsinn.security.jaspic.client.SecurityClientAuthContext.secureRequest

        return AuthStatus.SEND_CONTINUE;
    }

    @Override
    public AuthStatus validateResponse(MessageInfo messageInfo, Subject clientSubject, Subject serviceSubject) throws AuthException {
        LOG.debug("{}:validateResponse called: messageInfo={}, clientSubject={}, serviceSubject={}", this, messageInfo, clientSubject, serviceSubject);

        // TODO klenkes Auto defined stub for: de.kaiserpfalzedv.paladinsinn.security.jaspic.client.SecurityClientAuthContext.validateResponse

        return AuthStatus.SEND_CONTINUE;
    }

    @Override
    public void cleanSubject(MessageInfo messageInfo, Subject subject) throws AuthException {
        LOG.debug("{}:cleanSubject called: messageInfo={}, subject={}", this, messageInfo, subject);

        // TODO klenkes Auto defined stub for: de.kaiserpfalzedv.paladinsinn.security.jaspic.client.SecurityClientAuthContext.cleanSubject
    }
}
