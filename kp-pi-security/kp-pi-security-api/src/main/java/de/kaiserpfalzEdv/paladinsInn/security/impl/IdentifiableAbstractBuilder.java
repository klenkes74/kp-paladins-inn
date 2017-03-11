package de.kaiserpfalzedv.paladinsinn.security.impl;

import java.util.ArrayList;
import java.util.UUID;

import de.kaiserpfalzedv.paladinsinn.security.Identifiable;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2017-03-11
 */
public abstract class IdentifiableAbstractBuilder<T extends Identifiable> {
    protected UUID uniqueId;
    protected String name;


    protected final ArrayList<String> validationErrors = new ArrayList<>(5);

    public abstract T build();

    public abstract void validateDuringBuild();

    protected void setDefaultsIfNeeded() {
        if (uniqueId == null) {
            uniqueId = UUID.randomUUID();
        }

        if (name == null || name.isEmpty()) {
            name = uniqueId.toString();
        }
    }

    public abstract boolean validate();

    public IdentifiableAbstractBuilder<T> withUniqueId(final UUID uniqueId) {
        this.uniqueId = uniqueId;
        return this;
    }

    public IdentifiableAbstractBuilder<T> withName(final String name) {
        this.name = name;
        return this;
    }
}
