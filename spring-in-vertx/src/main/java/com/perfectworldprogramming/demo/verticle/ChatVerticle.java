package com.perfectworldprogramming.demo.verticle;

import com.perfectworldprogramming.mod.spring.app.context.ConfigType;
import com.perfectworldprogramming.mod.spring.app.context.SpringApplicationContextHolder;
import com.perfectworldprogramming.services.ChatService;
import com.perfectworldprogramming.vertx.ChatHandler;
import org.springframework.context.ApplicationContext;
import org.vertx.java.core.Future;
import org.vertx.java.core.Handler;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.eventbus.EventBus;
import org.vertx.java.core.http.HttpServer;
import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.core.net.NetServer;
import org.vertx.java.core.net.NetSocket;
import org.vertx.java.core.parsetools.RecordParser;
import org.vertx.java.core.sockjs.SockJSServer;
import org.vertx.java.core.sockjs.SockJSSocket;
import org.vertx.java.platform.Verticle;

public class ChatVerticle extends Verticle {

    private EventBus eventBus;

    private static final String SHARED_DATA_SOCKET_IDS = "socketIDs";
    private static final String CHAT_ADDRESS = "CHAT";

    @Override
    public void start(Future<Void> startedResult) {
        //super.start();

        this.eventBus = vertx.eventBus();
        startNetServer(1234);
        startSockJSServer();

        // Here is how we start a Spring Application Context in Vert.x with mod-spring-appcontext
        // First create a JsonObject
        JsonObject configFiles = new JsonObject();

        // Then create a JsonArray to store the locations of your Spring configurations
        JsonArray xmlFilesArray = new JsonArray();
        xmlFilesArray.add("META-INF/spring/spring-in-vertx-context.xml");

        // Add the JsonAarry to the property "configFiles
        configFiles.putArray("configFiles", xmlFilesArray);
        // Add the type of Spring configuration that you are using xml or @Configuration to the property "configType"
        configFiles.putString("configType", ConfigType.XML.getValue());

        SpringApplicationContextHolder.setVertx(vertx);
        SpringApplicationContextHolder.createApplicationContext(configFiles);

        /* The first time this is called is when the ApplicationContext(s) are created. There is actually two created
           a parent and a child context. the parent will have the Vert.x instance so that the child, which has your beans
           can have vertx injected into them.
        */
        ApplicationContext context = SpringApplicationContextHolder.getApplicationContext();

        ChatHandler chatHandler = context.getBean(ChatHandler.class);

        eventBus.registerHandler(ChatService.CHAT_ADDRESS, chatHandler);
    }

    private void startNetServer(int port) {
        final NetServer netServer = vertx.createNetServer();
        netServer.connectHandler(new Handler<NetSocket>() {
            @Override
            public void handle(NetSocket socket) {
                vertx.sharedData().getSet(SHARED_DATA_SOCKET_IDS).add(socket.writeHandlerID());
                socket.dataHandler(RecordParser.newDelimited("\n", new Handler<Buffer>() {
                    public void handle(Buffer frame) {
                    String line = frame.toString().trim();
                    eventBus.send(CHAT_ADDRESS, line);
                    //sendToAllSockets(line);
                    }
                }));
            }
        });
        netServer.listen(port);
    }

    private void startSockJSServer() {
        HttpServer httpServer = vertx.createHttpServer();

        SockJSServer sockJSServer = vertx.createSockJSServer(httpServer);

        JsonObject config = new JsonObject().putString("prefix", "/chat");
        sockJSServer.installApp(config, new Handler<SockJSSocket>() {
            public void handle(final SockJSSocket sock) {
                vertx.sharedData().getSet(SHARED_DATA_SOCKET_IDS).add(sock.writeHandlerID());
                sock.endHandler(new Handler<Void>() {
                    @Override
                    public void handle(Void aVoid) {
                        vertx.sharedData().getSet(SHARED_DATA_SOCKET_IDS).remove(sock.writeHandlerID());
                    }
                });
            }
        });
        httpServer.listen(8083);
    }
}
