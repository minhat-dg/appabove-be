package com.appabove.app.service;

import com.appabove.app.dto.response.GroupResponse;
import com.appabove.app.model.App;
import com.appabove.app.model.Group;
import com.appabove.app.repository.AppRepository;
import com.appabove.app.repository.GroupRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class GroupService {

    private final GroupRepository groupRepository;
    private final AppRepository appRepository;
    private MessageService messageService;
    private BunnyStorageService bunnyStorageService;

    public GroupService(GroupRepository groupRepository, AppRepository appRepository, MessageService messageService, BunnyStorageService bunnyStorageService) {
        this.groupRepository = groupRepository;
        this.appRepository = appRepository;
        this.messageService = messageService;
        this.bunnyStorageService = bunnyStorageService;
    }

    public GroupResponse createGroup(String groupName, String appId) throws IOException {
        if (groupRepository.existsByApp_AppIdAndGroupName(appId, groupName)) {
            throw new IllegalArgumentException(messageService.get("group.name.duplicate"));
        }

        App app = appRepository.findById(appId).orElseThrow(() -> new IllegalArgumentException(messageService.get("app.not.found", appId)));
        String groupId = UUID.randomUUID().toString();
        String storagePath = app.getStoragePath() + groupId +"/";
        bunnyStorageService.uploadFile(storagePath, null);
        Group group = new Group(groupId, groupName, storagePath, app, LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
        return groupRepository.save(group).toGroupResponse();
    }


    public List<GroupResponse> getGroupsByAppId(String appId) {
        if (!appRepository.existsById(appId)) {
            throw new RuntimeException("App not found with ID: " + appId);
        }
        List<GroupResponse> groups = new ArrayList<>();
        groupRepository.findByApp_AppId(appId).forEach(group -> {
            groups.add(group.toGroupResponse());
        });
        return groups;
    }


    public void deleteGroupById(String id) throws IOException {
        if (!groupRepository.existsById(id)) {
            throw new IllegalArgumentException(messageService.get("group.not.found", id));
        }
        bunnyStorageService.deleteFile(getGroup(id).getStoragePath());
        groupRepository.deleteById(id);
    }

    public Group getGroup(String id) {
        return groupRepository.findById(id).orElseThrow(() -> new IllegalArgumentException(messageService.get("group.not.found", id)));
    }

}

