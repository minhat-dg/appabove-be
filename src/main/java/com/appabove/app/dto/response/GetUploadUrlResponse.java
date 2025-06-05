package com.appabove.app.dto.response;

public class GetUploadUrlResponse {
    private String uploadUrl;
    private String accessKey;
    private String id;

    public GetUploadUrlResponse(String uploadUrl, String accessKey, String id) {
        this.uploadUrl = uploadUrl;
        this.accessKey = accessKey;
        this.id = id;
    }

    public String getUploadUrl() {
        return uploadUrl;
    }

    public void setUploadUrl(String uploadUrl) {
        this.uploadUrl = uploadUrl;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
