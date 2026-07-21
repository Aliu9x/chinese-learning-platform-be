package com.aliu.be.exception;

/**
 * Dùng cho lỗi tải lên, đọc, lưu hoặc xóa image, audio và video.
 */
public class FileStorageException extends BusinessException {

    public FileStorageException(String message) {
        super(ErrorCode.FILE_STORAGE_FAILED, message);
    }

    public FileStorageException(String message, Throwable cause) {
        super(ErrorCode.FILE_STORAGE_FAILED, message, cause);
    }

    public FileStorageException(ErrorCode errorCode) {
        super(errorCode);
    }

    public FileStorageException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public FileStorageException(ErrorCode errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }
}
