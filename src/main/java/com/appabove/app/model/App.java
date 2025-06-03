package com.appabove.app.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class App {
    @Id
    private String appId;

    @Column(unique = true)
    private String appName;

    private String iconUrl;

    private String storagePath;

    @OneToMany(mappedBy = "app", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Group> groups = new ArrayList<>();

    public App() {
    }

    public App(String appId, String appName) {
        this.appId = appId;
        this.appName = appName;
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
        return storagePath;
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
}
