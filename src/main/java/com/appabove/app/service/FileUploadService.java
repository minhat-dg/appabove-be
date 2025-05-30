package com.appabove.app.service;

import com.appabove.app.dto.UploadResult;
import com.appabove.app.model.Group;
import com.appabove.app.model.UploadedFile;
import com.appabove.app.repository.AppRepository;
import com.appabove.app.repository.FileUploadRepository;
import com.appabove.app.repository.GroupRepository;
import com.appabove.app.utils.ApkUtils;
import com.appabove.app.utils.FileUtils;
import com.appabove.app.utils.IpaUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

@Service
public class FileUploadService {

    private String ipaDir = "ipa";
    private String apkDir = "apk";
    private String plistDir = "plist";
    private String iconDir = "icon";

    private final FileUploadRepository fileUploadRepository;
    private final GroupRepository groupRepository;
    private final AppRepository appRepository;
    private final BunnyStorageService bunnyStorageService;
    private final MessageService messageService;

    public FileUploadService(FileUploadRepository fileUploadRepository, GroupRepository groupRepository, MessageService messageService, AppRepository appRepository, BunnyStorageService bunnyStorageService) {
        this.fileUploadRepository = fileUploadRepository;
        this.groupRepository = groupRepository;
        this.messageService = messageService;
        this.appRepository = appRepository;
        this.bunnyStorageService = bunnyStorageService;
    }

    public UploadedFile uploadFile(MultipartFile file, String groupId) throws IOException {
        Group group = groupRepository.findById(groupId).orElseThrow(() -> new RuntimeException(messageService.get("group.not.found")));

        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null || originalFileName.isBlank()) {
            throw new RuntimeException(messageService.get("file.name.invalid"));
        }

        String id = UUID.randomUUID().toString();

        UploadedFile uploaded = new UploadedFile();
        uploaded.setId(id);
        uploaded.setFileName(originalFileName);
        uploaded.setSize(file.getSize());
        uploaded.setUploadTime(LocalDateTime.now());
        uploaded.setGroup(group);

        String fileType = FilenameUtils.getExtension(originalFileName).toLowerCase(Locale.ROOT);
        UploadResult result;
        switch (fileType) {
            case "ipa":
                result = handleIpaUpload(file, originalFileName, id);
                break;
            case "apk":
                result = handleApkUpload(file, originalFileName, id);
                break;
            default:
                throw new RuntimeException(messageService.get("file.type.unsupported"));
        }
        uploaded.setFileType(fileType);
        uploaded.setDownloadUrl(result.downloadUrl());
        uploaded.setVersion(result.version());

        String iconUrl = group.getApp().getIconUrl();
        if ((iconUrl == null || iconUrl.isBlank()) && "apk".equalsIgnoreCase(fileType)) {
            saveAppIcon(file, group);
        }

        return fileUploadRepository.save(uploaded);
    }

    private UploadResult handleIpaUpload(MultipartFile file, String fileName, String id) throws IOException {
        File buildFile = FileUtils.multipartToFile(file, ".ipa");
        String downloadUrl, version;
        try {
            // Lấy metadata từ IPA file
            Map<String, String> metadata = IpaUtils.extractIpaMetadata(buildFile);
            version = metadata.get("version");

            String ipaFileName = ipaDir + "/" + id + "/" + fileName;
            String ipaUrl = bunnyStorageService.getPublicUrl(ipaFileName);
            bunnyStorageService.uploadFile(ipaFileName, buildFile);

            // Tạo manifest plist file
            String plistFileName = plistDir + "/" + id + ".plist";
            String plistUrl = bunnyStorageService.getPublicUrl(plistFileName);
            String plistContent = IpaUtils.generatePlistContent(ipaUrl, metadata);
            File plistFile = FileUtils.writeStringToFile(plistContent);
            try {
                bunnyStorageService.uploadFile(plistFileName, plistFile);
            } finally {
                plistFile.delete();
            }

            downloadUrl = "itms-services://?action=download-manifest&url=" + plistUrl;
        } finally {
            buildFile.delete();
        }
        return new UploadResult(downloadUrl, version);
    }

    private UploadResult handleApkUpload(MultipartFile file, String fileName, String id) throws IOException {
        File buildFile = FileUtils.multipartToFile(file, ".apk");
        String downloadUrl, version;
        try {
            version = ApkUtils.getAppVersion(buildFile);
            // Link download thẳng cho file apk
            String apkFileName = apkDir + "/" + id + "/" + fileName;
            String apkUrl = bunnyStorageService.getPublicUrl(apkFileName);
            bunnyStorageService.uploadFile(apkFileName, buildFile);
            downloadUrl = apkUrl;
        } finally {
            buildFile.delete();
        }
        return new UploadResult(downloadUrl, version);
    }

    public void saveAppIcon(MultipartFile apkFile, Group group) throws IOException {
        File buildFile = FileUtils.multipartToFile(apkFile, ".apk");
        try {
            BufferedImage icon = ApkUtils.extractIconFromApk(buildFile);
            if (icon != null) {
                String iconFileName = iconDir + "/" + group.getApp().getAppName() + ".png";
                String iconUrl = bunnyStorageService.getPublicUrl(iconFileName);
                File iconFile = FileUtils.writeImageToFile(icon);
                try {
                    bunnyStorageService.uploadFile(iconFileName, iconFile);
                } finally {
                    iconFile.delete();
                }
                group.getApp().setIconUrl(iconUrl);
                appRepository.save(group.getApp());
            }
        } finally {
            buildFile.delete();
        }
    }

    public List<UploadedFile> getFilesByGroupId(String groupId) {
        return fileUploadRepository.findByGroup_GroupId(groupId, Sort.by(Sort.Direction.DESC, "uploadTime"));
    }
}
