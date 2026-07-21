package com.aliu.be.exception;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Dùng cho lỗi kiểm tra dữ liệu nghiệp vụ ngoài Bean Validation.
 */
public class ValidationException extends BusinessException {

    private final Map<String, String> fieldErrors;

    public ValidationException(String message) {
        super(ErrorCode.VALIDATION_FAILED, message);
        this.fieldErrors = Collections.emptyMap();
    }

    public ValidationException(String field, String message) {
        super(ErrorCode.VALIDATION_FAILED, "Dữ liệu không hợp lệ");
        this.fieldErrors = Map.of(field, message);
    }

    public ValidationException(Map<String, String> fieldErrors) {
        super(ErrorCode.VALIDATION_FAILED);
        this.fieldErrors = fieldErrors == null
                ? Collections.emptyMap()
                : Collections.unmodifiableMap(new LinkedHashMap<>(fieldErrors));
    }

    public Map<String, String> getFieldErrors() {
        return fieldErrors;
    }
}
