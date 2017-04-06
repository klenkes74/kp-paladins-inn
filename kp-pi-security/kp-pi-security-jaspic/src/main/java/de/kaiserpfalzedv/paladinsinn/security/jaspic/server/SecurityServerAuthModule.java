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
import java.util.Set;

import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.message.AuthException;
import javax.security.auth.message.AuthStatus;
import javax.security.auth.message.MessageInfo;
import javax.security.auth.message.MessagePolicy;
import javax.security.auth.message.config.ServerAuthContext;
import javax.security.auth.message.module.ServerAuthModule;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.kaiserpfalzedv.paladinsinn.security.api.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static javax.security.auth.message.MessagePolicy.ProtectionPolicy.AUTHENTICATE_CONTENT;
import static javax.security.auth.message.MessagePolicy.ProtectionPolicy.AUTHENTICATE_SENDER;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2017-04-01
 */
public class SecurityServerAuthModule implements ServerAuthModule {
    private static final Logger LOG = LoggerFactory.getLogger(SecurityServerAuthModule.class);

    private MessagePolicy requestPolicy;
    private MessagePolicy responsePolicy;
    private CallbackHandler handler;
    private Map options;

    @Override
    public void initialize(
            final MessagePolicy requestPolicy,
            final MessagePolicy responsePolicy,
            final CallbackHandler handler,
            final Map options
    ) throws AuthException {
        LOG.debug("{}:initialize called: requestPolicy={}, responsePolicy={}, handler={}, options={}",
                  this, requestPolicy, responsePolicy, handler, options
        );
        options.forEach((k, v) -> LOG.trace("      property {}: {}", k, v));

        this.requestPolicy = requestPolicy;
        this.responsePolicy = responsePolicy;
        this.handler = handler;
        this.options = options;
    }

    @Override
    public Class[] getSupportedMessageTypes() {
        LOG.debug("{}:getSupportedMessageTypes called", this);

        return new Class[]{HttpServletRequest.class, HttpServletResponse.class};
    }

    @Override
    public AuthStatus validateRequest(final MessageInfo messageInfo, Subject clientSubject, Subject serviceSubject) throws AuthException {
        LOG.debug("{}:validateRequest called: messageInfo={}, clientSubject={}, serviceSubject={}",
                  this, messageInfo, clientSubject, serviceSubject
        );
        LOG.trace("      Is request: {}", messageInfo.getRequestMessage() != null);
        messageInfo.getMap().forEach((k, v) -> LOG.trace("      messageInfo[{}]: {}", k, v));

        if (requestPolicy.isMandatory()) {
            MessagePolicy.TargetPolicy[] policies = requestPolicy.getTargetPolicies();

            for (int i = 0; i < policies.length; i++) {
                MessagePolicy.TargetPolicy p = policies[i];

                ServerAuthContext[] contexts = (ServerAuthContext[]) p.getTargets();
                for (int j = 0; j < contexts.length; j++) {
                    ServerAuthContext c = contexts[j];
                    LOG.trace("Working on: {}:{}", p, c);

                    switch (p.getProtectionPolicy().getID()) {
                        case AUTHENTICATE_SENDER:
                            LOG.info("{}:{}:{} authenticating sender ...", this, p, c);
                            break;
                        case AUTHENTICATE_CONTENT:
                            LOG.info("{}:{}:{} authenticating content ...", this, p, c);
                            break;
                        default:
                            LOG.info("{}:{}:{} do not know what to do with protection policy type: {}", this, p, c, p.getProtectionPolicy()
                                                                                                                     .getID());
                    }
                }
            }
        }

        return AuthStatus.SEND_CONTINUE;
    }

    @Override
    public AuthStatus secureResponse(final MessageInfo messageInfo, Subject serviceSubject) throws AuthException {
        LOG.debug("{}:secureResponse called: messageInfo={}, serviceSubject={}", this, messageInfo, serviceSubject);
        LOG.trace("      Is request: {}", messageInfo.getRequestMessage() != null);
        messageInfo.getMap().forEach((k, v) -> LOG.trace("      messageInfo[{}]: {}", k, v));

        // TODO klenkes Auto defined stub for: de.kaiserpfalzedv.paladinsinn.security.jaspic.server.SecurityServerAuthModule.secureResponse

        return AuthStatus.SEND_CONTINUE;
    }

    @Override
    public void cleanSubject(final MessageInfo messageInfo, Subject subject) throws AuthException {
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
