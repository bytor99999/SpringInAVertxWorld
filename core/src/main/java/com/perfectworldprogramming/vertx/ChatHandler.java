package com.perfectworldprogramming.vertx;

import com.perfectworldprogramming.services.ChatService;
import org.springframework.stereotype.Service;
import org.vertx.java.core.Handler;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.eventbus.EventBus;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.shareddata.SharedData;

import javax.annotation.Resource;
import java.util.Set;

@Service
public class ChatHandler implements Handler<Message<String>>{

    private static final String SHARED_DATA_SOCKET_IDS = "socketIDs";

    @Resource
    private EventBus eventBus;

    @Resource
    private Vertx vertx;

    @Resource
    private ChatService chatService;

    @Override
    public void handle(Message<String> message) {
        String changedString = chatService.capitalize(message.body());
        SharedData data = vertx.sharedData();
        Set<String> socketIDs = data.getSet(SHARED_DATA_SOCKET_IDS);
        changedString += "\n";
        Buffer bufferMessage = new Buffer(changedString);
        for (String socketID: socketIDs) {
            eventBus.send(socketID, bufferMessage);
        }
    }
}
