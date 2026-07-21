package com.aliu.be.exception;

import org.springframework.http.HttpStatus;

/**
 * Danh sách mã lỗi nghiệp vụ thống nhất của hệ thống.
 */
public enum ErrorCode {

    // Lỗi chung
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "SYS_001", "Đã xảy ra lỗi hệ thống"),
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "REQ_001", "Dữ liệu yêu cầu không hợp lệ"),
    VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "REQ_002", "Dữ liệu không vượt qua kiểm tra hợp lệ"),
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "RES_001", "Không tìm thấy dữ liệu"),
    DUPLICATE_RESOURCE(HttpStatus.CONFLICT, "RES_002", "Dữ liệu đã tồn tại"),

    // Xác thực và phân quyền
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "AUTH_001", "Bạn cần đăng nhập để sử dụng chức năng này"),
    FORBIDDEN(HttpStatus.FORBIDDEN, "AUTH_002", "Bạn không có quyền thực hiện chức năng này"),
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "AUTH_003", "Tên đăng nhập hoặc mật khẩu không chính xác"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH_004", "Token không hợp lệ"),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH_005", "Token đã hết hạn"),
    REVOKED_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH_006", "Token đã bị thu hồi"),

    // Người dùng
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER_001", "Không tìm thấy người dùng"),
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "USER_002", "Email đã được sử dụng"),
    USERNAME_ALREADY_EXISTS(HttpStatus.CONFLICT, "USER_003", "Tên đăng nhập đã được sử dụng"),
    USER_ACCOUNT_LOCKED(HttpStatus.FORBIDDEN, "USER_004", "Tài khoản đã bị khóa"),

    // Nội dung học tiếng Trung
    HSK_LEVEL_NOT_FOUND(HttpStatus.NOT_FOUND, "HSK_001", "Không tìm thấy cấp độ HSK"),
    VOCABULARY_NOT_FOUND(HttpStatus.NOT_FOUND, "VOC_001", "Không tìm thấy từ vựng"),
    VOCABULARY_ALREADY_EXISTS(HttpStatus.CONFLICT, "VOC_002", "Từ vựng đã tồn tại"),
    GRAMMAR_NOT_FOUND(HttpStatus.NOT_FOUND, "GRAM_001", "Không tìm thấy ngữ pháp"),
    COURSE_NOT_FOUND(HttpStatus.NOT_FOUND, "COURSE_001", "Không tìm thấy khóa học"),
    LESSON_NOT_FOUND(HttpStatus.NOT_FOUND, "LESSON_001", "Không tìm thấy bài học"),
    QUIZ_NOT_FOUND(HttpStatus.NOT_FOUND, "QUIZ_001", "Không tìm thấy bài kiểm tra"),

    // File và media
    FILE_EMPTY(HttpStatus.BAD_REQUEST, "FILE_001", "File tải lên không được để trống"),
    FILE_TYPE_NOT_SUPPORTED(HttpStatus.BAD_REQUEST, "FILE_002", "Loại file không được hỗ trợ"),
    FILE_SIZE_EXCEEDED(HttpStatus.PAYLOAD_TOO_LARGE, "FILE_003", "Kích thước file vượt quá giới hạn"),
    FILE_STORAGE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "FILE_004", "Không thể lưu file"),
    FILE_NOT_FOUND(HttpStatus.NOT_FOUND, "FILE_005", "Không tìm thấy file");

    private final HttpStatus httpStatus;
    private final String code;
    private final String defaultMessage;

    ErrorCode(HttpStatus httpStatus, String code, String defaultMessage) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.defaultMessage = defaultMessage;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getCode() {
        return code;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }
}
