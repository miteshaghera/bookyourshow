package com.xyz.bp.movie;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.xyz.bp.core.model.AbstractEntity;
import com.xyz.bp.core.util.JsonArrayCollector;
import io.vertx.core.json.JsonObject;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@JsonDeserialize(builder = Movie.MovieBuilder.class)
public final class Movie extends AbstractEntity {

    public static final String TITLE = "title";
    public static final String GENRE = "genre";
    public static final String LANGUAGE = "lang";
    public static final String RELEASE_DATE = "releaseDate";
    public static final String RELEASED_IN = "releasedIn";
    private final String title;
    private final Set<String> genre;
    private final Set<String> language;
    private final long releaseDate;
    private final Set<String> releasedIn;

    public Movie(MovieBuilder builder) {
        super(builder);
        this.title = builder.title;
        this.genre = Collections.unmodifiableSet(builder.genre);
        this.language = Collections.unmodifiableSet(builder.language);
        this.releaseDate = builder.releaseDate;
        if (builder.releasedIn == null || builder.releasedIn.isEmpty()) {
            throw new IllegalArgumentException("at least one city is required");
        }
        this.releasedIn = Collections.unmodifiableSet(builder.releasedIn);
    }

    public Movie(JsonObject json) {
        super(json);
        this.title = json.getString(TITLE);
        this.genre = json.getJsonArray(GENRE).stream().filter(e -> e instanceof String)
                .map(String.class::cast).collect(Collectors.toUnmodifiableSet());
        this.language = json.getJsonArray(LANGUAGE).stream().filter(e -> e instanceof String)
                .map(String.class::cast).collect(Collectors.toUnmodifiableSet());
        this.releaseDate = json.getLong(RELEASE_DATE);
        this.releasedIn = json.getJsonArray(RELEASED_IN).stream().filter(e -> e instanceof String)
                .map(String.class::cast).collect(Collectors.toUnmodifiableSet());
    }

    @Override
    protected JsonObject toJson() {
        return super.toJson()
                .put(TITLE, this.title)
                .put(GENRE, this.genre.stream().collect(new JsonArrayCollector<String>()))
                .put(LANGUAGE, this.language.stream().collect(new JsonArrayCollector<String>()))
                .put(RELEASE_DATE, this.releaseDate)
                .put(RELEASED_IN, this.releasedIn.stream().collect(new JsonArrayCollector<String>()));
    }

    public String getTitle() {
        return title;
    }

    public Set<String> getGenre() {
        return genre;
    }

    public Set<String> getLanguage() {
        return language;
    }

    public long getReleaseDate() {
        return releaseDate;
    }

    public Set<String> getReleasedIn() {
        return releasedIn;
    }


    @JsonPOJOBuilder
    public static class MovieBuilder extends AbstractBuilder {
        private String title;
        private Set<String> genre;
        private Set<String> language;
        private long releaseDate;
        private Set<String> releasedIn;

        public MovieBuilder withTitle(String title) {
            this.title = title;
            return this;
        }

        public MovieBuilder withGenre(Set<String> genre) {
            this.genre = genre;
            return this;
        }

        public MovieBuilder addGenre(String genre) {
            if (this.genre == null) {
                this.genre = new LinkedHashSet<>();
            }
            this.genre.add(genre);
            return this;
        }

        public MovieBuilder withLanguage(Set<String> language) {
            this.language = language;
            return this;
        }

        public MovieBuilder addLanguage(String language) {
            if (this.language == null) {
                this.language = new LinkedHashSet<>();
            }
            this.language.add(language);
            return this;
        }


        public MovieBuilder withReleaseDate(long releaseDate) {
            this.releaseDate = releaseDate;
            return this;
        }

        public MovieBuilder withReleasedIn(Set<String> releasedIn) {
            this.releasedIn = releasedIn;
            return this;
        }

        public MovieBuilder addPlace(String cityCode) {
            if (this.releasedIn == null) {
                this.releasedIn = new LinkedHashSet<>();
            }
            this.releasedIn.add(cityCode);
            return this;
        }


        public Movie build() {
            return new Movie(this);
        }
    }
}
