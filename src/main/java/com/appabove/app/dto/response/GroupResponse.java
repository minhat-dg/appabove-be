package com.appabove.app.dto.response;

public class GroupResponse {
    private String id;
    private String name;
    private String appId;
    private String createdAt;
    private int androidBuildCount;
    private int iosBuildCount;

    public GroupResponse(String id, String name, String appId, String createdAt, int androidBuildCount, int iosBuildCount) {
        this.id = id;
        this.name = name;
        this.appId = appId;
        this.createdAt = createdAt;
        this.androidBuildCount = androidBuildCount;
        this.iosBuildCount = iosBuildCount;
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

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public int getAndroidBuildCount() {
        return androidBuildCount;
    }

    public void setAndroidBuildCount(int androidBuildCount) {
        this.androidBuildCount = androidBuildCount;
    }

    public int getIosBuildCount() {
        return iosBuildCount;
    }

    public void setIosBuildCount(int iosBuildCount) {
        this.iosBuildCount = iosBuildCount;
    }
}
