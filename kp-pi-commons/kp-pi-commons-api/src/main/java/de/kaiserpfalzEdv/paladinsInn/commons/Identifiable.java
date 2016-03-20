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

import java.io.Serializable;
import java.util.UUID;

/**
 * An identifiable object.
 *
 * @author rlichti {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 2016-03-18
 */
public interface Identifiable extends Serializable {
    /**
     * @return The unique ID of this object.
     */
    UUID getIdentifier();
}
