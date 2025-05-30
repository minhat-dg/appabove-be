package com.appabove.app.utils;

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

    public static String getAppVersion(File file) throws IOException {
        try (ApkFile apkFile = new ApkFile(file)) {
            String versionName = apkFile.getApkMeta().getVersionName();
            Long versionCode = apkFile.getApkMeta().getVersionCode();
            return String.format("%s (%d)", versionName, versionCode);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
