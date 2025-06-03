package com.appabove.app.service;

import com.appabove.app.model.App;
import com.appabove.app.model.Group;
import com.appabove.app.repository.AppRepository;
import com.appabove.app.repository.GroupRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

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

    public Group createGroup(String groupName, String appId) {
        if (groupRepository.existsByGroupName(groupName)) {
            throw new IllegalArgumentException(messageService.get("group.name.duplicate"));
        }

        App app = appRepository.findById(appId).orElseThrow(() -> new IllegalArgumentException(messageService.get("app.not.found", appId)));
        Group group = new Group(groupName, app);
        group.setStoragePath(app.getStoragePath() + group.getGroupId());
        return groupRepository.save(group);
    }


    public List<Group> getGroupsByAppId(String appId) {
        boolean appExists = appRepository.existsById(appId);
        if (!appExists) {
            throw new RuntimeException("App not found with ID: " + appId);
        }
        return groupRepository.findAll(Sort.by(Sort.Direction.ASC, "groupName")).stream().filter(g -> g.getApp().getAppId().equals(appId)).collect(Collectors.toList());
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

