package com.perfectworldprogramming.services;

public interface ChatService {

    public static final String CHAT_ADDRESS = "CHAT";

    public abstract void sendChat(String message);

    public abstract String capitalize(String message);
}
