package com.perfectworldprogramming.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.vertx.java.core.eventbus.EventBus;

import javax.annotation.Resource;

@Service
public class ChatServiceImpl implements ChatService {

    @Resource
    private EventBus eventBus;

    @Override
    public void sendChat(String message) {
        eventBus.send(CHAT_ADDRESS, message);
    }

    @Override
    public String capitalize(String message) {
        return message.toUpperCase();
    }

}
