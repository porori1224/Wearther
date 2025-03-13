package com.example.weather.controller;

import com.example.weather.service.WeatherService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class WeatherController {
    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/weather")
    public String getWeather(@RequestParam String city, Model model) {
        String weatherData = weatherService.getWeather(city);
        model.addAttribute("weather", weatherData);
        return "weather"; // templates/weather.html을 렌더링
    }
}