package com.xyz.bp.place;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.FindOptions;
import io.vertx.rxjava3.core.Vertx;
import io.vertx.rxjava3.ext.mongo.MongoClient;
import org.apache.commons.lang3.StringUtils;

import java.util.LinkedList;
import java.util.List;

public final class PlaceServiceImpl implements PlaceService {

    public static final String CITIES_COLLECTION = "places";
    private static volatile PlaceServiceImpl _instance;

    private final MongoClient mongoClient;

    private PlaceServiceImpl(Vertx vertx) {
        mongoClient = MongoClient.createShared(vertx, vertx.getOrCreateContext().config().getJsonObject("mongodb"), "places-ds");
    }

    public static PlaceServiceImpl getInstance(Vertx vertx) {
        PlaceServiceImpl localRef = _instance;
        if (null == localRef) {
            synchronized (PlaceServiceImpl.class) {
                localRef = _instance;
                if (null == localRef) {
                    localRef = _instance = new PlaceServiceImpl(vertx);
                }
            }
        }
        return localRef;
    }

    @Override
    public Single<List<Place>> listCities(String searchStr) {
        JsonObject query = JsonObject.of();
        FindOptions findOptions = new FindOptions().setLimit(10);
        if (StringUtils.isNotBlank(searchStr)) {
            query.put("$text", JsonObject.of("$search", searchStr));
            JsonObject score = JsonObject.of("score", JsonObject.of("$meta", "textScore"));
            findOptions.setFields(score).setSort(score);
        }
        return mongoClient.rxFindWithOptions(CITIES_COLLECTION, query, findOptions)
                .flatMapObservable(Observable::fromIterable)
                .map(Place::new)
                .collect(LinkedList::new, List::add);
    }

    @Override
    public Single<Place> createCity(Place place) {
        return mongoClient.rxFindOne(CITIES_COLLECTION, JsonObject.of(Place.CODE, place.getCode()), null)
                .map(Place::new)
                .doOnEvent((ele, cause) -> {
                    if (ele != null) {
                        throw new IllegalArgumentException("city is already present");
                    }
                })
                .switchIfEmpty(mongoClient.rxInsert(CITIES_COLLECTION, place.toJson())
                        .map(ignored -> place))
                .toSingle();
    }
}