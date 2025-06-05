package com.appabove.app.utils;

import com.appabove.app.dto.UploadResult;
import net.dongliu.apk.parser.ApkFile;
import net.dongliu.apk.parser.bean.Icon;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;

public class ApkUtils {
    public static BufferedImage extractIconFromApk(File file) throws IOException {
        try (ApkFile apkFile = new ApkFile(file)) {
            List<Icon> icons = apkFile.getAllIcons().stream().filter(iconFace -> iconFace instanceof Icon).map(iconFace -> (Icon) iconFace).toList();
            if (icons.isEmpty()) {
                System.err.println("Không tìm thấy icon hợp lệ trong APK");
                return null;
            }

            // Tìm icon có density cao nhất
            Icon bestIcon = icons.stream().max(Comparator.comparingInt(Icon::getDensity)).orElse(null);

            if (bestIcon.getData() != null) {
                return ImageIO.read(new ByteArrayInputStream(bestIcon.getData()));
            } else {
                System.err.println("Không tìm thấy icon hợp lệ");
                return null;
            }

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static UploadResult getApkInfo(File file) throws IOException {
        try (ApkFile apkFile = new ApkFile(file)) {
            String versionName = apkFile.getApkMeta().getVersionName();
            Long versionCode = apkFile.getApkMeta().getVersionCode();
            String version = String.format("%s (%d)", versionName, versionCode);
            String appName = apkFile.getApkMeta().getName();
            String packageName = apkFile.getApkMeta().getPackageName();
            return new UploadResult("", packageName, appName, version);
        } catch (IOException e) {
            throw new IOException("Error while reading APK file", e);
        }
    }
}
