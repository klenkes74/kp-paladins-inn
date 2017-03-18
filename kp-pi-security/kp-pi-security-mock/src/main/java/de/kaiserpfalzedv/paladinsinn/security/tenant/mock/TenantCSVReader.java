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

package de.kaiserpfalzedv.paladinsinn.security.tenant.mock;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.UUID;

import de.kaiserpfalzedv.paladinsinn.commons.BuilderValidationException;
import de.kaiserpfalzedv.paladinsinn.commons.DataReader;
import de.kaiserpfalzedv.paladinsinn.security.tenant.model.Tenant;
import de.kaiserpfalzedv.paladinsinn.security.tenant.model.impl.TenantBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2017-03-18
 */
public class TenantCSVReader implements DataReader<Tenant> {
    private static final Logger LOG = LoggerFactory.getLogger(TenantCSVReader.class);

    @Override
    public Set<Tenant> read(URI uri) throws IOException {
        return read(uri.toURL().openStream());
    }

    @Override
    public Set<Tenant> read(InputStream stream) {
        Scanner scanner = new Scanner(stream);
        scanner.useDelimiter(";");

        return readAllLines(scanner);
    }

    @Override
    public Set<Tenant> read(String fileName) throws FileNotFoundException {
        return read(new File(fileName));
    }

    @Override
    public Set<Tenant> read(File file) throws FileNotFoundException {
        return read(new FileInputStream(file));
    }

    private Set<Tenant> readAllLines(Scanner scanner) {
        HashSet<Tenant> tenants = new HashSet<>();

        while (scanner.hasNextLine()) {
            try {
                tenants.add(readSingleUser(scanner.nextLine()));
            } catch (BuilderValidationException e) {
                LOG.error(e.getClass().getSimpleName() + " caught: " + e.getMessage(), e);
            }
        }

        return tenants;
    }

    private Tenant readSingleUser(String line) throws BuilderValidationException {
        LOG.trace("Reading line: {}", line);

        Scanner scanner = new Scanner(line);
        scanner.useDelimiter(";");

        String uniqueId = scanner.hasNext() ? scanner.next() : UUID.randomUUID().toString();

        if (!scanner.hasNext()) {
            throw new IllegalArgumentException("Need to give a key for tenant: " + uniqueId);
        }
        String tenantKey = scanner.next();

        if (!scanner.hasNext()) {
            throw new IllegalArgumentException("Need to give a nme for the tenant: " + uniqueId);
        }
        String tenantName = scanner.next();

        Tenant result = new TenantBuilder()
                .withUniqueId(UUID.fromString(uniqueId))
                .withKey(tenantKey)
                .withName(tenantName)
                .build();

        LOG.debug("Tenant created: {} -> {}", line, result);
        return result;
    }
}
