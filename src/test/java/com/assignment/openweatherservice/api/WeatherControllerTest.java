package com.assignment.openweatherservice.api;

import com.assignment.openweatherservice.service.WeatherService;
import com.assignment.openweatherservice.weatherstack.WeatherStackClientException;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WeatherController.class)
class WeatherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WeatherService weatherService;

    @Test
    void shouldReturnWeatherResponse() throws Exception {
        String responseBody = IOUtils.toString(Objects.requireNonNull(getClass().getResourceAsStream("/controller-response.json")), StandardCharsets.UTF_8);
        String someCity = "someCity";

        when(weatherService.getByCity(someCity)).thenReturn(new WeatherResponse(someCity, 13));

        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/weather/city/{city}", someCity))
                .andExpect(status().isOk())
                .andExpect(content().json(responseBody));
    }

    @Test
    void shouldReturnNotFoundException() throws Exception {
        String nonExistingCity = "nonExistingCity";

        when(weatherService.getByCity(nonExistingCity)).thenThrow(new WeatherStackClientException("404", "City no found"));

        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/weather/city/{city}", nonExistingCity))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnUserFriendlyException() throws Exception {
        String someCity = "someCity";

        when(weatherService.getByCity(someCity)).thenThrow(new RuntimeException("unknown exception"));

        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/weather/city/{city}", someCity))
                .andExpect(status().is5xxServerError());
    }
}