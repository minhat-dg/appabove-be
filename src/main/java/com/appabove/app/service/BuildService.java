package com.appabove.app.service;

import com.appabove.app.dto.UploadResult;
import com.appabove.app.dto.response.BuildResponse;
import com.appabove.app.dto.response.GetAllBuildResponse;
import com.appabove.app.dto.response.GetUploadUrlResponse;
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
import java.time.ZoneId;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

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
        String storagePath = group.getStoragePath() + id;
        String fileType = FilenameUtils.getExtension(fileName).toLowerCase(Locale.ROOT);
        Build uploaded = new Build(id, fileName, storagePath, 0, fileType.equalsIgnoreCase("apk") ? "android" : "ios", group);
        String fileUrl = bunnyStorageService.getPublicUrl(uploaded.getStoragePath() + fileName);
        uploaded.setFileUrl(fileUrl);
        buildRepository.save(uploaded);
        return bunnyStorageService.getUploadInfo(uploaded.getStoragePath() + fileName, id);
    }

    public BuildResponse getFileMetaData(String id) throws IOException {
        Build build = buildRepository.findById(id).orElseThrow(() -> new RuntimeException(messageService.get("file.not.found")));
        try {
            File downloadFile = FileUtils.downloadFile(build.getFileUrl());
            try {
                build.setSize(downloadFile.length());
                build.setUploadedAt(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
                UploadResult result;
                switch (build.getPlatform()) {
                    case "ios":
                        result = getIpaInfo(downloadFile, build);
                        break;
                    case "android":
                        result = ApkUtils.getApkInfo(downloadFile);
                        break;
                    default:
                        throw new RuntimeException(messageService.get("file.type.unsupported"));
                }
                build.setPlistUrl(result.plistUrl());
                build.setVersion(result.version());
                build.setPackageName(result.packageName());
                build.setAppName(result.appName());

                String iconUrl = build.getGroup().getApp().getIconUrl();
                if ((iconUrl == null || iconUrl.isBlank()) && "android".equalsIgnoreCase(build.getPlatform())) {
                    saveAppIcon(downloadFile, build.getGroup());
                }
            } finally {
                downloadFile.delete();
            }
        } catch (Exception e) {
            buildRepository.delete(build);
            throw new RuntimeException("Upload failed!");
        }
        return buildRepository.save(build).toBuildResponse();
    }

    private UploadResult getIpaInfo(File ipaFile, Build build) throws IOException {
        String version, packageName, appName;
        Map<String, String> metadata = IpaUtils.extractIpaMetadata(ipaFile);
        version = metadata.get("version");
        packageName = metadata.get("bundle_id");
        appName = metadata.get("title");

        // Tạo manifest plist file
        String plistFilePath = build.getStoragePath() + build.getId() + ".plist";
        String plistUrl = bunnyStorageService.getPublicUrl(plistFilePath);
        String plistContent = IpaUtils.generatePlistContent(build.getFileUrl(), metadata);
        File plistFile = FileUtils.writeStringToFile(plistContent);
        try {
            bunnyStorageService.uploadFile(plistFilePath, plistFile);
        } finally {
            plistFile.delete();
        }

        String downloadUrl = "itms-services://?action=download-manifest&url=" + plistUrl;
        return new UploadResult(downloadUrl, packageName, appName, version);
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

    public GetAllBuildResponse getBuildsByPlatform(String groupId, String platform) {
        List<BuildResponse> builds = buildRepository.findByGroup_GroupIdAndUploadedAtIsNotNull(groupId, Sort.by(Sort.Direction.DESC, "uploadedAt")).stream().filter(build -> platform.equalsIgnoreCase(build.getPlatform())).map(Build::toBuildResponse).collect(Collectors.toList());
        Group group = groupRepository.findById(groupId).orElseThrow(() -> new RuntimeException(messageService.get("group.not.found")));
        GetAllBuildResponse response = new GetAllBuildResponse();
        response.setBuilds(builds);
        response.setGroupName(group.getGroupName());
        response.setAppName(group.getApp().getAppName());
        response.setAppIconUrl(group.getApp().getIconUrl());
        return response;
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
