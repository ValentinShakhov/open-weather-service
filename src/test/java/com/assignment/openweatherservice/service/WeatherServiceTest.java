package com.assignment.openweatherservice.service;

import com.assignment.openweatherservice.api.WeatherResponse;
import com.assignment.openweatherservice.config.CachingConfiguration;
import com.assignment.openweatherservice.weatherstack.WeatherStackClient;
import com.assignment.openweatherservice.weatherstack.response.Current;
import com.assignment.openweatherservice.weatherstack.response.Location;
import com.assignment.openweatherservice.weatherstack.response.WeatherStackResponse;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import redis.embedded.RedisServer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Import({CachingConfiguration.class, WeatherService.class})
@ExtendWith(SpringExtension.class)
@ImportAutoConfiguration(classes = {CacheAutoConfiguration.class, RedisAutoConfiguration.class})
@TestPropertySource("classpath:/caching-configuration.properties")
class WeatherServiceTest {

    private static final RedisServer REDIS_SERVER = new RedisServer(6380);

    @MockBean
    private WeatherStackClient weatherStackClient;

    @Autowired
    private WeatherService weatherService;

    @BeforeAll
    public static void init() {
        REDIS_SERVER.start();
    }

    @AfterAll
    public static void shutdown() {
        REDIS_SERVER.stop();
    }

    @Test
    void givenRedisCaching_whenFindItemById_thenItemReturnedFromCache() {
        String city = "someCity";

        WeatherStackResponse clientResponse = mock(WeatherStackResponse.class);
        Location location = mock(Location.class);
        Current current = mock(Current.class);

        when(weatherStackClient.getByCity(city)).thenReturn(clientResponse);
        when(clientResponse.getLocation()).thenReturn(location);
        when(clientResponse.getCurrent()).thenReturn(current);
        when(location.getName()).thenReturn(city);
        when(current.getTemperature()).thenReturn(13);

        WeatherResponse resultCacheMiss = weatherService.getByCity(city);
        WeatherResponse resultCacheHit = weatherService.getByCity(city);

        verify(weatherStackClient, times(1)).getByCity(city);
        assertEquals(resultCacheMiss, resultCacheHit);
    }
}