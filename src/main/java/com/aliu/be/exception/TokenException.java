package com.aliu.be.exception;

/**
 * Dùng cho lỗi access token và refresh token.
 */
public class TokenException extends BusinessException {

    public TokenException(String message) {
        super(ErrorCode.INVALID_TOKEN, message);
    }

    public TokenException(ErrorCode errorCode) {
        super(errorCode);
    }

    public TokenException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public TokenException(ErrorCode errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }

    public static TokenException invalid() {
        return new TokenException(ErrorCode.INVALID_TOKEN);
    }

    public static TokenException expired() {
        return new TokenException(ErrorCode.EXPIRED_TOKEN);
    }

    public static TokenException revoked() {
        return new TokenException(ErrorCode.REVOKED_TOKEN);
    }
}
