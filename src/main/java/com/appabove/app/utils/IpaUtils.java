package com.appabove.app.utils;

import com.dd.plist.NSDictionary;
import com.dd.plist.PropertyListParser;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Enumeration;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class IpaUtils {

    public static Map<String, String> extractIpaMetadata(File file) {
        try (ZipFile zipFile = new ZipFile(file)) {
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            String infoPlistPath = null;

            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                String name = entry.getName();

                if (name.startsWith("Payload/") && name.endsWith(".app/Info.plist")) {
                    infoPlistPath = name;
                    break;
                }
            }

            if (infoPlistPath == null) {
                return Map.of("bundle_id", "com.example.app", "version", "1.0", "title", "My App");
            }

            ZipEntry plistEntry = zipFile.getEntry(infoPlistPath);
            try (InputStream is = zipFile.getInputStream(plistEntry)) {
                NSDictionary rootDict = (NSDictionary) PropertyListParser.parse(is);

                String bundleId = rootDict.objectForKey("CFBundleIdentifier") != null ? rootDict.objectForKey("CFBundleIdentifier").toString() : "com.example.app";
                String version = rootDict.objectForKey("CFBundleShortVersionString") != null ? rootDict.objectForKey("CFBundleShortVersionString").toString() : "1.0";
                String title = rootDict.objectForKey("CFBundleDisplayName") != null ? rootDict.objectForKey("CFBundleDisplayName").toString() : rootDict.objectForKey("CFBundleName") != null ? rootDict.objectForKey("CFBundleName").toString() : "My App";

                return Map.of("bundle_id", bundleId, "version", version, "title", title);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return Map.of("bundle_id", "com.example.app", "version", "1.0", "title", "My App");
        }
    }

    public static String generatePlistContent(String ipaUrl, Map<String, String> metadata) {
        return String.format("""
                <?xml version="1.0" encoding="UTF-8"?>
                <!DOCTYPE plist PUBLIC "-//Apple//DTD PLIST 1.0//EN" "http://www.apple.com/DTDs/PropertyList-1.0.dtd">
                <plist version="1.0">
                  <dict>
                    <key>items</key>
                    <array>
                      <dict>
                        <key>assets</key>
                        <array>
                          <dict>
                            <key>kind</key>
                            <string>software-package</string>
                            <key>url</key>
                            <string>%s</string>
                          </dict>
                        </array>
                        <key>metadata</key>
                        <dict>
                          <key>bundle-identifier</key>
                          <string>%s</string>
                          <key>bundle-version</key>
                          <string>%s</string>
                          <key>kind</key>
                          <string>software</string>
                          <key>title</key>
                          <string>%s</string>
                        </dict>
                      </dict>
                    </array>
                  </dict>
                </plist>
                """, ipaUrl, metadata.get("bundle_id"), metadata.get("version"), metadata.get("title"));
    }
}
