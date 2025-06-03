package com.appabove.app.service;

import com.appabove.app.dto.GetUploadUrlResponse;
import com.appabove.app.dto.UploadResult;
import com.appabove.app.model.Build;
import com.appabove.app.model.Group;
import com.appabove.app.repository.AppRepository;
import com.appabove.app.repository.BuildRepository;
import com.appabove.app.repository.GroupRepository;
import com.appabove.app.utils.ApkUtils;
import com.appabove.app.utils.FileUtils;
import com.appabove.app.utils.IpaUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

@Service
public class BuildService {
    private final BuildRepository buildRepository;
    private final GroupRepository groupRepository;
    private final AppRepository appRepository;
    private final BunnyStorageService bunnyStorageService;
    private final MessageService messageService;

    public BuildService(BuildRepository buildRepository, GroupRepository groupRepository, MessageService messageService, AppRepository appRepository, BunnyStorageService bunnyStorageService) {
        this.buildRepository = buildRepository;
        this.groupRepository = groupRepository;
        this.messageService = messageService;
        this.appRepository = appRepository;
        this.bunnyStorageService = bunnyStorageService;
    }

//    public UploadedFile uploadFile(MultipartFile file, String groupId) throws IOException {
//        Group group = groupRepository.findById(groupId).orElseThrow(() -> new RuntimeException(messageService.get("group.not.found")));
//
//        String originalFileName = file.getOriginalFilename();
//        if (originalFileName == null || originalFileName.isBlank()) {
//            throw new RuntimeException(messageService.get("file.name.invalid"));
//        }
//
//        String id = UUID.randomUUID().toString();
//
//        UploadedFile uploaded = new UploadedFile();
//        uploaded.setId(id);
//        uploaded.setFileName(originalFileName);
//        uploaded.setSize(file.getSize());
//        uploaded.setUploadTime(LocalDateTime.now());
//        uploaded.setGroup(group);
//
//        String fileType = FilenameUtils.getExtension(originalFileName).toLowerCase(Locale.ROOT);
//        UploadResult result;
//        switch (fileType) {
//            case "ipa":
//                result = handleIpaUpload(file, originalFileName, id);
//                break;
//            case "apk":
//                result = handleApkUpload(file, originalFileName, id);
//                break;
//            default:
//                throw new RuntimeException(messageService.get("file.type.unsupported"));
//        }
//        uploaded.setFileType(fileType);
//        uploaded.setDownloadUrl(result.downloadUrl());
//        uploaded.setVersion(result.version());
//
//        String iconUrl = group.getApp().getIconUrl();
//        if ((iconUrl == null || iconUrl.isBlank()) && "apk".equalsIgnoreCase(fileType)) {
//            File buildFile = FileUtils.multipartToFile(file, ".ipa");
//            try {
//                saveAppIcon(buildFile, group);
//            } finally {
//                buildFile.delete();
//            }
//        }
//
//        return fileUploadRepository.save(uploaded);
//    }

    public GetUploadUrlResponse getUploadUrl(String fileName, String groupId) throws IOException {
        Group group = groupRepository.findById(groupId).orElseThrow(() -> new RuntimeException(messageService.get("group.not.found")));

        String id = UUID.randomUUID().toString();
        Build uploaded = new Build();
        uploaded.setId(id);
        uploaded.setFileName(fileName);
        uploaded.setGroup(group);
        String fileType = FilenameUtils.getExtension(fileName).toLowerCase(Locale.ROOT);
        String storagePath = group.getStoragePath() + id;
        uploaded.setStoragePath(storagePath);
        String downloadUrl = bunnyStorageService.getPublicUrl(uploaded.getStoragePath() + fileName);
        uploaded.setFileType(fileType);
        uploaded.setDownloadUrl(downloadUrl);
        buildRepository.save(uploaded);
        return bunnyStorageService.getUploadInfo(uploaded.getStoragePath() + fileName, id);
    }

    public Build getFileMetaData(String id) throws IOException {
        Build file = buildRepository.findById(id).orElseThrow(() -> new RuntimeException(messageService.get("file.not.found")));
        File downloadFile = FileUtils.downloadFile(file.getDownloadUrl());
        try {
            file.setSize(downloadFile.length());
            file.setUploadTime(LocalDateTime.now());
            UploadResult result;
            switch (file.getFileType()) {
                case "ipa":
                    result = getIpaInfo(downloadFile, file);
                    break;
                case "apk":
                    result = getApkInfo(downloadFile, file);
                    break;
                default:
                    throw new RuntimeException(messageService.get("file.type.unsupported"));
            }
            file.setDownloadUrl(result.downloadUrl());
            file.setVersion(result.version());

            String iconUrl = file.getGroup().getApp().getIconUrl();
            if ((iconUrl == null || iconUrl.isBlank()) && "apk".equalsIgnoreCase(file.getFileType())) {
                saveAppIcon(downloadFile, file.getGroup());
            }
        } finally {
            downloadFile.delete();
        }
        return buildRepository.save(file);
    }

