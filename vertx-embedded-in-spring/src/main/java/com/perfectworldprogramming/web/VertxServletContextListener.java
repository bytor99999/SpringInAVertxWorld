package com.perfectworldprogramming.web;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.vertx.java.core.Vertx;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class VertxServletContextListener implements ServletContextListener {

    public void contextInitialized(final ServletContextEvent event) {
    }

    public void contextDestroyed(final ServletContextEvent event) {
        WebApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(event.getServletContext());
        Vertx vertx = appContext.getBean(Vertx.class);

        if (vertx != null) {
            vertx.stop();
        }

    }

}
