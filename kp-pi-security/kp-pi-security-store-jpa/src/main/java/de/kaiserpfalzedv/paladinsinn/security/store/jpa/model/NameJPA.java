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

import javax.persistence.Column;
import javax.persistence.Embeddable;

import de.kaiserpfalzedv.paladinsinn.commons.api.person.Name;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2017-03-26
 */
@Embeddable
public class NameJPA implements Name {
    private static final long serialVersionUID = 3251136825872440349L;

    @Column(name = "GIVEN_PREFIX", length = 50)
    private String givenNamePrefix;
    @Column(name = "GIVEN", length = 200)
    private String givenName;
    @Column(name = "GIVEN_POSTFIX", length = 50)
    private String givenNamePostfix;

    @Column(name = "SN_PREFIX", length = 50)
    private String snPrefix;
    @Column(name = "SN", length = 200)
    private String sn;
    @Column(name = "SN_POSTFIX", length = 50)
    private String snPostfix;

    @Override
    public String getGivenNamePrefix() {
        return givenNamePrefix;
    }

    @Override
    public String getGivenName() {
        return givenName;
    }

    @Override
    public String getGivenNamePostfix() {
        return givenNamePostfix;
    }

    @Override
    public String getSnPrefix() {
        return snPrefix;
    }

    @Override
    public String getSn() {
        return sn;
    }

    @Override
    public String getSnPostfix() {
        return snPostfix;
    }

    @Override
    public String getFormalSn() {
        return getSnPrefix() + ' ' + getSn() + ' ' + getSnPostfix();
    }

    @Override
    public String getInformalFullName() {
        return getGivenName() + ' ' + getSn();
    }

    @Override
    public String getFormalFullName() {
        return getGivenNamePrefix() + ' ' + getGivenName() + ' ' + getGivenNamePostfix()
                + getSnPrefix() + ' ' + getSn() + ' ' + getSnPostfix();
    }

    public void setGivenNamePrefix(String givenNamePrefix) {
        this.givenNamePrefix = givenNamePrefix;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public void setGivenNamePostfix(String givenNamePostfix) {
        this.givenNamePostfix = givenNamePostfix;
    }

    public void setSnPrefix(String snPrefix) {
        this.snPrefix = snPrefix;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public void setSnPostfix(String snPostfix) {
        this.snPostfix = snPostfix;
    }
}
