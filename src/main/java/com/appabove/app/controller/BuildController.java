package com.appabove.app.controller;

import com.appabove.app.dto.request.BodyIdRequest;
import com.appabove.app.dto.request.FinalizeUploadRequest;
import com.appabove.app.dto.request.GetBuildsByPlatformRequest;
import com.appabove.app.dto.request.GetUploadUrlRequest;
import com.appabove.app.dto.response.BaseResponse;
import com.appabove.app.dto.response.BuildResponse;
import com.appabove.app.dto.response.GetAllBuildResponse;
import com.appabove.app.dto.response.GetUploadUrlResponse;
import com.appabove.app.service.BuildService;
import com.appabove.app.service.GroupService;
import com.appabove.app.service.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/builds")
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


    @PostMapping("/get-by-platform")
    public ResponseEntity<?> getBuildByPlatform(@RequestBody GetBuildsByPlatformRequest request) {
        GetAllBuildResponse response = buildService.getBuildsByPlatform(request.getGroupId(), request.getPlatform());
        return ResponseEntity.ok(BaseResponse.success(messageService.get("successfully"), response));
    }

    @PostMapping("/get-upload-url")
    public ResponseEntity<?> getUploadUrl(@RequestBody GetUploadUrlRequest request) throws IOException {
        GetUploadUrlResponse response = buildService.getUploadUrl(request.getFileName(), request.getGroupId());
        return ResponseEntity.ok(BaseResponse.success(messageService.get("successfully"), response));
    }

    @PostMapping("/verify-upload")
    public ResponseEntity<?> verifyBuild(@RequestBody FinalizeUploadRequest request) throws IOException {
        BuildResponse response = buildService.getFileMetaData(request.getId());
        return ResponseEntity.ok(BaseResponse.success(messageService.get("successfully"), response));
    }

    @PostMapping("/delete")
    public ResponseEntity<?> deleteBuild(@RequestBody BodyIdRequest request) throws IOException {
        buildService.deleteBuild(request.getId());
        return ResponseEntity.ok(BaseResponse.success(messageService.get("successfully")));
    }

    @PostMapping("/count-install")
    public ResponseEntity<?> installCount(@RequestBody BodyIdRequest request) {
        buildService.countInstall(request.getId());
        return ResponseEntity.ok(BaseResponse.success(messageService.get("successfully")));
    }
}

