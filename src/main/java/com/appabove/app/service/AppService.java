package com.appabove.app.service;

import com.appabove.app.dto.request.CreateAppRequest;
import com.appabove.app.dto.response.AppResponse;
import com.appabove.app.model.App;
import com.appabove.app.model.User;
import com.appabove.app.repository.AppRepository;
import com.appabove.app.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class AppService {
    private final AppRepository appRepository;
    private final UserRepository userRepository;
    private final MessageService messageService;
    private final BunnyStorageService bunnyStorageService;

    public AppService(AppRepository appRepository, UserRepository userRepository, MessageService messageService, BunnyStorageService bunnyStorageService) {
        this.appRepository = appRepository;
        this.userRepository = userRepository;
        this.messageService = messageService;
        this.bunnyStorageService = bunnyStorageService;
    }

    public AppResponse createApp(CreateAppRequest request, String userId) throws IOException {
        if (appRepository.existsByUser_IdAndAppName(userId, request.getName())) {
            throw new RuntimeException(messageService.get("app.name.duplicate"));
        }
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        String appId = UUID.randomUUID().toString();
        App app = new App(appId, request.getName(), LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")), user, appId);
        bunnyStorageService.uploadFile(app.getStoragePath(), null);
        return appRepository.save(app).toAppResponse();
    }

    public List<AppResponse> getAppsByUserId(String userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("User not found");
        }
        List<AppResponse> apps = new ArrayList<>();
        appRepository.findByUser_Id(userId).forEach(app -> {
            apps.add(app.toAppResponse());
        });
        return apps;
    }

    public void deleteAppById(String id) throws IOException {
        if (!appRepository.existsById(id)) {
            throw new RuntimeException(messageService.get("app.not.found", id));
        } else {
            bunnyStorageService.deleteFile(appRepository.findById(id).isPresent() ? appRepository.findById(id).get().getStoragePath() : null);
            appRepository.deleteById(id);
        }
    }

    public AppResponse getApp(String appId) throws IOException {
        App app = appRepository.findById(appId).orElseThrow(() -> new RuntimeException(messageService.get("app.not.found", appId)));
        return app.toAppResponse();
    }
}