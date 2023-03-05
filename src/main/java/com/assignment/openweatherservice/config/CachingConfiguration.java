package com.assignment.openweatherservice.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.cache.RedisCacheConfiguration;

import java.time.Duration;

@Profile("!no-cache")
@Configuration
@EnableCaching
@EnableConfigurationProperties(CachingConfigurationProperties.class)
public class CachingConfiguration {

    @Bean
    public RedisCacheConfiguration cacheConfiguration(CachingConfigurationProperties configurationProperties) {
        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(configurationProperties.getTtl()));
    }
}