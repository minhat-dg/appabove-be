package com.appabove.app.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(
        name = "app_group",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"app_id", "group_name"})
        }
)
public class Group {
    @Id
    private String groupId;

    @Column(name = "group_name")
    private String groupName;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "app_id")
    private App app;

    public Group() {
    }

    public Group(String groupName, App app) {
        this.groupId = UUID.randomUUID().toString();
        this.groupName = groupName;
        this.app = app;
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
}
