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

package de.kaiserpfalzedv.paladinsinn.security.jaspic;

import java.util.Map;

import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.message.AuthException;
import javax.security.auth.message.config.AuthConfigFactory;
import javax.security.auth.message.config.AuthConfigProvider;
import javax.security.auth.message.config.ClientAuthConfig;
import javax.security.auth.message.config.RegistrationListener;
import javax.security.auth.message.config.ServerAuthConfig;

import de.kaiserpfalzedv.paladinsinn.commons.api.BuilderValidationException;
import de.kaiserpfalzedv.paladinsinn.security.jaspic.client.SecurityClientAuthConfigBuilder;
import de.kaiserpfalzedv.paladinsinn.security.jaspic.server.SecurityServerAuthConfigBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2017-04-01
 */
public class SecurityAuthConfigProvider implements AuthConfigProvider, RegistrationListener {
    private static final Logger LOG = LoggerFactory.getLogger(SecurityAuthConfigProvider.class);

    private AuthConfigFactory factory;
    private Map<String, String> properties;


    public SecurityAuthConfigProvider(Map<String, String> properties, AuthConfigFactory factory) {
        LOG.info("***** Creating: {}", this);
        LOG.trace("      authentication configuration factory: {}", factory);
        properties.forEach((k, v) -> LOG.trace("      property {}: {}", k, v));

        this.factory = factory;
        this.properties = properties;
    }


    @Override
    public ClientAuthConfig getClientAuthConfig(String layer, String appContext, CallbackHandler handler) throws AuthException {
        LOG.debug("{}:getClientAuthConfig: layer={}, appContext={}, handler={}", this, layer, appContext, handler);

        try {
            return new SecurityClientAuthConfigBuilder(factory, properties)
                    .withLayer(layer)
                    .withAppContext(appContext)
                    .withHandler(handler)
                    .build();
        } catch (BuilderValidationException e) {
            LOG.error(e.getClass().getSimpleName() + " caught: " + e.getMessage(), e);

            throw new AuthException(e.getName() + " caught: " + e.getMessage());
        }
    }

    @Override
    public ServerAuthConfig getServerAuthConfig(String layer, String appContext, CallbackHandler handler) throws AuthException {
        LOG.debug("{}:getServerAuthConfig: layer={}, appContext={}, handler={}", this, layer, appContext, handler);

        try {
            return new SecurityServerAuthConfigBuilder(factory, properties)
                    .withLayer(layer)
                    .withAppContext(appContext)
                    .withHandler(handler)
                    .build();
        } catch (BuilderValidationException e) {
            LOG.error(e.getClass().getSimpleName() + " caught: " + e.getMessage(), e);

            throw new AuthException(e.getName() + " caught: " + e.getMessage());
        }
    }

    @Override
    public void refresh() {
        LOG.debug("{}:refresh()", this);

        // TODO klenkes Auto defined stub for: de.kaiserpfalzedv.paladinsinn.security.jaspic.SecurityAuthConfigProvider.refresh
    }

    @Override
    public void notify(String layer, String appContext) {
        LOG.debug("{}:notify called: layer={}, appContext={}", this, layer, appContext);

        // TODO klenkes Auto defined stub for: de.kaiserpfalzedv.paladinsinn.security.jaspic.SecurityAuthConfigProvider.notify
    }
}
