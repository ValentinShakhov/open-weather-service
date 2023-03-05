package com.assignment.openweatherservice.weatherstack;

import com.assignment.openweatherservice.weatherstack.response.Error;
import com.assignment.openweatherservice.weatherstack.response.ErrorResponse;
import com.assignment.openweatherservice.weatherstack.response.WeatherStackResponse;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.client.DefaultResponseErrorHandler;

import java.io.IOException;

@Component
public class WeatherStackClientErrorHandler extends DefaultResponseErrorHandler {

    private final Logger logger = LoggerFactory.getLogger(WeatherStackClientErrorHandler.class);

    private final ObjectMapper objectMapper;

    public WeatherStackClientErrorHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean hasError(@NonNull ClientHttpResponse httpResponse) throws IOException {
        if (httpResponse.getStatusCode().isError()) {
            return true;
        }

        try {
            WeatherStackResponse response = objectMapper.readValue(httpResponse.getBody(), WeatherStackResponse.class);
            return response.getCurrent() == null;
        } catch (Exception e) {
            return true;
        }
    }

    @Override
    public void handleError(ClientHttpResponse httpResponse) throws IOException {
        logger.error(new String(httpResponse.getBody().readAllBytes()));

        if (httpResponse.getStatusCode().isError()) {
            throw new WeatherStackClientException(String.valueOf(httpResponse.getStatusCode().value()), "Server error");
        }

        try {
            Error error = objectMapper.readValue(httpResponse.getBody(), ErrorResponse.class).getError();
            throw new WeatherStackClientException(error.getCode(), error.getInfo());
        } catch (MismatchedInputException | JsonParseException e) {
            logger.error(e.getMessage());
            throw new WeatherStackClientException("Unknown error", "Unable to read response error");
        }
    }
}