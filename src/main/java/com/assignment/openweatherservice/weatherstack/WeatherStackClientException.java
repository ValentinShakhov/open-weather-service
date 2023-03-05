package com.assignment.openweatherservice.weatherstack;

public class WeatherStackClientException extends RuntimeException {

    private final String code;
    private final String message;

    public WeatherStackClientException(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
