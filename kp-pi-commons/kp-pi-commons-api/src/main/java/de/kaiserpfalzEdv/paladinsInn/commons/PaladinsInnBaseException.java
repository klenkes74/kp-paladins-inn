/*
 * Copyright 2016 Kaiserpfalz EDV-Service, Roland T. Lichti
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

package de.kaiserpfalzEdv.paladinsInn.commons;

import java.util.UUID;

/**
 * The base exception for all checked exceptions of the Paladin's Inn Topic Hub.
 *
 * @author rlichti {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 2016-03-20
 */
public class PaladinsInnBaseException extends Exception implements IdentityWritable {
    /**
     * Unique identifier of this exception.
     */
    private UUID identifier = UUID.randomUUID();


    public PaladinsInnBaseException(final String message) {
        super(message);
    }

    public PaladinsInnBaseException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public PaladinsInnBaseException(final Throwable cause) {
        super(cause);
    }

    public PaladinsInnBaseException(final String message, final Throwable cause,
                                    final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }


    @Override
    public UUID getIdentifier() {
        return identifier;
    }

    @Override
    public void setIdentity(final UUID id) {
        this.identifier = id;
    }
}
