package com.oniverse.fitmap.modules.chat;

public class Chat {
    private String currentUserId;
    private String userIdDestination;
    // Ajoutez d'autres champs si nécessaire

    public Chat(String currentUserId, String userId2) {
        this.currentUserId = currentUserId;
        this.userIdDestination = userId2;
    }

    // definir les Getters et setters
    public String getUserId() {
        return this.currentUserId;
    }
    public String getUserIdDestinateur() {
        return currentUserId;
    }

}
