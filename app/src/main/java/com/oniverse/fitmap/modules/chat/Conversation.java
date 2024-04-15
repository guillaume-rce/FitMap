package com.oniverse.fitmap.modules.chat;

public class Conversation {
    private String userId;
    private String lastMessage;

    public Conversation(String userId, String lastMessage) {
        this.userId = userId;
        this.lastMessage = lastMessage;
    }

    // Getters et setters
    public String getUserId() {

        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }
}

