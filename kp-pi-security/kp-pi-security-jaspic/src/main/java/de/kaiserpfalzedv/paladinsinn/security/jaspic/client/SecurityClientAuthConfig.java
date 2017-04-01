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

import java.util.Map;

import javax.security.auth.Subject;
import javax.security.auth.message.AuthException;
import javax.security.auth.message.MessageInfo;
import javax.security.auth.message.config.ClientAuthConfig;
import javax.security.auth.message.config.ClientAuthContext;

import de.kaiserpfalzedv.paladinsinn.commons.api.BuilderValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2017-04-01
 */
public class SecurityClientAuthConfig implements ClientAuthConfig {
    private static final Logger LOG = LoggerFactory.getLogger(SecurityClientAuthConfig.class);

    @Override
    public ClientAuthContext getAuthContext(String authContextID, Subject clientSubject, Map properties) throws AuthException {
        LOG.debug("{}:getAuthContext called: authContextID={}, clientSubject={}, properties={}", this, authContextID, clientSubject, properties);
        properties.forEach((k, v) -> LOG.trace("      property {}: {}", k, v));

        try {
            return new SecurityClientAuthContextBuilder()
                    .withAuthContextId(authContextID)
                    .withClientSubject(clientSubject)
                    .withProperties(properties)
                    .build();
        } catch (BuilderValidationException e) {
            throw new AuthException(e.getName() + " caught: " + e.getMessage());
        }
    }

    @Override
    public String getMessageLayer() {
        LOG.debug("{}:getMessageLayer called", this);

        // TODO klenkes Auto defined stub for: de.kaiserpfalzedv.paladinsinn.security.jaspic.client.SecurityClientAuthConfig.getMessageLayer

        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getAppContext() {
        LOG.debug("{}:getAppContext called", this);

        // TODO klenkes Auto defined stub for: de.kaiserpfalzedv.paladinsinn.security.jaspic.client.SecurityClientAuthConfig.getAppContext

        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getAuthContextID(MessageInfo messageInfo) {
        LOG.debug("{}:getAuthContextID called: messageInfo={}", this, messageInfo);

        // TODO klenkes Auto defined stub for: de.kaiserpfalzedv.paladinsinn.security.jaspic.client.SecurityClientAuthConfig.getAuthContextID

        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void refresh() {
        LOG.debug("{}:refresh called", this);

        // TODO klenkes Auto defined stub for: de.kaiserpfalzedv.paladinsinn.security.jaspic.client.SecurityClientAuthConfig.refresh
    }

    @Override
    public boolean isProtected() {
        LOG.debug("{}:isProtected called", this);

        // TODO klenkes Auto defined stub for: de.kaiserpfalzedv.paladinsinn.security.jaspic.client.SecurityClientAuthConfig.isProtected

        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
