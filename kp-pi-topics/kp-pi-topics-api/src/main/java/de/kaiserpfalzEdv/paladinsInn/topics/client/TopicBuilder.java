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

package de.kaiserpfalzEdv.paladinsInn.topics.client;

import de.kaiserpfalzEdv.paladinsInn.commons.Builder;
import de.kaiserpfalzEdv.paladinsInn.commons.BuilderValidationException;
import de.kaiserpfalzEdv.paladinsInn.security.Tenant;
import de.kaiserpfalzEdv.paladinsInn.security.User;
import de.kaiserpfalzEdv.paladinsInn.topics.Topic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;

/**
 * Builds a new topic.
 * @author rlichti {@literal <rlichti@kaiserpfalz-edv.de>}
 * @since 2016-03-20
 */
public class TopicBuilder implements Builder<Topic> {
    private UUID identifier;
    private UUID tenant;
    private UUID maintainer;

    private String name;
    private Topic parent;
    private final ArrayList<Topic> children = new ArrayList<>();


    public Topic build() throws BuilderValidationException {
        validate();

        Topic result = new TopicClientImpl(
                identifier, tenant, maintainer,
                name, parent, children
        );



        return result;
    }

    public void validate() throws BuilderValidationException {
        HashSet<String> failures = new HashSet<>(6);

        if (identifier == null) {
            identifier = UUID.randomUUID();
        }

        if (name == null) {
            failures.add("A topic needs a name. No name given!");
        }

        if (failures.size() != 0) {
            throw new BuilderValidationException(failures);
        }
    }

    public TopicBuilder withTopic(final Topic topic) {
        setIdentifier(topic.getIdentifier());
        setTenant(topic.getTenant());
        setMaintainer(topic.getMaintainer());
        setName(topic.getName());
        setParent(topic.getParent());
        setChildren(topic.getChildren());

        return this;
    }


    public TopicBuilder setIdentifier(final UUID identifier) {
        this.identifier = identifier;
        return this;
    }

    public TopicBuilder setTenant(final UUID tenant) {
        this.tenant = tenant;
        return this;
    }

    public TopicBuilder setTenant(final Tenant tenant) {
        this.tenant = tenant.getIdentifier();
        return this;
    }

    public TopicBuilder setMaintainer(final UUID maintainer) {
        this.maintainer = maintainer;
        return this;
    }

    public TopicBuilder setMaintainer(final User maintainer) {
        this.maintainer = maintainer.getIdentifier();
        return this;
    }

    public TopicBuilder setName(final String name) {
        this.name = name;
        return this;
    }

    public TopicBuilder setParent(final Topic parent) {
        this.parent = parent;
        return this;
    }

    public TopicBuilder unsetParent() {
        this.parent = null;
        return this;
    }

    public TopicBuilder setChildren(final Collection<Topic> children) {
        if (children != null) {
            this.children.addAll(children);
        }
        return this;
    }
}
