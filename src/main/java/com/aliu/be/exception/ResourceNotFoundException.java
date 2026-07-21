package com.aliu.be.exception;

/**
 * Dùng khi không tìm thấy user, từ vựng, khóa học, bài học hoặc tài nguyên khác.
 */
public class ResourceNotFoundException extends BusinessException {

    public ResourceNotFoundException(String message) {
        super(ErrorCode.RESOURCE_NOT_FOUND, message);
    }

    public ResourceNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }

    public ResourceNotFoundException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(
                ErrorCode.RESOURCE_NOT_FOUND,
                String.format("Không tìm thấy %s với %s = %s", resourceName, fieldName, fieldValue)
        );
    }
}
