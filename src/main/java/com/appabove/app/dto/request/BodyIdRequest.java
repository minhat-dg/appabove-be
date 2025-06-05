package com.appabove.app.dto.request;

public class BodyIdRequest {
    String id;

    public BodyIdRequest() {
    }

    public BodyIdRequest(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
