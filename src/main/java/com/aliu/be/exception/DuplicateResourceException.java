package com.aliu.be.exception;

/**
 * Dùng khi tạo dữ liệu trùng lặp như email, username hoặc từ vựng.
 */
public class DuplicateResourceException extends BusinessException {

    public DuplicateResourceException(String message) {
        super(ErrorCode.DUPLICATE_RESOURCE, message);
    }

    public DuplicateResourceException(ErrorCode errorCode) {
        super(errorCode);
    }

    public DuplicateResourceException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public DuplicateResourceException(String resourceName, String fieldName, Object fieldValue) {
        super(
                ErrorCode.DUPLICATE_RESOURCE,
                String.format("%s với %s = %s đã tồn tại", resourceName, fieldName, fieldValue)
        );
    }
}
