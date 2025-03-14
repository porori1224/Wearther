package com.example.weather.service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.cdimascio.dotenv.Dotenv;

@Service
public class WeatherService {
    private final RestTemplate restTemplate = new RestTemplate();
    private final String API_KEY;

    public WeatherService() {
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
        this.API_KEY = dotenv.get("OpenWeather_API_Key");

        // ✅ API_KEY가 null일 경우 예외 처리 추가
        if (this.API_KEY == null || this.API_KEY.isEmpty()) {
            throw new IllegalStateException("❌ OpenWeather API Key가 설정되지 않았습니다. .env 파일을 확인하세요!");
        }
    }

    public String getWeatherWithAirQuality(String city) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
    
            String encodedCity = URLEncoder.encode(city, StandardCharsets.UTF_8);
            String weatherUrl = "https://api.openweathermap.org/data/2.5/weather?q=" + encodedCity + "&appid=" + API_KEY + "&units=metric&lang=kr";
    
            // ✅ API 요청 URL 로그 출력
            System.out.println("🌍 요청 URL: " + weatherUrl);
            
            // ✅ 날씨 API 응답 처리
            String weatherResponse = restTemplate.getForObject(weatherUrl, String.class);
            System.out.println("✅ 날씨 API 응답: " + weatherResponse);

            JsonNode weatherJson = objectMapper.readTree(weatherResponse);
            if (!weatherJson.has("coord")) {
                throw new IllegalStateException("❌ 날씨 데이터에 'coord' 필드가 없습니다. 응답 확인 필요.");
            }

            double lat = weatherJson.get("coord").get("lat").asDouble();
            double lon = weatherJson.get("coord").get("lon").asDouble();
    
            // ✅ 미세먼지 API 요청
            String airPollutionUrl = "https://api.openweathermap.org/data/2.5/air_pollution?lat=" + lat + "&lon=" + lon + "&appid=" + API_KEY;
            String airResponse = restTemplate.getForObject(airPollutionUrl, String.class);
            System.out.println("✅ 미세먼지 API 응답: " + airResponse);
    
            JsonNode airJson = objectMapper.readTree(airResponse);
            if (!airJson.has("list") || airJson.get("list").isEmpty()) {
                throw new IllegalStateException("❌ 미세먼지 데이터에 'list' 필드가 없거나 비어 있습니다. 응답 확인 필요.");
            }

            int airQualityIndex = airJson.get("list").get(0).get("main").get("aqi").asInt();
    
            // ✅ 미세먼지 등급 변환
            String airQuality;
            switch (airQualityIndex) {
                case 1: airQuality = "좋음"; break;
                case 2: airQuality = "보통"; break;
                case 3: airQuality = "나쁨"; break;
                case 4: airQuality = "매우 나쁨"; break;
                default: airQuality = "위험"; break;
            }

            // ✅ 최종 JSON 응답 개선
            JsonNode finalResponse = objectMapper.createObjectNode()
                    .put("city", weatherJson.get("name").asText())
                    .put("temperature", weatherJson.get("main").get("temp").asDouble())
                    .put("weather", weatherJson.get("weather").get(0).get("description").asText())
                    .put("humidity", weatherJson.get("main").get("humidity").asInt() + "%") // 습도 추가
                    .put("windSpeed", weatherJson.get("wind").get("speed").asDouble() + "m/s") // 바람 속도 추가
                    .put("airQuality", airQuality);

            return objectMapper.writeValueAsString(finalResponse);
        } catch (HttpClientErrorException e) {
            System.err.println("❌ API 요청 오류: " + e.getResponseBodyAsString());
            return "{\"error\": \"API 요청 중 오류가 발생했습니다. (" + e.getStatusCode() + ")\"}";
        } catch (Exception e) {
            e.printStackTrace(); // 🔥 예외 발생 시 로그 출력
            return "{\"error\": \"데이터를 불러올 수 없습니다.\"}";
        }
    }
}