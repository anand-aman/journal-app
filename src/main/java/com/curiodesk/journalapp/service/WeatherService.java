package com.curiodesk.journalapp.service;

import com.curiodesk.journalapp.api.response.WeatherResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class WeatherService {
    /// email (WeatherStack): sahepe5919@bocably.com

    private static final String apiKey = "b7b3fd51f83881e2164ebb4106115d46";

    private static final String baseUrl = "http://api.weatherstack.com/current";

    // private final RestTemplate restTemplate; // Old approach (blocking client used before RestClient)
    private final RestClient restClient;

    public WeatherService(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.build();
        // this.restTemplate = restTemplate; // Old approach
    }

    public WeatherResponse getWeather(String city) {
        String finalUrl = UriComponentsBuilder.fromUriString(baseUrl)
                .queryParam("access_key", apiKey)
                .queryParam("query", city)
                .toUriString();

        // Old RestTemplate approach:
        // ResponseEntity<WeatherResponse> response = restTemplate.exchange(finalUrl, HttpMethod.GET, null, WeatherResponse.class);
        // return response.getBody();

        return restClient.get()
                .uri(finalUrl)
                .retrieve()
                .body(WeatherResponse.class);
    }

}
