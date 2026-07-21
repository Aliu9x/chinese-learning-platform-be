package com.aliu.be.common.constant;

/**
 * Các hằng số phục vụ xác thực và phân quyền.
 */
public final class SecurityConstants {

    private SecurityConstants() {
        throw new IllegalStateException("Utility class");
    }

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String TOKEN_TYPE = "Bearer";

    public static final String ACCESS_TOKEN_COOKIE = "access_token";
    public static final String REFRESH_TOKEN_COOKIE = "refresh_token";

    public static final long ACCESS_TOKEN_EXPIRATION_MS = 15 * 60 * 1000L;
    public static final long REFRESH_TOKEN_EXPIRATION_MS = 7 * 24 * 60 * 60 * 1000L;

    public static final String ROLE_PREFIX = "ROLE_";
    public static final String ROLE_USER = "ROLE_USER";
    public static final String ROLE_TEACHER = "ROLE_TEACHER";
    public static final String ROLE_ADMIN = "ROLE_ADMIN";

    public static final String[] PUBLIC_ENDPOINTS = {
            "/api/v1/auth/**",
            "/api/v1/public/**",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/actuator/health"
    };
}
