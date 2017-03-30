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

import java.util.Objects;

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
    private static final long serialVersionUID = 8374124626037087707L;

    @Column(name = "GIVENNAME_PREFIX", length = 50)
    private String givenNamePrefix;

    @Column(name = "GIVENNAME", length = 200)
    private String givenName;

    @Column(name = "GIVENNAME_POSTFIX", length = 50)
    private String givenNamePostfix;


    @Column(name = "SN_PREFIX", length = 50)
    private String snPrefix;

    @Column(name = "SN", length = 200)
    private String sn;

    @Column(name = "SN_POSTFIX", length = 50)
    private String snPostfix;


    public NameJPA() {}

    public NameJPA(final String givenName, final String sn) {
        setGivenName(givenName);
        setSn(sn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getGivenNamePrefix(), getGivenName(), getGivenNamePostfix(), getSnPrefix(), getSn(), getSnPostfix());
    }

    @Override
    public String getGivenNamePrefix() {
        return givenNamePrefix;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NameJPA)) return false;
        NameJPA nameJPA = (NameJPA) o;
        return Objects.equals(getGivenNamePrefix(), nameJPA.getGivenNamePrefix()) &&
                Objects.equals(getGivenName(), nameJPA.getGivenName()) &&
                Objects.equals(getGivenNamePostfix(), nameJPA.getGivenNamePostfix()) &&
                Objects.equals(getSnPrefix(), nameJPA.getSnPrefix()) &&
                Objects.equals(getSn(), nameJPA.getSn()) &&
                Objects.equals(getSnPostfix(), nameJPA.getSnPostfix());
    }

    public void setGivenNamePrefix(String givenNamePrefix) {
        this.givenNamePrefix = givenNamePrefix;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("NameJPA{");
        sb.append("givenName='").append(givenName).append('\'');
        sb.append(", sn='").append(sn).append('\'');
        sb.append('}');
        return sb.toString();
    }

    @Override
    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    @Override
    public String getGivenNamePostfix() {
        return givenNamePostfix;
    }

    public void setGivenNamePostfix(String givenNamePostfix) {
        this.givenNamePostfix = givenNamePostfix;
    }


    @Override
    public String getSnPrefix() {
        return snPrefix;
    }

    public void setSnPrefix(String snPrefix) {
        this.snPrefix = snPrefix;
    }

    @Override
    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    @Override
    public String getSnPostfix() {
        return snPostfix;
    }

    public void setSnPostfix(String snPostfix) {
        this.snPostfix = snPostfix;
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


}
