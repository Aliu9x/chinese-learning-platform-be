package com.aliu.be.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import tools.jackson.databind.ObjectMapper;

import java.time.Duration;
import java.util.Map;

/**
 * Cấu hình cache bằng Redis cho dữ liệu HSK, chủ đề,
 * từ vựng và khóa học.
 */
@Configuration
@EnableCaching
public class CacheConfig {

    public static final String HSK_LEVELS_CACHE = "hsk-levels";
    public static final String TOPICS_CACHE = "topics";
    public static final String VOCABULARIES_CACHE = "vocabularies";
    public static final String COURSES_CACHE = "courses";

    @Bean
    public CacheManager cacheManager(
            RedisConnectionFactory connectionFactory,
            ObjectMapper objectMapper,
            @Value("${app.cache.default-ttl-minutes:30}")
            long defaultTtlMinutes
    ) {

        GenericJacksonJsonRedisSerializer jsonSerializer =
                new GenericJacksonJsonRedisSerializer(objectMapper);

        RedisCacheConfiguration defaultConfiguration =
                RedisCacheConfiguration
                        .defaultCacheConfig()
                        .entryTtl(Duration.ofMinutes(defaultTtlMinutes))
                        .disableCachingNullValues()
                        .serializeKeysWith(
                                RedisSerializationContext.SerializationPair
                                        .fromSerializer(
                                                new StringRedisSerializer()
                                        )
                        )
                        .serializeValuesWith(
                                RedisSerializationContext.SerializationPair
                                        .fromSerializer(jsonSerializer)
                        );

        Map<String, RedisCacheConfiguration> cacheConfigurations =
                Map.of(
                        HSK_LEVELS_CACHE,
                        defaultConfiguration.entryTtl(
                                Duration.ofHours(12)
                        ),

                        TOPICS_CACHE,
                        defaultConfiguration.entryTtl(
                                Duration.ofHours(6)
                        ),

                        VOCABULARIES_CACHE,
                        defaultConfiguration.entryTtl(
                                Duration.ofMinutes(30)
                        ),

                        COURSES_CACHE,
                        defaultConfiguration.entryTtl(
                                Duration.ofHours(1)
                        )
                );

        return RedisCacheManager
                .builder(connectionFactory)
                .cacheDefaults(defaultConfiguration)
                .withInitialCacheConfigurations(cacheConfigurations)
                .transactionAware()
                .build();
    }
}