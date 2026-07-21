package com.aliu.be.exception;

import com.aliu.be.common.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Bắt lỗi tập trung và trả ErrorResponse thống nhất cho frontend.
 */
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleCustomValidation(
            ValidationException exception,
            HttpServletRequest request
    ) {
        ErrorCode errorCode = exception.getErrorCode();

        ErrorResponse response = exception.getFieldErrors().isEmpty()
                ? buildError(
                        errorCode,
                        exception.getMessage(),
                        request.getRequestURI()
                )
                : ErrorResponse.validation(
                        errorCode.getHttpStatus().value(),
                        errorCode.getCode(),
                        exception.getMessage(),
                        request.getRequestURI(),
                        exception.getFieldErrors()
                );

        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(response);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(
            BusinessException exception,
            HttpServletRequest request
    ) {
        ErrorCode errorCode = exception.getErrorCode();

        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(
                        buildError(
                                errorCode,
                                exception.getMessage(),
                                request.getRequestURI()
                        )
                );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(
            ConstraintViolationException exception,
            HttpServletRequest request
    ) {
        Map<String, String> errors = new LinkedHashMap<>();

        for (ConstraintViolation<?> violation :
                exception.getConstraintViolations()) {

            String field = violation
                    .getPropertyPath()
                    .toString();

            errors.put(field, violation.getMessage());
        }

        ErrorCode errorCode = ErrorCode.VALIDATION_FAILED;

        ErrorResponse response = ErrorResponse.validation(
                errorCode.getHttpStatus().value(),
                errorCode.getCode(),
                errorCode.getDefaultMessage(),
                request.getRequestURI(),
                errors
        );

        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(response);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials(
            BadCredentialsException exception,
            HttpServletRequest request
    ) {
        ErrorCode errorCode = ErrorCode.INVALID_CREDENTIALS;

        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(
                        buildError(
                                errorCode,
                                errorCode.getDefaultMessage(),
                                request.getRequestURI()
                        )
                );
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(
            AccessDeniedException exception,
            HttpServletRequest request
    ) {
        ErrorCode errorCode = ErrorCode.FORBIDDEN;

        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(
                        buildError(
                                errorCode,
                                errorCode.getDefaultMessage(),
                                request.getRequestURI()
                        )
                );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrity(
            DataIntegrityViolationException exception,
            HttpServletRequest request
    ) {
        LOGGER.warn(
                "Vi phạm ràng buộc dữ liệu tại {}",
                request.getRequestURI(),
                exception
        );

        ErrorCode errorCode = ErrorCode.DUPLICATE_RESOURCE;

        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(
                        buildError(
                                errorCode,
                                errorCode.getDefaultMessage(),
                                request.getRequestURI()
                        )
                );
    }

    /*
     * MaxUploadSizeExceededException đã được
     * ResponseEntityExceptionHandler quản lý.
     *
     * Vì vậy phải override, không dùng @ExceptionHandler.
     */
    @Override
    protected ResponseEntity<Object> handleMaxUploadSizeExceededException(
            MaxUploadSizeExceededException exception,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        ErrorCode errorCode = ErrorCode.FILE_SIZE_EXCEEDED;

        String message = errorCode.getDefaultMessage();

        if (exception.getMaxUploadSize() > 0) {
            long maxSizeMb =
                    exception.getMaxUploadSize() / (1024 * 1024);

            message = errorCode.getDefaultMessage()
                    + ". Kích thước tối đa cho phép: "
                    + maxSizeMb
                    + " MB";
        }

        ErrorResponse response = buildError(
                errorCode,
                message,
                extractPath(request)
        );

        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedException(
            Exception exception,
            HttpServletRequest request
    ) {
        LOGGER.error(
                "Lỗi hệ thống chưa được xử lý tại {}",
                request.getRequestURI(),
                exception
        );

        ErrorCode errorCode =
                ErrorCode.INTERNAL_SERVER_ERROR;

        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(
                        buildError(
                                errorCode,
                                errorCode.getDefaultMessage(),
                                request.getRequestURI()
                        )
                );
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        Map<String, String> errors = new LinkedHashMap<>();

        for (FieldError fieldError :
                exception.getBindingResult().getFieldErrors()) {

            errors.putIfAbsent(
                    fieldError.getField(),
                    fieldError.getDefaultMessage()
            );
        }

        ErrorCode errorCode =
                ErrorCode.VALIDATION_FAILED;

        ErrorResponse response =
                ErrorResponse.validation(
                        errorCode.getHttpStatus().value(),
                        errorCode.getCode(),
                        errorCode.getDefaultMessage(),
                        extractPath(request),
                        errors
                );

        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(response);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException exception,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        return badRequest(
                "JSON không đúng định dạng hoặc chứa giá trị không hợp lệ",
                request
        );
    }

    @Override
    protected ResponseEntity<Object>
    handleMissingServletRequestParameter(
            MissingServletRequestParameterException exception,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        return badRequest(
                "Thiếu tham số bắt buộc: "
                        + exception.getParameterName(),
                request
        );
    }

    @Override
    protected ResponseEntity<Object>
    handleHttpMediaTypeNotSupported(
            HttpMediaTypeNotSupportedException exception,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        ErrorCode errorCode =
                ErrorCode.INVALID_REQUEST;

        ErrorResponse response = ErrorResponse.of(
                HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(),
                errorCode.getCode(),
                "Content-Type không được hỗ trợ: "
                        + exception.getContentType(),
                extractPath(request)
        );

        return ResponseEntity
                .status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                .body(response);
    }

    private ResponseEntity<Object> badRequest(
            String message,
            WebRequest request
    ) {
        ErrorCode errorCode =
                ErrorCode.INVALID_REQUEST;

        ErrorResponse response = buildError(
                errorCode,
                message,
                extractPath(request)
        );

        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(response);
    }

    private ErrorResponse buildError(
            ErrorCode errorCode,
            String message,
            String path
    ) {
        return ErrorResponse.of(
                errorCode.getHttpStatus().value(),
                errorCode.getCode(),
                message,
                path
        );
    }

    private String extractPath(WebRequest request) {
        String description =
                request.getDescription(false);

        return description.startsWith("uri=")
                ? description.substring(4)
                : description;
    }
}