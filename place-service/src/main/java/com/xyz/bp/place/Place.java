package com.xyz.bp.place;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.xyz.bp.core.model.AbstractEntity;
import io.vertx.core.json.JsonObject;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

@JsonDeserialize(builder = Place.CityBuilder.class)
public final class Place extends AbstractEntity {

    public static final String CODE = "code";
    public static final String NAME = "name";
    private final String code;

    private final String name;

    private Place(CityBuilder builder) {
        super(builder);
        this.code = builder.code;
        this.name = builder.name;
    }

    public Place(JsonObject json) {
        super(json);
        String code = json.getString(CODE);
        if (StringUtils.isBlank(code)) {
            throw new IllegalArgumentException("code can not be blank.");
        }
        this.code = code;
        String name = json.getString(NAME);
        if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("name can not be blank.");
        }
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public JsonObject toJson() {
        return super.toJson().put(CODE, this.code).put(NAME, this.name);
    }

    public static CityBuilder builder() {
        return new CityBuilder();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Place place = (Place) o;

        return Objects.equals(code, place.code);
    }

    @Override
    public int hashCode() {
        return code != null ? code.hashCode() : 0;
    }

    @JsonPOJOBuilder
    public static class CityBuilder extends AbstractBuilder {
        private String code;
        private String name;

        public CityBuilder withCode(String code) {
            this.code = code;
            return this;
        }

        public CityBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public Place build() {
            return new Place(this);
        }
    }
}
