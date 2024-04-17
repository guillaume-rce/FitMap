package com.oniverse.fitmap.modules.chat;

public class User {
    private String name;
    private String firstName;
    private String email;
    private String password;

    public User(String name, String firstName, String email, String password) {
        this.name = name;
        this.firstName = firstName;
        this.email = email;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
    public void setName(String name) {
        this.name = name;
    }

}
