package com.appabove.app.controller;

import com.appabove.app.dto.*;
import com.appabove.app.model.Group;
import com.appabove.app.model.UploadedFile;
import com.appabove.app.service.FileUploadService;
import com.appabove.app.service.GroupService;
import com.appabove.app.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/files")
public class FileUploadController {

    private final FileUploadService fileUploadService;
    private final GroupService groupService;

    @Autowired
    private MessageService messageService;

    public FileUploadController(FileUploadService fileUploadService, GroupService groupService) {
        this.fileUploadService = fileUploadService;
        this.groupService = groupService;
    }

    @PostMapping("/upload")
    public ResponseEntity<BaseResponse<UploadedFile>> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("groupId") String groupId) throws IOException {
        UploadedFile uploaded = fileUploadService.uploadFile(file, groupId);
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.success(messageService.get("successfully"), uploaded));
    }

//    @GetMapping("/download/{id}")
//    public ResponseEntity<Resource> downloadFile(@PathVariable String id) throws IOException {
//        Resource file = fileUploadService.getFile(id);
//        String fileName = file.getFilename().substring(file.getFilename().indexOf("_") + 1); // giữ tên gốc
//
//        return ResponseEntity.ok()
//                .contentType(MediaType.APPLICATION_OCTET_STREAM)
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
//                .body(file);
//    }
//
//    @GetMapping("/manifest/{manifestFileName:.+}")
//    public ResponseEntity<Resource> serveManifest(@PathVariable String manifestFileName) throws IOException {
//        Resource manifest = fileUploadService.getManifestFile(manifestFileName);
//        return ResponseEntity.ok()
//                .contentType(MediaType.parseMediaType("application/xml"))
//                .body(manifest);
//    }
//
//    @GetMapping("/icon/{iconFileName}")
//    public ResponseEntity<Resource> getIcon(@PathVariable String iconFileName) throws IOException {
//        Resource resource = fileUploadService.getIcon(iconFileName);
//        return ResponseEntity.ok()
//                .contentType(MediaType.IMAGE_PNG)
//                .body(resource);
//    }

    @GetMapping("/by-group/{groupId}")
    public ResponseEntity<BaseResponse<GetAllFilesResponse>> getFilesByGroup(@PathVariable String groupId) {
        List<UploadedFile> files = fileUploadService.getFilesByGroupId(groupId);
        Group group = groupService.getGroup(groupId);
        GetAllFilesResponse response = new GetAllFilesResponse();
        response.setFiles(files);
        response.setGroupName(group.getGroupName());
        response.setAppIconUrl(group.getApp().getIconUrl());
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.success(messageService.get("successfully"), response));
    }

    @PostMapping("/upload-url")
    public ResponseEntity<BaseResponse<GetUploadUrlResponse>> getUploadUrl(@RequestBody GetUploadUrlRequest request) throws IOException {
        GetUploadUrlResponse response = fileUploadService.getUploadUrl(request.getFileName(), request.getGroupId());
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.success(messageService.get("successfully"), response));
    }

    @PostMapping("/finalize-upload")
    public ResponseEntity<BaseResponse<UploadedFile>> finalizeUpload(@RequestBody FinalizeUploadRequest request) throws IOException {
        UploadedFile response = fileUploadService.getFileMetaData(request.getId());
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.success(messageService.get("successfully"), response));
    }
}

