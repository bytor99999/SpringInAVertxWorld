package com.perfectworldprogramming.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.vertx.java.core.Vertx;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class VertxServletContextListener implements ServletContextListener {

    Logger log = LoggerFactory.getLogger(VertxServletContextListener.class);

    public void contextInitialized(final ServletContextEvent event) {
    }

    public void contextDestroyed(final ServletContextEvent event) {
        WebApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(event.getServletContext());
        Vertx vertx = appContext.getBean(Vertx.class);

        if (vertx != null) {
            log.info("Shutting down Vert.x instance");
            vertx.stop();
        }

    }

}
