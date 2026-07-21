package com.aliu.be.common.dto;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Cấu trúc lỗi thống nhất trả về cho frontend.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
        boolean success,
        int status,
        String errorCode,
        String message,
        String path,
        Map<String, String> validationErrors,
        LocalDateTime timestamp
) {
    public ErrorResponse {
        success = false;
        validationErrors = validationErrors == null ? null : Map.copyOf(validationErrors);
    }

    public static ErrorResponse of(int status, String errorCode, String message, String path) {
        return new ErrorResponse(
                false, status, errorCode, message, path, null, LocalDateTime.now()
        );
    }

    public static ErrorResponse validation(
            int status,
            String errorCode,
            String message,
            String path,
            Map<String, String> validationErrors
    ) {
        return new ErrorResponse(
                false, status, errorCode, message, path,
                validationErrors, LocalDateTime.now()
        );
    }
}
