package com.oniverse.fitmap.modules.chat;

public class User {
    private String id;
    private String name;
    private String email;

    // Constructeur
    public User(String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    // Getters
    public String getId() {

        return id;
    }

    public String getName() {

        return name;
    }

    public String getEmail() {

        return email;
    }

    // Setters (facultatif, selon vos besoins)
    public void setId(String id) {

        this.id = id;
    }

    public void setName(String name) {

        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
