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

package de.kaiserpfalzedv.paladinsinn.commons.api.service;

import java.lang.annotation.Annotation;
import java.util.Collection;

import de.kaiserpfalzedv.paladinsinn.commons.api.PaladinsInnBaseException;

/**
 * The class could not be loaded via the {@link ServiceSelector} with the given annotations.
 *
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2017-04-02
 */
public class ClassNotLoadableException extends PaladinsInnBaseException {
    private static final long serialVersionUID = -6796682380861891245L;


    private Class<?> clasz;
    private Collection<Annotation> annotations;

    public ClassNotLoadableException(Class<?> clasz, Collection<Annotation> annotations) {
        super("No class of type " + clasz.getSimpleName() + " found with annotations: " + annotations);
    }


    public Class<?> getClasz() {
        return clasz;
    }

    public Collection<Annotation> getAnnotations() {
        return annotations;
    }
}
