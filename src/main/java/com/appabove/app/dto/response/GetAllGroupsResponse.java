package com.appabove.app.dto.response;

import com.appabove.app.model.Group;

import java.util.List;

public class GetAllGroupsResponse {
    private List<Group> groups;
    private String appName;
    private String appIconUrl;

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
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
