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

import de.kaiserpfalzedv.paladinsinn.security.access.SecurityException;

/**
 * The security persistence exception will be thrown if there are problems with the persistence layer of the security
 * classes.
 *
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2017-03-19
 */
public class SecurityPersistenceException extends SecurityException {
    private static final long serialVersionUID = 5148039620770551316L;

    /**
     * The class this security exception is thrown for.
     */
    private final Class<?> clasz;

    /**
     * @param message the failure message.
     */
    public SecurityPersistenceException(Class<?> clasz, String message) {
        super(String.format("%s (Class: %s)", message, clasz.getCanonicalName()));

        this.clasz = clasz;
    }

    /**
     * @param message the failure message.
     * @param cause   the failure cause.
     */
    public SecurityPersistenceException(Class<?> clasz, String message, Throwable cause) {
        super(String.format("%s (Class: %s)", message, clasz.getCanonicalName()), cause);

        this.clasz = clasz;
    }

    /**
     * @param message            the failure message.
     * @param cause              the failure cause.
     * @param enableSuppression
     * @param writableStackTrace
     */
    public SecurityPersistenceException(
            Class<?> clasz, String message,
            Throwable cause,
            boolean enableSuppression,
            boolean writableStackTrace
    ) {
        super(
                String.format("%s (Class: %s)", message, clasz.getCanonicalName()),
                cause,
                enableSuppression,
                writableStackTrace
        );

        this.clasz = clasz;
    }

    public Class<?> getClasz() {
        return clasz;
    }
}
