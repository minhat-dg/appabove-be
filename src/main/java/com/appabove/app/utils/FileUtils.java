package com.appabove.app.utils;

import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

public class FileUtils {

    public static File multipartToFile(MultipartFile multipart, String type) throws IOException {
        File tempFile = File.createTempFile("temp", type);
        try (InputStream in = multipart.getInputStream(); OutputStream out = new FileOutputStream(tempFile)) {
            in.transferTo(out);
        }
        return tempFile;
    }

    public static File writeStringToFile(String content) throws IOException {
        File tempFile = File.createTempFile("temp_plist", ".plist");
        Files.writeString(tempFile.toPath(), content, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        return tempFile;
    }

    public static File writeImageToFile(BufferedImage icon) throws IOException {
        File tempFile = File.createTempFile("temp_icon", ".png");
        ImageIO.write(icon, "png", tempFile);
        return tempFile;
    }

    public static File downloadFile(String fileUrl) throws IOException {
        // Tạo file tạm thời với phần mở rộng như ".ipa"
        File tempFile = Files.createTempFile("download-", "temp").toFile();

        try (InputStream in = new URL(fileUrl).openStream(); OutputStream out = new FileOutputStream(tempFile)) {

            byte[] buffer = new byte[8192];
            int bytesRead;

            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        }

        return tempFile;
    }
}
