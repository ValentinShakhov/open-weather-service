package com.assignment.openweatherservice.weatherstack;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.support.HttpRequestWrapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;

@Component
public class WeatherStackClientAccessKeyAppender implements ClientHttpRequestInterceptor {

    private static final String ACCESS_KEY_REQUEST_PARAM = "access_key";

    private final WeatherStackClientConfigurationProperties configurationProperties;

    public WeatherStackClientAccessKeyAppender(WeatherStackClientConfigurationProperties configurationProperties) {
        this.configurationProperties = configurationProperties;
    }

    @Override
    @NonNull
    public ClientHttpResponse intercept(@NonNull HttpRequest request,
                                        @NonNull byte[] body,
                                        ClientHttpRequestExecution execution) throws IOException {
        HttpRequestWrapper httpRequestWrapper = new HttpRequestWrapper(request) {
            @Override
            @NonNull
            public URI getURI() {
                return UriComponentsBuilder.fromUri(request.getURI())
                        .queryParam(ACCESS_KEY_REQUEST_PARAM, configurationProperties.getAccessKey())
                        .build()
                        .toUri();
            }
        };

        return execution.execute(httpRequestWrapper, body);
    }
}
