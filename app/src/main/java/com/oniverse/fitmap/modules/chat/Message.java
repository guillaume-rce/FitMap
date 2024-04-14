package com.oniverse.fitmap.modules.chat;

public class Message {
    private String message;
    private String timestamp;
    private String senderId;
    private boolean isSent;

    public Message() {
        // Constructeur vide nécessaire pour Firebase
    }

    public Message(String message, String timestamp, String senderId) {
        this.message = message;
        this.timestamp = timestamp;
        this.senderId = senderId;
    }

    public Message(String messageText, String format, String senderId, boolean b) {
        this.message = messageText;
        this.timestamp = format;
        this.senderId = senderId;
        this.isSent = b;
    }

    // Getters et setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }
    public boolean isSent() {
        return isSent;
    }
    public void setSent(boolean sent) {
        isSent = sent;
    }
}
