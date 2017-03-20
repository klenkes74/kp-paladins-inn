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

package de.kaiserpfalzedv.paladinsinn.security.access.store;

import de.kaiserpfalzedv.paladinsinn.security.access.SecurityRuntimeException;

/**
 * The unchecked persistence exception of the security classes. For runtime errors like networking problems or so this
 * exception should be used.
 * <p>
 * For business failures (like duplicate keys) {@link SecurityPersistenceException} is the correct choice.
 *
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2017-03-19
 */
public class SecurityPersistenceRuntimeException extends SecurityRuntimeException {
    private static final long serialVersionUID = -4777561056547581529L;

    private final Class<?> clasz;

    /**
     * @param clasz The class this exception is thrown for.
     * @param message the failure message.
     */
    public SecurityPersistenceRuntimeException(final Class<?> clasz, final String message) {
        super(String.format("%s (Class: %s)", message, clasz.getCanonicalName()));

        this.clasz = clasz;
    }

    /**
     * @param clasz The class this exception is thrown for.
     * @param message the failure message.
     * @param cause   the failure cause.
     */
    public SecurityPersistenceRuntimeException(final Class<?> clasz, final String message, final Throwable cause) {
        super(String.format("%s (Class: %s)", message, clasz.getCanonicalName()), cause);

        this.clasz = clasz;
    }

    /**
     * @param clasz The class this exception is thrown for.
     * @param message            the failure message.
     * @param cause              the failure cause.
     * @param enableSuppression
     * @param writableStackTrace
     */
    public SecurityPersistenceRuntimeException(
            final Class<?> clasz,
            final String message,
            final Throwable cause,
            final boolean enableSuppression,
            final boolean writableStackTrace
    ) {
        super(
                String.format("%s (Class: %s)", message, clasz.getCanonicalName()),
                cause,
                enableSuppression,
                writableStackTrace
        );

        this.clasz = clasz;
    }
}
