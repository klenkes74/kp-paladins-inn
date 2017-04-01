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

import java.util.Map;

import javax.security.auth.Subject;
import javax.security.auth.message.AuthException;
import javax.security.auth.message.MessageInfo;
import javax.security.auth.message.config.ServerAuthConfig;
import javax.security.auth.message.config.ServerAuthContext;

import de.kaiserpfalzedv.paladinsinn.commons.api.BuilderValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2017-04-01
 */
public class SecurityServerAuthConfig implements ServerAuthConfig {
    private static final Logger LOG = LoggerFactory.getLogger(SecurityServerAuthConfig.class);

    @Override
    public ServerAuthContext getAuthContext(String authContextID, Subject serviceSubject, Map properties) throws AuthException {
        LOG.debug("{}:getAuthContext called: authContextID={}, subject={}, properties={}", this, authContextID, serviceSubject, properties);
        properties.forEach((k, v) -> LOG.trace("      property {}: {}", k, v));

        try {
            return new SecurityServerAuthContextBuilder()
                    .withAuthContextId(authContextID)
                    .withServiceSubject(serviceSubject)
                    .withProperties(properties)
                    .build();
        } catch (BuilderValidationException e) {
            throw new AuthException(e.getName() + " caught: " + e.getMessage());
        }
    }

    @Override
    public String getMessageLayer() {
        LOG.debug("{}:getMessageLayer called", this);

        // TODO klenkes Auto defined stub for: de.kaiserpfalzedv.paladinsinn.security.jaspic.server.SecurityServerAuthConfig.getMessageLayer

        return null;
    }

    @Override
    public String getAppContext() {
        LOG.debug("{}:getAppContext called", this);

        // TODO klenkes Auto defined stub for: de.kaiserpfalzedv.paladinsinn.security.jaspic.server.SecurityServerAuthConfig.getAppContext

        return null;
    }

    @Override
    public String getAuthContextID(MessageInfo messageInfo) {
        LOG.debug("{}:getAuthContextID called: messageInfo={}", this, messageInfo);

        // TODO klenkes Auto defined stub for: de.kaiserpfalzedv.paladinsinn.security.jaspic.server.SecurityServerAuthConfig.getAuthContextID

        return null;
    }

    @Override
    public void refresh() {
        LOG.debug("{}:refresh called", this);

        // TODO klenkes Auto defined stub for: de.kaiserpfalzedv.paladinsinn.security.jaspic.server.SecurityServerAuthConfig.refresh
    }

    @Override
    public boolean isProtected() {
        LOG.debug("{}:isProtected called", this);

        // TODO klenkes Auto defined stub for: de.kaiserpfalzedv.paladinsinn.security.jaspic.server.SecurityServerAuthConfig.isProtected

        return false;
    }
}
