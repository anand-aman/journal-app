package com.curiodesk.journalapp.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record WeatherResponse(
        Request request,
        Location location,
        Current current
) {
    public record Request(
            String type,
            String query,
            String language,
            String unit
    ) {
    }

    public record Location(
            String name,
            String country,
            String region,
            String lat,
            String lon,
            @JsonProperty("timezone_id") String timezoneId,
            @JsonProperty("localtime") String localTime,
            @JsonProperty("localtime_epoch") int localTimeEpoch,
            @JsonProperty("utc_offset") String utcOffset
    ) {
    }

    public record Current(
            @JsonProperty("observation_time") String observationTime,
            int temperature,
            @JsonProperty("weather_code") int weatherCode,
            @JsonProperty("weather_icons") List<String> weatherIcons,
            @JsonProperty("weather_descriptions") List<String> weatherDescriptions,
            Astro astro,
            @JsonProperty("air_quality") AirQuality airQuality,
            @JsonProperty("wind_speed") int windSpeed,
            @JsonProperty("wind_degree") int windDegree,
            @JsonProperty("wind_dir") String windDir,
            int pressure,
            int precip,
            int humidity,
            @JsonProperty("cloudcover") int cloudCover,
            @JsonProperty("feelslike") int feelsLike,
            @JsonProperty("uv_index") int uvIndex,
            int visibility,
            @JsonProperty("is_day") String isDay
    ) {
    }

    public record Astro(
            String sunrise,
            String sunset,
            String moonrise,
            String moonset,
            @JsonProperty("moon_phase") String moonPhase,
            @JsonProperty("moon_illumination") int moonIllumination
    ) {
    }

    public record AirQuality(
            String co,
            String no2,
            String o3,
            String so2,
            @JsonProperty("pm2_5") String pm25,
            String pm10,
            @JsonProperty("us-epa-index") String usEpaIndex,
            @JsonProperty("gb-defra-index") String gbDefraIndex
    ) {
    }
}
