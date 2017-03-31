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

package de.kaiserpfalzedv.paladinsinn.security.store.jpa.model;

import java.util.HashSet;

import de.kaiserpfalzedv.paladinsinn.commons.api.Builder;
import de.kaiserpfalzedv.paladinsinn.commons.api.BuilderValidationException;
import de.kaiserpfalzedv.paladinsinn.commons.api.person.Name;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2017-03-31
 */
public class NameJPABuilder implements Builder<NameJPA> {
    private String givenNamePrefix;
    private String givenName;
    private String givenNamePostfix;

    private String snPrefix;
    private String sn;
    private String snPostfix;


    @Override
    public NameJPA build() throws BuilderValidationException {
        NameJPA result = new NameJPA(givenName, sn);

        result.setGivenNamePrefix(givenNamePrefix);
        result.setGivenNamePostfix(givenNamePostfix);

        result.setSnPrefix(snPrefix);
        result.setSnPostfix(snPostfix);

        return result;
    }

    @Override
    public void validate() throws BuilderValidationException {
        HashSet<String> failures = new HashSet<>(2);

        if (givenName == null || givenName.isEmpty()) {
            failures.add("A given name is needed!");
        }

        if (sn == null || sn.isEmpty()) {
            failures.add("A sur name is needed!");
        }

        if (!failures.isEmpty()) {
            throw new BuilderValidationException(NameJPA.class, failures);
        }
    }


    public NameJPABuilder withName(final Name name) {
        withGivenNamePrefix(name.getGivenNamePrefix());
        withGivenName(name.getGivenName());
        withGivenNamePostfix(name.getGivenNamePostfix());

        withSnPrefix(name.getSnPrefix());
        withSn(name.getSn());
        withSnPostfix(name.getSnPostfix());

        return this;
    }

    public NameJPABuilder withGivenNamePrefix(final String givenNamePrefix) {
        this.givenNamePrefix = givenNamePrefix;
        return this;
    }

    public NameJPABuilder withGivenName(final String givenName) {
        this.givenName = givenName;
        return this;
    }

    public NameJPABuilder withGivenNamePostfix(final String givenNamePostfix) {
        this.givenNamePostfix = givenNamePostfix;
        return this;
    }

    public NameJPABuilder withSnPrefix(final String snPrefix) {
        this.snPrefix = snPrefix;
        return this;
    }

    public NameJPABuilder withSn(final String sn) {
        this.sn = sn;
        return this;
    }

    public NameJPABuilder withSnPostfix(final String snPostfix) {
        this.snPostfix = snPostfix;
        return this;
    }
}
