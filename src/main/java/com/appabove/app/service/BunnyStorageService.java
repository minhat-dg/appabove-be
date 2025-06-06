package com.appabove.app.service;

import com.appabove.app.dto.response.GetUploadUrlResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@Service
public class BunnyStorageService {
    @Value("${BUNNY_STORAGE_ZONE}")
    private String bunnyStorageZone;

    @Value("${BUNNY_ACCESS_KEY}")
    private String bunnyAccessKey;

    @Value("${BUNNY_STORAGE_HOST_NAME}")
    private String bunnyHostName;

    @Value("${BUNNY_STORAGE_ENDPOINT}")
    private String bunnyEndPoint;

    /**
     * Upload a file to BunnyCDN
     */
    public void uploadFile(String filePath, File file) throws IOException {
        String url = String.format("https://%s/%s/%s", bunnyEndPoint, bunnyStorageZone, filePath);

        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestMethod("PUT");
        conn.setDoOutput(true);
        conn.setRequestProperty("AccessKey", bunnyAccessKey);
        conn.setRequestProperty("Content-Type", "application/octet-stream");

        if (file != null) {
            try (OutputStream os = conn.getOutputStream(); FileInputStream fis = new FileInputStream(file)) {
                fis.transferTo(os); // Stream trực tiếp lên BunnyCDN
            }
        }

        int responseCode = conn.getResponseCode();
        if (responseCode != 200 && responseCode != 201) {
            String error = new String(conn.getErrorStream().readAllBytes(), StandardCharsets.UTF_8);
            throw new IOException("Upload failed: HTTP " + responseCode + "\n" + error);
        }
    }

    /**
     * Download a file from BunnyCDN
     */
    public Resource getFile(String fileName) throws IOException {
        String fileUrl = String.format("https://%s/%s", bunnyHostName, fileName);
        Resource resource = new UrlResource(fileUrl);
        if (!resource.exists()) {
            throw new IOException("File not found at BunnyCDN: " + fileUrl);
        }
        return resource;
    }

    /**
     * Get public URL of a file stored in BunnyCDN
     */
    public String getPublicUrl(String filePath) {
        return String.format("https://%s/%s", bunnyHostName, filePath);
    }

    public GetUploadUrlResponse getUploadInfo(String filePath, String id) throws IOException {
        String url = String.format("https://%s/%s/%s", bunnyEndPoint, bunnyStorageZone, filePath);
        return new GetUploadUrlResponse(url, bunnyAccessKey, id);
    }

    public void deleteFile(String filePath) throws IOException {
        String url = String.format("https://%s/%s/%s", bunnyEndPoint, bunnyStorageZone, filePath);

        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestMethod("DELETE");
        conn.setRequestProperty("AccessKey", bunnyAccessKey);

        int responseCode = conn.getResponseCode();
        if (responseCode != 200 && responseCode != 204 && responseCode != 404) {
            String error = new String(conn.getErrorStream().readAllBytes(), StandardCharsets.UTF_8);
            throw new IOException("Delete failed: HTTP " + responseCode + "\n" + error);
        }
    }
}

