package com.assignment.openweatherservice.weatherstack;

import com.assignment.openweatherservice.weatherstack.response.WeatherStackResponse;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {WeatherStackClientConfiguration.class,
        WeatherStackClientErrorHandler.class,
        WeatherStackClientAccessKeyAppender.class,
        WeatherStackClient.class,
        ClientTestConfiguration.class})
@ActiveProfiles("unit")
@TestPropertySource("classpath:/weatherstack/client-configuration.properties")
class WeatherStackClientTest {

    static final String ACCESS_KEY = "someKey";

    private static final String CONTENT_TYPE_HEADER = "Content-type";

    private static final WireMockServer WIRE_MOCK_SERVER = new WireMockServer(8082);

    @Autowired
    private WeatherStackClient weatherStackClient;

    @BeforeAll
    public static void init() {
        WIRE_MOCK_SERVER.start();
    }

    @AfterAll
    public static void shutdown() {
        WIRE_MOCK_SERVER.stop();
    }

    @Test
    public void shouldThrowExceptionUnreadableBody() {
        WIRE_MOCK_SERVER.stubFor(get("/current?query=someCity&access_key=" + ACCESS_KEY)
                .willReturn(aResponse()
                        .withHeader(CONTENT_TYPE_HEADER, MediaType.APPLICATION_JSON.toString())
                        .withStatus(200)
                        .withBody("unreadable json body")));

        assertThrows(WeatherStackClientException.class, () -> weatherStackClient.getByCity("someCity"));
    }

    @Test
    public void shouldThrowExceptionUnrecognizedBody() {
        WIRE_MOCK_SERVER.stubFor(get("/current?query=someCity&access_key=" + ACCESS_KEY)
                .willReturn(aResponse()
                        .withHeader(CONTENT_TYPE_HEADER, MediaType.APPLICATION_JSON.toString())
                        .withStatus(200)
                        .withBody("\"a\":\"b\"")));

        assertThrows(WeatherStackClientException.class, () -> weatherStackClient.getByCity("someCity"));
    }

    @Test
    public void shouldThrowExceptionServerError() {
        WIRE_MOCK_SERVER.stubFor(get("/current?query=someCity&access_key=" + ACCESS_KEY)
                .willReturn(aResponse()
                        .withHeader(CONTENT_TYPE_HEADER, MediaType.APPLICATION_JSON.toString())
                        .withStatus(500)
                        .withBody("Internal server error")));

        try {
            weatherStackClient.getByCity("someCity");
        } catch (WeatherStackClientException e) {
            assertEquals("500", e.getCode());
        }
    }

    @Test
    public void shouldThrowExceptionServerRecognizedError() throws IOException {
        String responseBody = IOUtils.toString(Objects.requireNonNull(getClass().getResourceAsStream("/weatherstack/stubs/recognized-error-response.json")), StandardCharsets.UTF_8);

        WIRE_MOCK_SERVER.stubFor(get("/current?query=someCity&access_key=" + ACCESS_KEY)
                .willReturn(aResponse()
                        .withHeader(CONTENT_TYPE_HEADER, MediaType.APPLICATION_JSON.toString())
                        .withStatus(200)
                        .withBody(responseBody)));

        try {
            weatherStackClient.getByCity("someCity");
        } catch (WeatherStackClientException e) {
            assertEquals("104", e.getCode());
        }
    }

    @Test
    public void shouldReturnProperResponse() throws IOException {
        String responseBody = IOUtils.toString(Objects.requireNonNull(getClass().getResourceAsStream("/weatherstack/stubs/valid-response.json")), StandardCharsets.UTF_8);

        WIRE_MOCK_SERVER.stubFor(get("/current?query=someCity&access_key=" + ACCESS_KEY)
                .willReturn(aResponse()
                        .withHeader(CONTENT_TYPE_HEADER, MediaType.APPLICATION_JSON.toString())
                        .withStatus(200)
                        .withBody(responseBody)));

        WeatherStackResponse response = weatherStackClient.getByCity("someCity");
        assertEquals("someCity", response.getLocation().getName());
        assertEquals(13, response.getCurrent().getTemperature());
    }
}