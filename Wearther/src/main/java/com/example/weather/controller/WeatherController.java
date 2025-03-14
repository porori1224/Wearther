package com.example.weather.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.weather.service.WeatherService;

@RestController
public class WeatherController {
    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/api/weather") // ✅ API 경로 수정
    public String getWeather(@RequestParam(required = false, defaultValue = "Seoul") String city) {
        return weatherService.getWeatherWithAirQuality(city); // 날씨 + 미세먼지 정보 반환
    }
}