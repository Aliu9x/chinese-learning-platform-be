package com.aliu.be.common.util;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

/** Tiện ích lấy thông tin người dùng hiện tại từ Spring Security. */
public final class SecurityUtils {

    private SecurityUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static Optional<Authentication> getCurrentAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            return Optional.empty();
        }
        return Optional.of(authentication);
    }

    public static Optional<String> getCurrentUsername() {
        return getCurrentAuthentication().map(Authentication::getName);
    }

    public static String getRequiredCurrentUsername() {
        return getCurrentUsername().orElseThrow(
                () -> new IllegalStateException("Không tìm thấy người dùng đang đăng nhập")
        );
    }

    public static boolean isAuthenticated() {
        return getCurrentAuthentication().isPresent();
    }

    public static boolean hasAuthority(String authority) {
        if (StringUtils.isBlank(authority)) return false;
        return getCurrentAuthentication()
                .stream()
                .flatMap(authentication -> authentication.getAuthorities().stream())
                .map(GrantedAuthority::getAuthority)
                .anyMatch(authority::equals);
    }

    public static boolean hasRole(String role) {
        if (StringUtils.isBlank(role)) return false;
        String normalizedRole = role.startsWith("ROLE_") ? role : "ROLE_" + role;
        return hasAuthority(normalizedRole);
    }
}
