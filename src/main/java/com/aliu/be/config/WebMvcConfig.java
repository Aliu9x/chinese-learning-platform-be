package com.aliu.be.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;

/**
 * Cấu hình Spring MVC và tài nguyên upload cục bộ.
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final Path uploadDirectory;

    public WebMvcConfig() {
        this.uploadDirectory = Path.of("uploads").toAbsolutePath().normalize();
    }

    /**
     * Cho phép truy cập file trong thư mục uploads qua đường dẫn /uploads/**.
     * Khi triển khai production, nên dùng object storage hoặc Nginx thay vì cách này.
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(uploadDirectory.toUri().toString())
                .setCachePeriod(3600);
    }

    /**
     * Vị trí đăng ký Converter/Formatter dùng chung khi dự án cần mở rộng.
     */
    @Override
    public void addFormatters(FormatterRegistry registry) {
        // Ví dụ sau này có thể thêm converter từ String sang enum hoặc ID nghiệp vụ.
    }
}
