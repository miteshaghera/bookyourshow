package com.xyz.bp.place;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.internal.functions.Functions;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.rxjava3.config.ConfigRetriever;
import io.vertx.rxjava3.core.AbstractVerticle;
import io.vertx.rxjava3.core.RxHelper;
import io.vertx.rxjava3.core.buffer.Buffer;
import io.vertx.rxjava3.core.http.HttpServer;
import io.vertx.rxjava3.core.http.HttpServerRequest;
import io.vertx.rxjava3.ext.web.Router;
import io.vertx.rxjava3.ext.web.RoutingContext;
import io.vertx.rxjava3.ext.web.handler.BodyHandler;

import java.util.List;

public class PlaceServiceVerticle extends AbstractVerticle {

    private PlaceService service;

    @Override
    public Completable rxStart() {
        try {
            return ConfigRetriever.create(vertx)
                    .rxGetConfig()
                    .flatMap(this::startHttpServer).ignoreElement();
        } catch (Exception e) {
            return Completable.error(e);
        }
    }

    private Single<HttpServer> startHttpServer(JsonObject config) {
        service = PlaceServiceImpl.getInstance(vertx);
        var httpServer = vertx.createHttpServer(new HttpServerOptions(config.getJsonObject("http")));
        var router = Router.router(vertx);
        router.post().handler(BodyHandler.create());
        router.put().handler(BodyHandler.create());
        router.patch().handler(BodyHandler.create());
        router.delete().handler(BodyHandler.create());
        router.get("/places").handler(this::listPlaces);
        router.post("/places").handler(this::createPlace);
        httpServer.requestStream()
                .toFlowable()
                .map(HttpServerRequest::pause)
                .onBackpressureDrop(req -> req.response().setStatusCode(503).end())
                .observeOn(RxHelper.scheduler(vertx))
                .subscribe(req -> {
                    req.resume();
                    router.handle(req);
                });

        return httpServer.rxListen();
    }

    private void createPlace(RoutingContext rc) {
        Place place = Json.decodeValue(rc.body().buffer().getDelegate(), Place.class);
        service.createCity(place).map(Json::encodeToBuffer)
                .flatMapCompletable(buffer -> rc.response().setStatusCode(201).rxEnd(Buffer.newInstance(buffer)))
                .subscribe(Functions.EMPTY_ACTION, cause -> rc.response().setStatusCode(500).end(JsonObject.of("msg", cause.getMessage()).toString()));
    }

    private void listPlaces(RoutingContext rc) {
        List<String> q = rc.queryParam("q");
        String searchStr = null;
        if (q != null && !q.isEmpty()) {
            searchStr = q.get(0);
        }
        service.listCities(searchStr).map(Json::encodeToBuffer)
                .flatMapCompletable(buffer -> rc.response().setStatusCode(200).rxEnd(Buffer.newInstance(buffer)))
                .subscribe(Functions.EMPTY_ACTION, cause -> rc.response().setStatusCode(500).end(JsonObject.of("msg", cause.getMessage()).toString()));
    }
}
