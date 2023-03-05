package com.assignment.openweatherservice.api;

import java.io.Serializable;

public record WeatherResponse(String city, int temperature) implements Serializable {
}
