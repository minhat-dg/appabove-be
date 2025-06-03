package com.appabove.app.dto;

import com.appabove.app.model.Build;

import java.util.List;

public class GetAllBuildResponse {
    private List<Build> files;
    private String groupName;
    private String appIconUrl;

    public List<Build> getFiles() {
        return files;
    }

    public void setFiles(List<Build> files) {
        this.files = files;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getAppIconUrl() {
        return appIconUrl;
    }

    public void setAppIconUrl(String appIconUrl) {
        this.appIconUrl = appIconUrl;
    }
}
