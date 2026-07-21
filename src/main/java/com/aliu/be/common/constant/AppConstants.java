package com.aliu.be.common.constant;

/**
 * Các hằng số dùng chung trong toàn bộ ứng dụng.
 */
public final class AppConstants {

    private AppConstants() {
        throw new IllegalStateException("Utility class");
    }

    public static final String APP_NAME = "Chinese Learning";
    public static final String DEFAULT_LANGUAGE = "vi";
    public static final String DEFAULT_TIME_ZONE = "Asia/Ho_Chi_Minh";
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    public static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static final int DEFAULT_PAGE_NUMBER = 0;
    public static final int DEFAULT_PAGE_SIZE = 20;
    public static final int MAX_PAGE_SIZE = 100;

    public static final int MIN_PASSWORD_LENGTH = 8;
    public static final int MAX_PASSWORD_LENGTH = 100;
    public static final int MAX_UPLOAD_SIZE_MB = 20;

    public static final String API_PREFIX = "/api/v1";
    public static final String SYSTEM_USER = "SYSTEM";
}
