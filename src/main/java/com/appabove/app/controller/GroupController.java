package com.appabove.app.controller;

import com.appabove.app.dto.BaseResponse;
import com.appabove.app.dto.CreateGroupRequest;
import com.appabove.app.dto.GetAllGroupsResponse;
import com.appabove.app.model.App;
import com.appabove.app.model.Group;
import com.appabove.app.service.AppService;
import com.appabove.app.service.GroupService;
import com.appabove.app.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/groups")
public class GroupController {

    private final GroupService groupService;

    private final AppService appService;

    private final MessageService messageService;

    public GroupController(GroupService groupService, AppService appService, MessageService messageService) {
        this.groupService = groupService;
        this.appService = appService;
        this.messageService = messageService;
    }

    @PostMapping
    public ResponseEntity<BaseResponse<Group>> createGroup(@RequestBody CreateGroupRequest request) {
        Group group = groupService.createGroup(request.getGroupName(), request.getAppId());
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.success(messageService.get("successfully"), group));
    }

    @GetMapping("/by-app/{appId}")
    public ResponseEntity<BaseResponse<GetAllGroupsResponse>> getGroupsByAppId(@PathVariable String appId) {
        List<Group> groups = groupService.getGroupsByAppId(appId);
        GetAllGroupsResponse response = new GetAllGroupsResponse();
        response.setGroups(groups);
        App app = appService.getApp(appId);
        response.setAppName(app.getAppName());
        response.setAppIconUrl(app.getIconUrl());
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.success(messageService.get("successfully"), response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<Void>> deleteGroup(@PathVariable String id) throws IOException {
        groupService.deleteGroupById(id);
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.success(messageService.get("successfully")));
    }
}
