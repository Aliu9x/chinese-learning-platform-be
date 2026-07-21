package com.aliu.be.common.util;

import java.nio.file.Path;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

/** Tiện ích kiểm tra và chuẩn hóa file upload. */
public final class FileUtils {

    private static final Set<String> ALLOWED_IMAGE_EXTENSIONS = Set.of("jpg", "jpeg", "png", "webp");
    private static final Set<String> ALLOWED_AUDIO_EXTENSIONS = Set.of("mp3", "wav", "ogg", "m4a");
    private static final Set<String> ALLOWED_VIDEO_EXTENSIONS = Set.of("mp4", "webm", "mov");

    private FileUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static String getExtension(String fileName) {
        String safeName = getSafeFileName(fileName);
        if (safeName == null) return "";
        int index = safeName.lastIndexOf('.');
        if (index < 0 || index == safeName.length() - 1) return "";
        return safeName.substring(index + 1).toLowerCase(Locale.ROOT);
    }

    public static String getBaseName(String fileName) {
        String safeName = getSafeFileName(fileName);
        if (safeName == null) return null;
        int index = safeName.lastIndexOf('.');
        return index > 0 ? safeName.substring(0, index) : safeName;
    }

    /** Loại bỏ thông tin đường dẫn để hạn chế path traversal. */
    public static String getSafeFileName(String fileName) {
        if (StringUtils.isBlank(fileName)) return null;
        return Path.of(fileName).getFileName().toString()
                .replaceAll("[\\r\\n]", "")
                .replaceAll("[^a-zA-Z0-9._-]", "_");
    }

    public static String generateUniqueFileName(String originalFileName) {
        String extension = getExtension(originalFileName);
        return UUID.randomUUID() + (extension.isBlank() ? "" : "." + extension);
    }

    public static boolean isAllowedImage(String fileName) {
        return ALLOWED_IMAGE_EXTENSIONS.contains(getExtension(fileName));
    }

    public static boolean isAllowedAudio(String fileName) {
        return ALLOWED_AUDIO_EXTENSIONS.contains(getExtension(fileName));
    }

    public static boolean isAllowedVideo(String fileName) {
        return ALLOWED_VIDEO_EXTENSIONS.contains(getExtension(fileName));
    }
}
