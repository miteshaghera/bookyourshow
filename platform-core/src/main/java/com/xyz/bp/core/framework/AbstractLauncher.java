package com.xyz.bp.core.framework;

import io.reactivex.rxjava3.plugins.RxJavaPlugins;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.impl.launcher.VertxCommandLauncher;
import io.vertx.core.impl.launcher.VertxLifecycleHooks;
import io.vertx.core.json.JsonObject;
import io.vertx.rxjava3.RxHelper;

public class AbstractLauncher extends VertxCommandLauncher implements VertxLifecycleHooks {

    @Override
    public void afterConfigParsed(JsonObject config) {
    }

    @Override
    public void beforeStartingVertx(VertxOptions options) {

    }

    @Override
    public void afterStartingVertx(Vertx vertx) {
        RxJavaPlugins.setComputationSchedulerHandler(s -> RxHelper.scheduler(vertx));
        RxJavaPlugins.setIoSchedulerHandler(s -> RxHelper.blockingScheduler(vertx));
        RxJavaPlugins.setNewThreadSchedulerHandler(s -> RxHelper.scheduler(vertx));
    }

    @Override
    public void beforeDeployingVerticle(DeploymentOptions deploymentOptions) {

    }

    @Override
    public void beforeStoppingVertx(Vertx vertx) {

    }

    @Override
    public void afterStoppingVertx() {

    }

    @Override
    public void handleDeployFailed(Vertx vertx, String mainVerticle, DeploymentOptions deploymentOptions, Throwable cause) {

    }
}
