package com.assignment.openweatherservice.service;

import com.assignment.openweatherservice.api.WeatherResponse;
import com.assignment.openweatherservice.weatherstack.WeatherStackClient;
import com.assignment.openweatherservice.weatherstack.response.WeatherStackResponse;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class WeatherService {

    private final WeatherStackClient weatherStackClient;

    public WeatherService(WeatherStackClient weatherStackClient) {
        this.weatherStackClient = weatherStackClient;
    }

    @Cacheable("weather")
    public WeatherResponse getByCity(String city) {
        WeatherStackResponse weatherStackResponse = weatherStackClient.getByCity(city);

        return new WeatherResponse(weatherStackResponse.getLocation().getName(),
                weatherStackResponse.getCurrent().getTemperature());
    }
}
