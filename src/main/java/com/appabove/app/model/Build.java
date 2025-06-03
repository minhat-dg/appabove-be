package com.appabove.app.model;

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
    private String fileType;      // apk, ipa,...
    private String downloadUrl;
    private String version;
    private long size;
    private int installCount = 0;

    private LocalDateTime uploadTime;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "group_id")
    private Group group;

    public Build(String id, String fileName, String storagePath, String fileType, long size, LocalDateTime uploadTime, Group group, String downloadUrl) {
        this.id = id;
        this.fileName = fileName;
        this.storagePath = storagePath;
        this.fileType = fileType;
        this.size = size;
        this.uploadTime = uploadTime;
        this.group = group;
        this.downloadUrl = downloadUrl;
    }

    public Build() {

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
        return storagePath;
    }

    public void setStoragePath(String storagePath) {
        if (storagePath.endsWith("/")) {
            this.storagePath = storagePath;
        } else {
            this.storagePath = storagePath + "/";
        }
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public LocalDateTime getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(LocalDateTime uploadTime) {
        this.uploadTime = uploadTime;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
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
        this.installCount = installCount+1;
    }
}


