package com.appabove.app.dto.response;

public class AppResponse {
    private String id;
    private String name;
    private String logoUrl;
    private int groupCount;
    private String createdAt;

    public AppResponse(String id, String name, String logoUrl, int groupCount, String createdAt) {
        this.id = id;
        this.name = name;
        this.logoUrl = logoUrl;
        this.groupCount = groupCount;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public int getGroupCount() {
        return groupCount;
    }

    public void setGroupCount(int groupCount) {
        this.groupCount = groupCount;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