    private UploadResult getApkInfo(File apkFile, Build file) throws IOException {
        String downloadUrl, version;
        Map<String, String> metadata = IpaUtils.extractIpaMetadata(apkFile);
        version = metadata.get("version");
        downloadUrl = file.getDownloadUrl();
        return new UploadResult(downloadUrl, version);
    }

    private UploadResult getIpaInfo(File ipaFile, Build file) throws IOException {
        String downloadUrl, version;
        Map<String, String> metadata = IpaUtils.extractIpaMetadata(ipaFile);
        version = metadata.get("version");

        // Tạo manifest plist file
        String plistFilePath = file.getStoragePath() + file.getId() + ".plist";
        String plistUrl = bunnyStorageService.getPublicUrl(plistFilePath);
        String plistContent = IpaUtils.generatePlistContent(file.getDownloadUrl(), metadata);
        File plistFile = FileUtils.writeStringToFile(plistContent);
        try {
            bunnyStorageService.uploadFile(plistFilePath, plistFile);
        } finally {
            plistFile.delete();
        }

        downloadUrl = "itms-services://?action=download-manifest&url=" + plistUrl;
        return new UploadResult(downloadUrl, version);
    }

//    private UploadResult handleIpaUpload(MultipartFile file, String fileName, String id) throws IOException {
//        File buildFile = FileUtils.multipartToFile(file, ".ipa");
//        String downloadUrl, version;
//        try {
//            // Lấy metadata từ IPA file
//            Map<String, String> metadata = IpaUtils.extractIpaMetadata(buildFile);
//            version = metadata.get("version");
//
//            String ipaFileName = ipaDir + "/" + id + "/" + fileName;
//            String ipaUrl = bunnyStorageService.getPublicUrl(ipaFileName);
//            bunnyStorageService.uploadFile(ipaFileName, buildFile);
//
//            // Tạo manifest plist file
//            String plistFileName = plistDir + "/" + id + ".plist";
//            String plistUrl = bunnyStorageService.getPublicUrl(plistFileName);
//            String plistContent = IpaUtils.generatePlistContent(ipaUrl, metadata);
//            File plistFile = FileUtils.writeStringToFile(plistContent);
//            try {
//                bunnyStorageService.uploadFile(plistFileName, plistFile);
//            } finally {
//                plistFile.delete();
//            }
//
//            downloadUrl = "itms-services://?action=download-manifest&url=" + plistUrl;
//        } finally {
//            buildFile.delete();
//        }
//        return new UploadResult(downloadUrl, version);
//    }
//
//    private UploadResult handleApkUpload(MultipartFile file, String fileName, String id) throws IOException {
//        File buildFile = FileUtils.multipartToFile(file, ".apk");
//        String downloadUrl, version;
//        try {
//            version = ApkUtils.getAppVersion(buildFile);
//            // Link download thẳng cho file apk
//            String apkFileName = apkDir + "/" + id + "/" + fileName;
//            String apkUrl = bunnyStorageService.getPublicUrl(apkFileName);
//            bunnyStorageService.uploadFile(apkFileName, buildFile);
//            downloadUrl = apkUrl;
//        } finally {
//            buildFile.delete();
//        }
//        return new UploadResult(downloadUrl, version);
//    }

    public void saveAppIcon(File apkFile, Group group) throws IOException {
        BufferedImage icon = ApkUtils.extractIconFromApk(apkFile);
        if (icon != null) {
            String iconFilePath = group.getApp().getStoragePath() + group.getApp().getAppName() + ".png";
            String iconUrl = bunnyStorageService.getPublicUrl(iconFilePath);
            File iconFile = FileUtils.writeImageToFile(icon);
            try {
                bunnyStorageService.uploadFile(iconFilePath, iconFile);
            } finally {
                iconFile.delete();
            }
            group.getApp().setIconUrl(iconUrl);
            appRepository.save(group.getApp());
        }
    }

    public List<Build> getBuildsByGroupId(String groupId) {
        return buildRepository.findByGroup_GroupId(groupId, Sort.by(Sort.Direction.DESC, "uploadTime"));
    }

    public Build getBuildById(String id) {
        return buildRepository.findById(id).orElseThrow(() -> new IllegalArgumentException(messageService.get("file.not.found", id)));
    }

    public void deleteBuild(String id) throws IOException {
        if (!buildRepository.existsById(id)) {
            throw new IllegalArgumentException(messageService.get("file.not.found", id));
        }
        bunnyStorageService.deleteFile(getBuildById(id).getStoragePath());
        buildRepository.deleteById(id);
    }

    public void countInstall(String id) {
        if (!buildRepository.existsById(id)) {
            throw new IllegalArgumentException(messageService.get("file.not.found", id));
        }
        Build build = buildRepository.findById(id).orElseThrow(() -> new IllegalArgumentException(messageService.get("file.not.found", id)));
        build.increaseInstallCount();
        buildRepository.save(build);
    }
}
