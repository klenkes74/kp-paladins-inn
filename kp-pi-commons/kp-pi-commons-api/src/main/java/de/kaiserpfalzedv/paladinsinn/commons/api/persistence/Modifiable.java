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

package de.kaiserpfalzedv.paladinsinn.commons.api.persistence;

import java.time.OffsetDateTime;

/**
 * The data set tracks change timestamps for every record.
 *
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2017-03-26
 */
public interface Modifiable {
    /**
     * @return The JPA managed version number of this record.
     */
    long getVersion();

    /**
     * @return The date this record has been created.
     */
    OffsetDateTime getCreated();

    /**
     * @return The timestamp this record has been lastly changed.
     */
    OffsetDateTime getChanged();
}
