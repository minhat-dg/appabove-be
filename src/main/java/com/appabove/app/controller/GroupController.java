package com.appabove.app.controller;

import com.appabove.app.dto.request.BodyIdRequest;
import com.appabove.app.dto.request.CreateGroupRequest;
import com.appabove.app.dto.response.BaseResponse;
import com.appabove.app.dto.response.GroupResponse;
import com.appabove.app.service.GroupService;
import com.appabove.app.service.MessageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/groups")
public class GroupController {

    private final GroupService groupService;

    private final MessageService messageService;

    public GroupController(GroupService groupService, MessageService messageService) {
        this.groupService = groupService;
        this.messageService = messageService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createGroup(@RequestBody CreateGroupRequest request) throws IOException {
        GroupResponse group = groupService.createGroup(request.getName(), request.getAppId());
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.success(messageService.get("successfully"), group));
    }

    @PostMapping("/get-all")
    public ResponseEntity<?> getGroupsByAppId(@RequestBody BodyIdRequest request) {
        List<GroupResponse> groups = groupService.getGroupsByAppId(request.getId());
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.success(messageService.get("successfully"), groups));
    }

    @PostMapping("/delete")
    public ResponseEntity<BaseResponse<Void>> deleteGroup(@RequestBody BodyIdRequest request) throws IOException {
        groupService.deleteGroupById(request.getId());
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.success(messageService.get("successfully")));
    }
}
