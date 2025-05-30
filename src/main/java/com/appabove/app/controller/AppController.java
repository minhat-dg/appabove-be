package com.appabove.app.controller;

import com.appabove.app.dto.BaseResponse;
import com.appabove.app.dto.CreateAppRequest;
import com.appabove.app.model.App;
import com.appabove.app.service.AppService;
import com.appabove.app.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/apps")
public class AppController {
    private final AppService appService;

    public AppController(AppService appService) {
        this.appService = appService;
    }

    @Autowired
    private MessageService messageService;

    @PostMapping
    public ResponseEntity<BaseResponse<App>> createApp(@RequestBody CreateAppRequest request) {
        App app = appService.createApp(request);
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.success(messageService.get("successfully"), app));
    }

    @GetMapping
    public ResponseEntity<BaseResponse<List<App>>> getAllApps() {
        List<App> apps = appService.getAllApps();
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.success(messageService.get("successfully"), apps));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<Void>> deleteApp(@PathVariable String id) {
        appService.deleteAppById(id);
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponse.success(messageService.get("successfully")));
    }
}