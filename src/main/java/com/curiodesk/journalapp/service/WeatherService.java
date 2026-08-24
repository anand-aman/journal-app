package com.curiodesk.journalapp.service;

import com.curiodesk.journalapp.api.response.WeatherResponse;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {
    /// email (WeatherStack): sahepe5919@bocably.com

    private static final String apiKey = "b7b3fd51f83881e2164ebb4106115d46";

    private static final String baseUrl = "http://api.weatherstack.com/current";

    private final RestTemplate restTemplate;

    public WeatherService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public WeatherResponse getWeather(String city) {
        String finalUrl =  baseUrl + "?access_key=" + apiKey + "&query=" + city;
        ResponseEntity<WeatherResponse> response = restTemplate.exchange(finalUrl, HttpMethod.GET, null, WeatherResponse.class);
        return response.getBody();
    }

}
