package com.xyz.bp.movie;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.internal.functions.Functions;
import io.vertx.core.http.HttpHeaders;
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
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class MovieServiceVerticle extends AbstractVerticle {

    private MovieService service;

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
        service = MovieServiceImpl.getInstance(vertx);
        var httpServer = vertx.createHttpServer(new HttpServerOptions(config.getJsonObject("http")));
        var router = Router.router(vertx);
        router.post().handler(BodyHandler.create());
        router.put().handler(BodyHandler.create());
        router.patch().handler(BodyHandler.create());
        router.delete().handler(BodyHandler.create());
        router.get("/cities/:city/movies").handler(this::listMovies);
        router.post("/movies").handler(this::createMovie);
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

    private void createMovie(RoutingContext rc) {
        Movie movie = Json.decodeValue(rc.body().buffer().getDelegate(), Movie.class);
        service.createMovie(movie).map(Json::encodeToBuffer)
                .flatMapCompletable(buffer -> rc.response().putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                        .setStatusCode(201).rxEnd(Buffer.newInstance(buffer)))
                .subscribe(Functions.EMPTY_ACTION, cause -> rc.response().putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                        .setStatusCode(500).end(JsonObject.of("msg", cause.getMessage()).toString()));
    }

    private void listMovies(RoutingContext rc) {
        String city = rc.pathParam("city");
        List<String> qParam = rc.queryParam("q");
        String searchStr = qParam != null && !qParam.isEmpty() ? qParam.get(0) : null;
        if (StringUtils.isBlank(city)) {
            rc.response().rxEnd(JsonObject.of("msg", "city can not be empty").toString()).subscribe();
        } else
            service.listMovies(city, searchStr).map(Json::encodeToBuffer)
                    .flatMapCompletable(buffer -> rc.response().putHeader(HttpHeaders.CONTENT_TYPE, "application/json").setStatusCode(200).rxEnd(Buffer.newInstance(buffer)))
                    .subscribe(Functions.EMPTY_ACTION, cause -> rc.response().putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                            .setStatusCode(500).end(JsonObject.of("msg", cause.getMessage()).toString()));
    }
}
