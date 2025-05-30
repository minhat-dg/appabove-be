package com.appabove.app.service;

import com.appabove.app.dto.CreateAppRequest;
import com.appabove.app.model.App;
import com.appabove.app.repository.AppRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AppService {
    private final AppRepository appRepository;

    public AppService(AppRepository appRepository) {
        this.appRepository = appRepository;
    }

    @Autowired
    private MessageService messageService;

    public App createApp(CreateAppRequest request) {
        if (appRepository.existsByAppName(request.getAppName())) {
            throw new IllegalArgumentException(messageService.get("app.name.duplicate"));
        }
        String appId = UUID.randomUUID().toString();
        App app = new App(appId, request.getAppName());
        return appRepository.save(app);
    }

    public List<App> getAllApps() {
        return appRepository.findAll(Sort.by(Sort.Direction.ASC, "appName"));
    }

    public void deleteAppById(String id) {
        if (!appRepository.existsById(id)) {
            throw new IllegalArgumentException(messageService.get("app.not.found", id));
        }
        appRepository.deleteById(id);
    }

    public App getApp(String id) {
        return appRepository.findById(id).orElseThrow(() -> new IllegalArgumentException(messageService.get("app.not.found", id)));
    }
}