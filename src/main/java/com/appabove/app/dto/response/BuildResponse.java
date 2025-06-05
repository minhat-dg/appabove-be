package com.appabove.app.dto.response;

public class BuildResponse {
    private String id;
    private String groupId;
    private String platform;
    private String version;
    private String packageName;
    private String appName;
    private String fileUrl;
    private String fileName;
    private String sizeMb;
    private String plistUrl;
    private String uploadedBy;
    private String uploadedAt;
    private int installCount;

    public BuildResponse(String id, String groupId, String platform, String version, String packageName, String appName, String fileUrl, String fileName, String sizeMb, String plistUrl, String uploadedBy, String uploadedAt, int installCount) {
        this.id = id;
        this.groupId = groupId;
        this.platform = platform;
        this.version = version;
        this.packageName = packageName;
        this.appName = appName;
        this.fileUrl = fileUrl;
        this.fileName = fileName;
        this.sizeMb = sizeMb;
        this.plistUrl = plistUrl;
        this.uploadedBy = uploadedBy;
        this.uploadedAt = uploadedAt;
        this.installCount = installCount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getSizeMb() {
        return sizeMb;
    }

    public void setSizeMb(String sizeMb) {
        this.sizeMb = sizeMb;
    }

    public String getPlistUrl() {
        return plistUrl;
    }

    public void setPlistUrl(String plistUrl) {
        this.plistUrl = plistUrl;
    }

    public String getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(String uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

    public String getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(String uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    public int getInstallCount() {
        return installCount;
    }

    public void setInstallCount(int installCount) {
        this.installCount = installCount;
    }
}
