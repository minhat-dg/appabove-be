package com.appabove.app.model;

import com.appabove.app.dto.response.GroupResponse;
import com.appabove.app.utils.DateTimeUtils;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "app_group", uniqueConstraints = {@UniqueConstraint(columnNames = {"app_id", "group_name"})})
public class Group {
    @Id
    private String groupId;

    @Column(name = "group_name")
    private String groupName;

    private String storagePath;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "app_id")
    private App app;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Build> builds = new ArrayList<>();

    private LocalDateTime createdAt;

    public Group() {
    }

    public Group(String groupId, String groupName, String storagePath, App app, LocalDateTime createdAt) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.storagePath = storagePath;
        this.app = app;
        this.createdAt = createdAt;
    }

    public GroupResponse toGroupResponse() {
        return new GroupResponse(groupId, groupName, app.getAppId(), DateTimeUtils.formatDateTime(createdAt), getAndroidBuildCount(), getIosBuildCount());
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public App getApp() {
        return app;
    }

    public void setApp(App app) {
        this.app = app;
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

    public List<Build> getBuilds() {
        return builds;
    }

    public void setBuilds(List<Build> builds) {
        this.builds = builds;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public int getAndroidBuildCount() {
        return (int) builds.stream().filter(build -> "android".equalsIgnoreCase(build.getPlatform())).filter(build -> build.getUploadedAt() != null).count();
    }

    public int getIosBuildCount() {
        return (int) builds.stream().filter(build -> "ios".equalsIgnoreCase(build.getPlatform())).filter(build -> build.getUploadedAt() != null).count();
    }
}
