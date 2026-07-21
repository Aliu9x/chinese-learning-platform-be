package com.aliu.be.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Cấu hình Swagger/OpenAPI để xem và kiểm thử API.
 */
@Configuration
public class OpenApiConfig {

    private static final String SECURITY_SCHEME_NAME = "bearerAuth";

    @Bean
    public OpenAPI chineseLearningOpenApi() {
        SecurityScheme jwtScheme = new SecurityScheme()
                .name(SECURITY_SCHEME_NAME)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .description("Nhập JWT access token, không cần thêm tiền tố Bearer");

        return new OpenAPI()
                .info(new Info()
                        .title("Chinese Learning API")
                        .description("API cho hệ thống học tiếng Trung")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Chinese Learning Development Team")
                                .email("admin@chineselearning.local"))
                        .license(new License().name("Private Project")))
                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEME_NAME, jwtScheme))
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME));
    }
}
