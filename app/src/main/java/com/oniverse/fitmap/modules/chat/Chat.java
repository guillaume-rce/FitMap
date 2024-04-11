package com.oniverse.fitmap.modules.chat;

public class Chat {
    private String userId;
    private String username;
    private String lastMessage;

    public Chat() {
        // Constructeur vide requis pour Firebase
    }

    public Chat(String userId, String username, String lastMessage) {
        this.userId = userId;
        this.username = username;
        this.lastMessage = lastMessage;
    }

    // Getters et setters
    public String getUserId() {

        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }
}
