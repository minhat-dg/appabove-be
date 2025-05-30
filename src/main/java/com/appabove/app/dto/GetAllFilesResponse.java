package com.appabove.app.dto;

import com.appabove.app.model.UploadedFile;

import java.util.List;

public class GetAllFilesResponse {
    private List<UploadedFile> files;
    private String groupName;
    private String appIconUrl;

    public List<UploadedFile> getFiles() {
        return files;
    }

    public void setFiles(List<UploadedFile> files) {
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
