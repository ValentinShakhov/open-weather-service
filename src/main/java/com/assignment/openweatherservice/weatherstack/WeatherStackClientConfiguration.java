package com.assignment.openweatherservice.weatherstack;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Configuration
@EnableConfigurationProperties(WeatherStackClientConfigurationProperties.class)
public class WeatherStackClientConfiguration {

    private static final String SCHEME = "http";

    @Bean
    public RestTemplate weatherStackRestTemplate(RestTemplateBuilder restTemplateBuilder,
                                                 WeatherStackClientConfigurationProperties configurationProperties,
                                                 WeatherStackClientErrorHandler errorHandler,
                                                 WeatherStackClientAccessKeyAppender requestInterceptor) {
        String rootUri = UriComponentsBuilder.newInstance()
                .scheme(SCHEME)
                .host(configurationProperties.getHost())
                .port(configurationProperties.getPort())
                .build()
                .toUriString();

        return restTemplateBuilder
                .rootUri(rootUri)
                .errorHandler(errorHandler)
                .interceptors(requestInterceptor)
                .requestFactory(() -> new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()))
                .build();
    }
}
