package com.appabove.app.model;

import com.appabove.app.dto.response.AppResponse;
import com.appabove.app.utils.DateTimeUtils;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "app_name"})
)
public class App {
    @Id
    private String appId;

    private String appName;

    private String iconUrl;

    private String storagePath;

    @OneToMany(mappedBy = "app", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Group> groups = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDateTime createdAt;

    public App() {
    }

    public App(String appId, String appName, LocalDateTime createdAt, User user, String storagePath) {
        this.appId = appId;
        this.appName = appName;
        this.createdAt = createdAt;
        this.user = user;
        this.storagePath = storagePath;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
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

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public AppResponse toAppResponse() {
        return new AppResponse(
                appId, appName, iconUrl, groups.size(), DateTimeUtils.formatDateTime(createdAt)
        );
    }
}
