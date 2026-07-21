package com.aliu.be.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Clock;

/**
 * Khai báo các Bean nền tảng được sử dụng trong nhiều module.
 */
@Configuration
public class ApplicationConfig {

    /**
     * Mã hóa mật khẩu người dùng bằng BCrypt.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    /**
     * Cung cấp Clock để Service có thể lấy thời gian và dễ viết unit test.
     */
    @Bean
    public Clock applicationClock() {
        return Clock.systemDefaultZone();
    }
}
