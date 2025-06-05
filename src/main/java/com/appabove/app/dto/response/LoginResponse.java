package com.appabove.app.dto.response;

public class LoginResponse {
    private String accessToken;
    private String id;
    private String email;

    public LoginResponse(String accessToken, String id, String email) {
        this.accessToken = accessToken;
        this.id = id;
        this.email = email;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

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
}
