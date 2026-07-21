package com.aliu.be.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;

/**
 * Cấu hình đa ngôn ngữ tiếng Việt, tiếng Trung và tiếng Anh.
 */
@Configuration
public class LocaleConfig {

    public static final Locale VIETNAMESE = Locale.forLanguageTag("vi-VN");
    public static final Locale SIMPLIFIED_CHINESE = Locale.SIMPLIFIED_CHINESE;
    public static final Locale ENGLISH = Locale.ENGLISH;

    @Bean
    public LocaleResolver localeResolver() {
        AcceptHeaderLocaleResolver resolver = new AcceptHeaderLocaleResolver();
        resolver.setDefaultLocale(VIETNAMESE);
        resolver.setSupportedLocales(List.of(VIETNAMESE, SIMPLIFIED_CHINESE, ENGLISH));
        return resolver;
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource source =
                new ReloadableResourceBundleMessageSource();
        source.setBasenames("classpath:i18n/messages");
        source.setDefaultEncoding(StandardCharsets.UTF_8.name());
        source.setFallbackToSystemLocale(false);
        source.setUseCodeAsDefaultMessage(true);
        source.setCacheSeconds(3600);
        return source;
    }
}
