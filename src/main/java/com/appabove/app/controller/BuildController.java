package com.appabove.app.controller;

import com.appabove.app.dto.*;
import com.appabove.app.model.Build;
import com.appabove.app.model.Group;
import com.appabove.app.service.BuildService;
import com.appabove.app.service.GroupService;
import com.appabove.app.service.MessageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/files")
public class BuildController {

    private final BuildService buildService;
    private final GroupService groupService;

    private final MessageService messageService;

    public BuildController(BuildService buildService, GroupService groupService, MessageService messageService) {
        this.buildService = buildService;
        this.groupService = groupService;
        this.messageService = messageService;
    }

    //    @PostMapping("/upload")
//    public ResponseEntity<BaseResponse<UploadedFile>> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("groupId") String groupId) throws IOException {
//        UploadedFile uploaded = fileUploadService.uploadFile(file, groupId);
//        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.success(messageService.get("successfully"), uploaded));
//    }


    @GetMapping("/by-group/{groupId}")
    public ResponseEntity<BaseResponse<GetAllBuildResponse>> getFilesByGroup(@PathVariable String groupId) {
        List<Build> files = buildService.getBuildsByGroupId(groupId);
        Group group = groupService.getGroup(groupId);
        GetAllBuildResponse response = new GetAllBuildResponse();
        response.setFiles(files);
        response.setGroupName(group.getGroupName());
        response.setAppIconUrl(group.getApp().getIconUrl());
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.success(messageService.get("successfully"), response));
    }

    @PostMapping("/upload-url")
    public ResponseEntity<BaseResponse<GetUploadUrlResponse>> getUploadUrl(@RequestBody GetUploadUrlRequest request) throws IOException {
        GetUploadUrlResponse response = buildService.getUploadUrl(request.getFileName(), request.getGroupId());
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.success(messageService.get("successfully"), response));
    }

    @PostMapping("/finalize-upload")
    public ResponseEntity<BaseResponse<Build>> finalizeUpload(@RequestBody FinalizeUploadRequest request) throws IOException {
        Build response = buildService.getFileMetaData(request.getId());
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.success(messageService.get("successfully"), response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<Void>> deleteBuild(@PathVariable String id) throws IOException {
        buildService.deleteBuild(id);
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.success(messageService.get("successfully")));
    }

    @GetMapping("/install/{id}")
    public ResponseEntity<BaseResponse<GetAllBuildResponse>> installCount(@PathVariable String id) {
        buildService.countInstall(id);
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.success(messageService.get("successfully")));
    }
}

