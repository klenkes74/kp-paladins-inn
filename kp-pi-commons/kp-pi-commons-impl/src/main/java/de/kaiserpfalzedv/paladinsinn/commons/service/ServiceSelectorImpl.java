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

package de.kaiserpfalzedv.paladinsinn.commons.service;

import java.util.ServiceLoader;

import de.kaiserpfalzedv.paladinsinn.commons.api.service.MockService;
import de.kaiserpfalzedv.paladinsinn.commons.api.service.ServiceSelector;
import de.kaiserpfalzedv.paladinsinn.commons.api.service.WorkerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2017-04-02
 */
public class ServiceSelectorImpl<T> implements ServiceSelector<T> {
    private static final Logger LOG = LoggerFactory.getLogger(ServiceSelector.class);

    @Override
    public T loadWorker(final Class<T> clasz) throws ClassNotFoundException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        LOG.debug("Trying to load worker {} from class loader {}", clasz, classLoader);

        for (T c : ServiceLoader.load(clasz, classLoader)) {
            if (c.getClass().isAnnotationPresent(WorkerService.class)) {
                return c;
            }
        }

        LOG.debug("No matching worker object of type {} found!", clasz.getCanonicalName());
        throw new ClassNotFoundException("No matching worker class of type " + clasz.getCanonicalName() + " found!");
    }


    @Override
    public T loadMock(final Class<T> clasz) throws ClassNotFoundException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        LOG.debug("Trying to load mock {} from class loader {}", clasz, classLoader);

        for (T c : ServiceLoader.load(clasz, classLoader)) {
            if (c.getClass().isAnnotationPresent(MockService.class)) {
                return c;
            }
        }

        LOG.debug("No matching mock object of type {} found!", clasz.getCanonicalName());
        throw new ClassNotFoundException("No matching mock class of type " + clasz.getCanonicalName() + " found!");
    }
}
