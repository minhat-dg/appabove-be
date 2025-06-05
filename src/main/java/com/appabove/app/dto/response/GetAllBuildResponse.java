package com.appabove.app.dto.response;

import com.appabove.app.model.Build;

import java.util.List;

public class GetAllBuildResponse {
    private List<BuildResponse> builds;
    private String groupName;
    private String appName;
    private String appIconUrl;

    public List<BuildResponse> getBuilds() {
        return builds;
    }

    public void setBuilds(List<BuildResponse> builds) {
        this.builds = builds;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppIconUrl() {
        return appIconUrl;
    }

    public void setAppIconUrl(String appIconUrl) {
        this.appIconUrl = appIconUrl;
    }
}
