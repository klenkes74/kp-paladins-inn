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

package de.kaiserpfalzedv.paladinsinn.commons.api;

import java.util.UUID;

import de.kaiserpfalzedv.paladinsinn.commons.api.persistence.Nameable;

/**
 * The base exception for all unchecked exceptions of the Paladin's Inn Topic Hub.
 *
 * @author rlichti {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 2016-03-20
 */
public abstract class PaladinsInnBaseRuntimeException extends RuntimeException implements Nameable {
    private static final long serialVersionUID = 3952050672933252733L;
    
    /**
     * Unique identifier of this exception.
     */
    private UUID identifier = UUID.randomUUID();


    public PaladinsInnBaseRuntimeException(final String message) {
        super(message);
    }

    public PaladinsInnBaseRuntimeException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public PaladinsInnBaseRuntimeException(final Throwable cause) {
        super(cause);
    }

    public PaladinsInnBaseRuntimeException(final String message, final Throwable cause,
                                           final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }


    @Override
    public UUID getUniqueId() {
        return identifier;
    }

    @Override
    public String getName() {
        return getClass().getSimpleName();
    }
}
