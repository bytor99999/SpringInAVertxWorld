package com.perfectworldprogramming.controller;

import com.perfectworldprogramming.services.ChatService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Controller
public class ChatController {

    @Resource
    private ChatService chatService;

    @RequestMapping(value="/chat", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void sendChatMessage(@RequestBody String message) {
        chatService.sendChat(message);
    }
}
