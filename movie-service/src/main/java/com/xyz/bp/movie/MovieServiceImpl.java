package com.xyz.bp.movie;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.FindOptions;
import io.vertx.rxjava3.core.Vertx;
import io.vertx.rxjava3.ext.mongo.MongoClient;
import org.apache.commons.lang3.StringUtils;

import java.util.LinkedList;
import java.util.List;

public final class MovieServiceImpl implements MovieService {

    public static final String MOVIES_COLLECTION = "movies";
    private static volatile MovieServiceImpl _instance;

    private final MongoClient mongoClient;

    private MovieServiceImpl(Vertx vertx) {
        mongoClient = MongoClient.createShared(vertx, vertx.getOrCreateContext().config().getJsonObject("mongodb"), "movies-ds");
    }

    public static MovieServiceImpl getInstance(Vertx vertx) {
        MovieServiceImpl localRef = _instance;
        if (null == localRef) {
            synchronized (MovieServiceImpl.class) {
                localRef = _instance;
                if (null == localRef) {
                    localRef = _instance = new MovieServiceImpl(vertx);
                }
            }
        }
        return localRef;
    }

    @Override
    public Single<List<Movie>> listMovies(String city, String searchStr) {
        JsonObject query = JsonObject.of(Movie.RELEASED_IN, city);
        if (StringUtils.isNotBlank(searchStr)) {
            query.put("$text", JsonObject.of("$search", searchStr));
        }
        return mongoClient.rxFindWithOptions(MOVIES_COLLECTION, query,
                        new FindOptions().setSort(JsonObject.of(Movie.RELEASE_DATE, -1)))
                .flatMapObservable(Observable::fromIterable)
                .map(Movie::new)
                .collect(LinkedList::new, List::add);
    }

    @Override
    public Single<Movie> createMovie(Movie movie) {
        return null;
    }
}