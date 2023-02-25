package com.xyz.bp.core.model;

import io.vertx.core.json.JsonObject;

import java.io.Serializable;
import java.util.Objects;

public class AbstractEntity implements Serializable {

    public static final String ID = "id";
    private final String id;

    protected AbstractEntity(JsonObject json) {
        this.id = json.getString(ID);
    }

    protected AbstractEntity(AbstractBuilder builder) {
        this.id = builder.id;
    }

    protected JsonObject toJson() {
        return JsonObject.of(ID, this.id);
    }

    public static class AbstractBuilder {
        private String id;

        public AbstractBuilder withId(String id) {
            this.id = id;
            return this;
        }

        private AbstractEntity build() {
            return new AbstractEntity(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbstractEntity that = (AbstractEntity) o;

        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
