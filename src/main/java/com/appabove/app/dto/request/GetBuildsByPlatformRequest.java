package com.appabove.app.dto.request;

public class GetBuildsByPlatformRequest {
    private String platform;
    private String groupId;

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}
