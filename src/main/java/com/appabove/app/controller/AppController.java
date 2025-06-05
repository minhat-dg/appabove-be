package com.appabove.app.controller;

import com.appabove.app.dto.request.BodyIdRequest;
import com.appabove.app.dto.request.CreateAppRequest;
import com.appabove.app.dto.response.BaseResponse;
import com.appabove.app.dto.response.AppResponse;
import com.appabove.app.service.AppService;
import com.appabove.app.service.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/apps")
public class AppController {
    private final AppService appService;

    private final MessageService messageService;

    public AppController(AppService appService, MessageService messageService) {
        this.appService = appService;
        this.messageService = messageService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createApp(@RequestBody CreateAppRequest request,  Authentication authentication) throws IOException {
        String userId = (String) authentication.getPrincipal();
        AppResponse app = appService.createApp(request, userId);
        return ResponseEntity.ok(BaseResponse.success(messageService.get("successfully"), app));
    }

    @PostMapping("/get-all")
    public ResponseEntity<?> getAllApps(Authentication authentication) {
        String userId = (String) authentication.getPrincipal();
        List<AppResponse> apps = appService.getAppsByUserId(userId);
        return ResponseEntity.ok(BaseResponse.success(messageService.get("successfully"), apps));
    }

    @PostMapping("/get-by-id")
    public ResponseEntity<?> getByAppId(@RequestBody BodyIdRequest request) throws IOException {
        AppResponse app = appService.getApp(request.getId());
        return ResponseEntity.ok(BaseResponse.success(messageService.get("successfully"), app));
    }

    @PostMapping("/delete")
    public ResponseEntity<?> deleteApp(@RequestBody BodyIdRequest request) throws IOException {
        appService.deleteAppById(request.getId());
        return ResponseEntity.ok(BaseResponse.success(messageService.get("successfully")));
    }
}