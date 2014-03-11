package com.perfectworldprogramming.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.VertxFactory;
import org.vertx.java.core.eventbus.EventBus;

@Configuration
public class VertxConfiguration {

    @Autowired

    @Bean
    public EventBus eventBus() {
        return vertx().eventBus();
    }

    @Bean
    public Vertx vertx() {
        final String hostname = "127.0.0.1";
        //final Integer port = 5454;
        try {
            return VertxFactory.newVertx(hostname);
        } catch (Exception e) {
            return VertxFactory.newVertx(hostname);
        }
    }
}