package com.assignment.openweatherservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.lang.NonNull;

@ConfigurationProperties(prefix = "caching")
public class CachingConfigurationProperties {

    @NonNull
    private final Integer ttl;

    @ConstructorBinding
    public CachingConfigurationProperties(Integer ttl) {
        this.ttl = ttl;
    }

    public Integer getTtl() {
        return ttl;
    }
}
