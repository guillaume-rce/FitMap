package com.oniverse.fitmap.modules.chat;

import java.util.Objects;

public class Message {
    private String senderId;
    private String content;
    private boolean isSent;
    private long timestamp;
    private String receiverId;
    private String fileUrl;

    public Message(String senderId, String content, boolean isSent, long timestamp, String receiverId, String fileUrl) {
        this.senderId = senderId;
        this.content = content;
        this.isSent = isSent;
        this.timestamp = timestamp;
        this.receiverId = receiverId;
        this.fileUrl = fileUrl; // Initialiser le champ du fichier
    }

    // Constructeur vide requis pour Firebase
    public Message() {
    }

    // Constructeur avec tous les champs
    public Message(String senderId, String content, boolean isSent, long timestamp, String receiverId) {
        this.senderId = senderId;
        this.content = content;
        this.isSent = isSent;
        this.timestamp = timestamp;
        this.receiverId = receiverId;
    }


    // Getters et setters pour tous les champs

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isSent() {
        return isSent;
    }

    public void setSent(boolean sent) {
        isSent = sent;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getUserId() {
        return senderId;
    }

    public void setUserId(String userId) {
        this.senderId = userId;
    }
    public String getReceiverId() {
        return receiverId;
    }
    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }
    public String getFileUrl() {
        return fileUrl;
    }
    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    // Implémentation de equals et hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return timestamp == message.timestamp &&
                isSent == message.isSent &&
                Objects.equals(senderId, message.senderId) &&
                Objects.equals(content, message.content) &&
                Objects.equals(receiverId, message.receiverId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(senderId, content, isSent, timestamp, receiverId);
    }
}