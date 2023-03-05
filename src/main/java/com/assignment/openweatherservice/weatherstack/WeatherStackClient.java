package com.assignment.openweatherservice.weatherstack;

import com.assignment.openweatherservice.weatherstack.response.WeatherStackResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class WeatherStackClient {

    private final RestTemplate weatherStackRestTemplate;

    public WeatherStackClient(RestTemplate weatherStackRestTemplate) {
        this.weatherStackRestTemplate = weatherStackRestTemplate;
    }

    public WeatherStackResponse getByCity(String city) {
        return weatherStackRestTemplate.getForObject("/current?query={city}", WeatherStackResponse.class, city);
    }
}
