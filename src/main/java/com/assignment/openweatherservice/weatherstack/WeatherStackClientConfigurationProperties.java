package com.assignment.openweatherservice.weatherstack;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.lang.NonNull;

@ConfigurationProperties(prefix = "client.weather-stack")
public class WeatherStackClientConfigurationProperties {

    @NonNull
    private final String host;

    @NonNull
    private final Integer port;

    @NonNull
    private final String accessKey;

    @ConstructorBinding
    public WeatherStackClientConfigurationProperties(String host, int port, String accessKey) {
        this.host = host;
        this.port = port;
        this.accessKey = accessKey;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getAccessKey() {
        return accessKey;
    }
}
