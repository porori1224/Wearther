package com.example.weather.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {
    private final RestTemplate restTemplate = new RestTemplate();
    private final String API_KEY = "your_api_key"; // OpenWeather API 키

    public String getWeather(String city) {
        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + API_KEY + "&units=metric";
        return restTemplate.getForObject(url, String.class);
    }
}