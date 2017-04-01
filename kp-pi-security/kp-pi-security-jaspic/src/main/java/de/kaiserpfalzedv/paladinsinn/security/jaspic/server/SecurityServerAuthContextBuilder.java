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

import de.kaiserpfalzedv.paladinsinn.commons.api.Builder;
import de.kaiserpfalzedv.paladinsinn.commons.api.BuilderValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2017-04-01
 */
public class SecurityServerAuthContextBuilder implements Builder<SecurityServerAuthContext> {
    private static final Logger LOG = LoggerFactory.getLogger(SecurityServerAuthContextBuilder.class);

    private String authContextId;
    private Subject clientSubject;
    private Map properties;

    @Override
    public SecurityServerAuthContext build() throws BuilderValidationException {
        LOG.trace("Building new client authentication context: authContextId={}, clientSubject={}, properties={}",
                  authContextId, clientSubject, properties
        );
        properties.forEach((k, v) -> LOG.trace("      property {}: {}", k, v));

        return new SecurityServerAuthContext();
    }

    @Override
    public void validate() throws BuilderValidationException {
        // TODO klenkes Auto defined stub for: de.kaiserpfalzedv.paladinsinn.security.jaspic.client.SecuityServerAuthContextBuilder.validate
    }

    public SecurityServerAuthContextBuilder withAuthContextId(final String authContextId) {
        this.authContextId = authContextId;
        return this;
    }

    public SecurityServerAuthContextBuilder withServiceSubject(final Subject clientSubject) {
        this.clientSubject = clientSubject;
        return this;
    }

    public SecurityServerAuthContextBuilder withProperties(final Map properties) {
        this.properties = properties;
        return this;
    }
}
