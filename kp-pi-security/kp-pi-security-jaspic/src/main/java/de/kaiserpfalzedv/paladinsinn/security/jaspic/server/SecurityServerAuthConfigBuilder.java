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

import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.message.config.AuthConfigFactory;

import de.kaiserpfalzedv.paladinsinn.commons.api.Builder;
import de.kaiserpfalzedv.paladinsinn.commons.api.BuilderValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2017-04-01
 */
public class SecurityServerAuthConfigBuilder implements Builder<SecurityServerAuthConfig> {
    private static final Logger LOG = LoggerFactory.getLogger(SecurityServerAuthConfigBuilder.class);

    private AuthConfigFactory factory;
    private Map<String, String> properties;

    private String layer;
    private String appContext;
    private CallbackHandler handler;


    public SecurityServerAuthConfigBuilder(final AuthConfigFactory factory, final Map<String, String> properties) {
        LOG.debug("***** Creating {}: factory={}, properties={}", this, factory, properties);
        properties.forEach((k, v) -> LOG.trace("      Property {}: {}", k, v));

        this.factory = factory;
        this.properties = properties;
    }


    @Override
    public SecurityServerAuthConfig build() throws BuilderValidationException {
        LOG.trace("Building new client authentication context: layer={}, appContext={}, handler={}",
                  layer, appContext, handler
        );

        return new SecurityServerAuthConfig();
    }

    @Override
    public void validate() throws BuilderValidationException {
        // TODO klenkes Auto defined stub for: de.kaiserpfalzedv.paladinsinn.security.jaspic.client.SecurityServerAuthConfigBuilder.validate
    }

    public SecurityServerAuthConfigBuilder withLayer(final String layer) {
        this.layer = layer;
        return this;
    }

    public SecurityServerAuthConfigBuilder withAppContext(final String appContext) {
        this.appContext = appContext;
        return this;
    }

    public SecurityServerAuthConfigBuilder withHandler(final CallbackHandler handler) {
        this.handler = handler;
        return this;
    }
}
