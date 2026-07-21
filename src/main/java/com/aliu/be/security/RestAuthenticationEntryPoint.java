package com.aliu.be.security;


import com.aliu.be.common.dto.ErrorResponse;
import com.aliu.be.exception.BusinessException;
import com.aliu.be.exception.ErrorCode;
import tools.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Trả JSON 401 khi request chưa được xác thực hoặc JWT không hợp lệ.
 */
@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    public RestAuthenticationEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authenticationException
    ) throws IOException, ServletException {
        Object authenticationError = request.getAttribute("authentication_error");

        ErrorCode errorCode = authenticationError instanceof BusinessException businessException
                ? businessException.getErrorCode()
                : ErrorCode.UNAUTHORIZED;
        String message = authenticationError instanceof Exception exception
                ? exception.getMessage()
                : errorCode.getDefaultMessage();

        ErrorResponse errorResponse = ErrorResponse.of(
                errorCode.getHttpStatus().value(),
                errorCode.getCode(),
                message,
                request.getRequestURI()
        );

        response.setStatus(errorCode.getHttpStatus().value());
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        objectMapper.writeValue(response.getOutputStream(), errorResponse);
    }
}
