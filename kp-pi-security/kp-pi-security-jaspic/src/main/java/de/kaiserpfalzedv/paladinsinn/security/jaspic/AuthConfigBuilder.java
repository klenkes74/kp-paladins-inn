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
import javax.security.auth.message.config.AuthConfig;
import javax.security.auth.message.config.AuthConfigFactory;

import de.kaiserpfalzedv.paladinsinn.commons.api.Builder;
import de.kaiserpfalzedv.paladinsinn.commons.api.BuilderValidationException;
import de.kaiserpfalzedv.paladinsinn.security.jaspic.client.SecurityClientAuthConfig;
import de.kaiserpfalzedv.paladinsinn.security.jaspic.server.SecurityServerAuthConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2017-04-01
 */
public class AuthConfigBuilder<T extends AuthConfig> implements Builder<T> {
    private static final Logger LOG = LoggerFactory.getLogger(AuthConfigBuilder.class);

    private AuthConfigFactory factory;
    private Map<String, String> properties;

    private boolean serverAuthConfig = true;

    private String layer;
    private String appContext;
    private CallbackHandler handler;


    public AuthConfigBuilder(final AuthConfigFactory factory, final Map<String, String> properties) {
        LOG.debug("***** Creating {}: factory={}, properties={}", this, factory, properties);
        properties.forEach((k, v) -> LOG.trace("      Property {}: {}", k, v));

        this.factory = factory;
        this.properties = properties;
    }


    @SuppressWarnings("unchecked")
    @Override
    public T build() throws BuilderValidationException {
        LOG.trace("Building new client authentication context: layer={}, appContext={}, handler={}",
                  layer, appContext, handler
        );

        T result;
        if (serverAuthConfig) {
            result = (T) new SecurityServerAuthConfig();
        } else {
            result = (T) new SecurityClientAuthConfig();
        }

        return result;
    }

    public SecurityClientAuthConfig buildClientAuthConfig() throws BuilderValidationException {
        client();

        return (SecurityClientAuthConfig) build();
    }

    public SecurityServerAuthConfig buildServerAuthConfig() throws BuilderValidationException {
        server();

        return (SecurityServerAuthConfig) build();
    }

    @Override
    public void validate() throws BuilderValidationException {
        // TODO klenkes Auto defined stub for: de.kaiserpfalzedv.paladinsinn.security.jaspic.client.AuthConfigBuilder.validate
    }

    public AuthConfigBuilder server() {
        serverAuthConfig = true;
        return this;
    }

    public AuthConfigBuilder client() {
        serverAuthConfig = false;
        return this;
    }

    public AuthConfigBuilder withLayer(final String layer) {
        this.layer = layer;
        return this;
    }

    public AuthConfigBuilder withAppContext(final String appContext) {
        this.appContext = appContext;
        return this;
    }

    public AuthConfigBuilder withHandler(final CallbackHandler handler) {
        this.handler = handler;
        return this;
    }
}
