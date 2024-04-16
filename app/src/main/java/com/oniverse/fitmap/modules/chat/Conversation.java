package com.oniverse.fitmap.modules.chat;

import java.io.Serializable;
import java.util.List;

public class Conversation implements Serializable {
    private String id;
    private String currentUserId;
    private String otherUserId;
    private List<Message> messages;
    private String lastMessage;

    public Conversation() {
        // Constructeur vide requis pour Firebase
    }

    public Conversation(String id, String currentUserId, String otherUserId) {
        this.id = id;
        this.currentUserId = currentUserId;
        this.otherUserId = otherUserId;
    }

    // Getters et setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCurrentUserId() {
        return currentUserId;
    }

    public void setCurrentUserId(String currentUserId) {
        this.currentUserId = currentUserId;
    }


    public String getOtherUserId() {
        return otherUserId;
    }

    public void setOtherUserId(String otherUserId) {
        this.otherUserId = otherUserId;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }



}
