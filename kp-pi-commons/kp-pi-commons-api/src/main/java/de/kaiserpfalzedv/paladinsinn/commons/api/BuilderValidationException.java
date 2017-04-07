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

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * The validation during using a {@link Builder} failed.
 *
 * @author rlichti {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 2016-03-20
 */
public class BuilderValidationException extends PaladinsInnBaseRuntimeException {
    private static final long serialVersionUID = -4806737667677367796L;
    
    private final HashSet<String> failures = new HashSet<>();


    public BuilderValidationException(final Class<?> clasz, final Set<String> failures) {
        super("Can't build object of type: " + clasz.getCanonicalName());

        if (failures != null) {
            this.failures.addAll(failures);
        }
    }


    /**
     * @return unmodifiable set of failures as string messages.
     */
    public Set<String> getFailures() {
        return Collections.unmodifiableSet(failures);
    }
}
