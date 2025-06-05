package com.appabove.app.model;

import com.appabove.app.dto.response.BuildResponse;
import com.appabove.app.utils.DateTimeUtils;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.time.LocalDateTime;

@Entity
public class Build {
    @Id
    private String id;

    private String fileName;      // tên gốc
    private String storagePath;      // đường dẫn thực tế trên server
    private String fileUrl;
    private String version;
    private long size = 0;
    private int installCount = 0;
    private LocalDateTime uploadedAt;
    private String plistUrl;
    private String platform;
    private String appName;
    private String packageName;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "group_id")
    private Group group;

    public Build() {

    }

    public Build(String id, String fileName, String storagePath, int installCount, String platform, Group group) {
        this.id = id;
        this.fileName = fileName;
        this.storagePath = storagePath;
        this.installCount = installCount;
        this.platform = platform;
        this.group = group;
    }

    public BuildResponse toBuildResponse() {
        return new BuildResponse(id, group.getGroupId(), platform, version, packageName, appName, fileUrl, fileName, getSizeMB(), plistUrl, "", DateTimeUtils.formatDateTime(uploadedAt), getInstallCount());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getStoragePath() {
        if (storagePath.endsWith("/")) {
            return storagePath;
        } else {
            return storagePath + "/";
        }
    }

    public void setStoragePath(String storagePath) {
        if (storagePath.endsWith("/")) {
            this.storagePath = storagePath;
        } else {
            this.storagePath = storagePath + "/";
        }
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }


    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getInstallCount() {
        return installCount;
    }

    public void setInstallCount(int installCount) {
        this.installCount = installCount;
    }

    public void increaseInstallCount() {
        this.installCount = installCount + 1;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    public String getPlistUrl() {
        return plistUrl;
    }

    public void setPlistUrl(String plistUrl) {
        this.plistUrl = plistUrl;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getSizeMB() {
        return String.format("%.2fMB", size / 1024f / 1024f);
    }
}


