package com.appabove.app.service;

import com.appabove.app.dto.CreateAppRequest;
import com.appabove.app.model.App;
import com.appabove.app.repository.AppRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class AppService {
    private final AppRepository appRepository;
    private MessageService messageService;
    private BunnyStorageService bunnyStorageService;

    public AppService(AppRepository appRepository, MessageService messageService, BunnyStorageService bunnyStorageService) {
        this.appRepository = appRepository;
        this.messageService = messageService;
        this.bunnyStorageService = bunnyStorageService;
    }

    public App createApp(CreateAppRequest request) throws IOException {
        if (appRepository.existsByAppName(request.getAppName())) {
            throw new IllegalArgumentException(messageService.get("app.name.duplicate"));
        }
        String appId = UUID.randomUUID().toString();
        App app = new App(appId, request.getAppName());
        app.setStoragePath(appId);
        bunnyStorageService.uploadFile(app.getStoragePath(), null);
        return appRepository.save(app);
    }

    public List<App> getAllApps() {
        return appRepository.findAll(Sort.by(Sort.Direction.ASC, "appName"));
    }

    public void deleteAppById(String id) throws IOException {
        if (!appRepository.existsById(id)) {
            throw new IllegalArgumentException(messageService.get("app.not.found", id));
        }
        bunnyStorageService.deleteFile(getApp(id).getStoragePath());
        appRepository.deleteById(id);
    }

    public App getApp(String id) {
        return appRepository.findById(id).orElseThrow(() -> new IllegalArgumentException(messageService.get("app.not.found", id)));
    }
}