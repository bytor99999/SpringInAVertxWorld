package com.perfectworldprogramming.demo.verticle;

import org.vertx.java.core.AsyncResult;
import org.vertx.java.core.Handler;
import org.vertx.java.platform.Verticle;

public class StartupVerticle extends Verticle {

    @Override
    public void start() {
        super.start();
        container.deployVerticle("com.perfectworldprogramming.demo.verticle.ChatVerticle", 1, new Handler<AsyncResult<String>>() {
            @Override
            public void handle(AsyncResult<String> stringAsyncResult) {
                if (stringAsyncResult.succeeded()) {
                    container.logger().info("Completed deployment of ChatVerticle");
                } else {
                    container.logger().info("Failed deployment of ChatVerticle");
                    container.logger().info(stringAsyncResult.result());
                }
            }
        });
    }
}
