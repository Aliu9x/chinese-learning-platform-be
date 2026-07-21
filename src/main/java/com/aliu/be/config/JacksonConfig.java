package com.aliu.be.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.ZoneId;
import java.util.TimeZone;

/**
 * Cấu hình chuyển đổi Java Object và JSON.
 */
@Configuration
public class JacksonConfig {

    // @Bean
    // public Jackson2ObjectMapperBuilderCustomizer jacksonCustomizer() {
    //     return builder -> builder
    //             .modulesToInstall(new JavaTimeModule())
    //             .serializationInclusion(JsonInclude.Include.NON_NULL)
    //             .featuresToDisable(
    //                     SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,
    //                     DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES
    //             )
    //             .timeZone(TimeZone.getTimeZone(ZoneId.of("Asia/Ho_Chi_Minh")));
    // }
}
