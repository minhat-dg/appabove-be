package com.appabove.app.model;

import jakarta.persistence.*;

@Entity
public class User {
    @Id
    private String id;

    @Column(unique = true)
    private String email;

    private String password; // bcrypt hash

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}