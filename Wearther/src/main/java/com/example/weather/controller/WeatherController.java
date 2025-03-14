package com.example.weather.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WeatherController {

    @GetMapping("/api/wearther") // ✅ `/weather`에서 `/api/weather`로 변경하여 충돌 방지
    public String getWeather(@RequestParam(required = false, defaultValue = "Seoul") String city) {
        return "Weather data for " + city;
    }
}