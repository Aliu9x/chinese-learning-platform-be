package com.aliu.be.exception;

import java.util.Objects;

/**
 * Exception nền tảng cho các lỗi nghiệp vụ có ErrorCode.
 */
public class BusinessException extends RuntimeException {

    private final ErrorCode errorCode;

    public BusinessException(ErrorCode errorCode) {
        super(Objects.requireNonNull(errorCode, "errorCode không được null").getDefaultMessage());
        this.errorCode = errorCode;
    }

    public BusinessException(ErrorCode errorCode, String message) {
        super(message == null || message.isBlank()
                ? Objects.requireNonNull(errorCode, "errorCode không được null").getDefaultMessage()
                : message);
        this.errorCode = errorCode;
    }

    public BusinessException(ErrorCode errorCode, String message, Throwable cause) {
        super(message == null || message.isBlank()
                ? Objects.requireNonNull(errorCode, "errorCode không được null").getDefaultMessage()
                : message, cause);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
