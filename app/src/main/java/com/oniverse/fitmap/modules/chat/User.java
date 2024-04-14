package com.oniverse.fitmap.modules.chat;

import java.io.Serializable;

public class User implements Serializable {
    private String name;
    private String firstName;
    private String email;
    private String id;

    public User() {
        // Constructeur vide requis pour Firebase
    }

    public User(String id, String name, String firstName, String email) {
        this.name = name;
        this.firstName = firstName;
        this.email = email;
        this.id = id;
    }

    // Getters et setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getId() {
        return id;
    }
}
