package com.assignment.openweatherservice.api;

import com.assignment.openweatherservice.weatherstack.WeatherStackClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerErrorHandler {

    private final Logger logger = LoggerFactory.getLogger(ControllerErrorHandler.class);

    /*
     ** More cases should be specified here to display proper errors to the user. Tests should be added accordingly
     */
    @ExceptionHandler(WeatherStackClientException.class)
    public ResponseEntity<String> handleWeatherStackException(WeatherStackClientException exception) {
        switch (exception.getCode()) {
            case "400" -> {
                return ResponseEntity.badRequest().body(exception.getMessage());
            }
            case "404" -> {
                return ResponseEntity.notFound().build();
            }
            default -> {
                return ResponseEntity.internalServerError().body(exception.getMessage());
            }
        }
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAnyException(Exception exception) {
        logger.error(exception.getMessage());
        return handleWeatherStackException(new WeatherStackClientException("Unknown", "Unknown exception"));
    }
}
