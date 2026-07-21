package com.aliu.be.security;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Đưa CustomUserDetails của người dùng hiện tại vào tham số Controller.
 * Ví dụ: getProfile(@CurrentUser CustomUserDetails currentUser).
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface CurrentUser {

    /**
     * Nếu true, request bắt buộc phải có người dùng đăng nhập.
     */
    boolean required() default true;
}
