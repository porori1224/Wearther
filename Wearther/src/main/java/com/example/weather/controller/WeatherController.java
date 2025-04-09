package com.example.weather.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.weather.service.OutfitService;
import com.example.weather.service.WeatherService;

@Controller
public class WeatherController {
    private final WeatherService weatherService;
    private final OutfitService outfitService;

    public WeatherController(WeatherService weatherService, OutfitService outfitService) {
        this.weatherService = weatherService;
        this.outfitService = outfitService;
    }

    @GetMapping("/api/weather") // ✅ API 경로 수정
    @ResponseBody
    public String getWeather(
        @RequestParam(required = false) String city,
        @RequestParam(required = false) Double lat,
        @RequestParam(required = false) Double lon
    ) {
        try {
            // ✅ 로그 출력하여 요청된 값 확인
            System.out.println("🌍 [API 요청] city: " + city + ", lat: " + lat + ", lon: " + lon);

            if (lat != null && lon != null) {
                return weatherService.getWeatherByCoords(lat, lon, "현재 위치"); // ✅ "true" 대신 "현재 위치" 전달
            } else if (city != null) {
                return weatherService.getWeatherWithAirQuality(city);
            } else {
                return "{\"error\": \"도시명 또는 위치 정보가 필요합니다.\"}";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"error\": \"서버 내부 오류 발생. 관리자에게 문의하세요.\"}";
        }
    }

    @GetMapping("/recommend")
    @ResponseBody
    public String recommendOutfit(@RequestParam("temperature") double temperature,
                                  @RequestParam("weather") String weatherCondition,
                                  @RequestParam("dust") int dustLevel) {
        return outfitService.recommendOutfit(temperature, weatherCondition, dustLevel);
    }
}