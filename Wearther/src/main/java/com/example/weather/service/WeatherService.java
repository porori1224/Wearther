package com.example.weather.service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.cdimascio.dotenv.Dotenv;

@Service
public class WeatherService {
    private final RestTemplate restTemplate = new RestTemplate();
    private final String API_KEY;
    private final DecimalFormat df = new DecimalFormat("#.#"); // ✅ 소수점 1자리까지 표현

    public WeatherService() {
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
        this.API_KEY = dotenv.get("OpenWeather_API_Key");

        // ✅ API_KEY가 설정되지 않았을 경우 예외 처리
        if (this.API_KEY == null || this.API_KEY.isEmpty()) {
            throw new IllegalStateException("❌ OpenWeather API Key가 설정되지 않았습니다. .env 파일을 확인하세요!");
        }
    }

    /**
     * ✅ 한글 도시명을 영문으로 변환 후 날씨 조회
     */
    public String getWeatherWithAirQuality(String city) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            // ✅ 한글 도시명을 영문으로 변환하는 API 호출
            String geoUrl = "https://api.openweathermap.org/geo/1.0/direct?q=" + URLEncoder.encode(city, StandardCharsets.UTF_8) + "&limit=1&appid=" + API_KEY;
            System.out.println("🌍 위치 변환 요청 URL: " + geoUrl);

            String geoResponse = restTemplate.getForObject(geoUrl, String.class);
            JsonNode geoJson = objectMapper.readTree(geoResponse);

            // ✅ 변환된 도시가 없을 경우 예외 처리
            if (geoJson.isEmpty() || geoJson.get(0) == null) {
                return "{\"error\": \"해당 도시를 찾을 수 없습니다.\"}";
            }

            // ✅ 변환된 도시명 및 좌표 가져오기
            String englishCity = geoJson.get(0).get("name").asText().replace("-si", "").trim();
            double lat = geoJson.get(0).get("lat").asDouble();
            double lon = geoJson.get(0).get("lon").asDouble();
            System.out.println("✅ 변환된 도시명: " + englishCity);

            return getWeatherByCoords(lat, lon, "검색 위치");
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"error\": \"데이터를 불러올 수 없습니다.\"}";
        }
    }

    /**
     * ✅ 위도·경도를 기반으로 날씨 정보 조회
     */
    public String getWeatherByCoords(double lat, double lon, String locationType) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            // ✅ 날씨 API 호출
            String weatherUrl = "https://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon=" + lon + "&appid=" + API_KEY + "&units=metric&lang=kr";
            System.out.println("🌍 현재 위치 기반 날씨 요청 URL: " + weatherUrl);

            String weatherResponse = restTemplate.getForObject(weatherUrl, String.class);
            JsonNode weatherJson = objectMapper.readTree(weatherResponse);

            // ✅ 도시명 가져오기
            String cityName = weatherJson.get("name").asText();

            // ✅ 미세먼지 API 호출
            String airPollutionUrl = "https://api.openweathermap.org/data/2.5/air_pollution?lat=" + lat + "&lon=" + lon + "&appid=" + API_KEY;
            String airResponse = restTemplate.getForObject(airPollutionUrl, String.class);
            JsonNode airJson = objectMapper.readTree(airResponse);

            int airQualityIndex = airJson.get("list").get(0).get("main").get("aqi").asInt();
            double pm25 = airJson.path("list").path(0).path("components").path("pm2_5").asDouble(0.0);
            double pm10 = airJson.path("list").path(0).path("components").path("pm10").asDouble(0.0);

            // ✅ 미세먼지 등급 변환
            String airQuality = switch (airQualityIndex) {
                case 1 -> "🔵 좋음";
                case 2 -> "🟢 보통";
                case 3 -> "🟡 나쁨";
                case 4 -> "🔴 매우 나쁨";
                default -> "🟣 위험";
            };

            // ✅ 환기 추천 여부 결정
            String ventilationAdvice = (pm25 <= 15 && pm10 <= 30) ? "✅ 환기 추천" : (pm25 <= 35 && pm10 <= 50) ? "⚠️ 환기 가능 (주의)" : "❌ 환기 비추천";

            // ✅ 최종 JSON 응답
            JsonNode finalResponse = objectMapper.createObjectNode()
                    .put("locationType", locationType + ": " + cityName)
                    .put("temperature", df.format(weatherJson.get("main").get("temp").asDouble()) + "°C")
                    .put("weather", "(" + weatherJson.get("weather").get(0).get("description").asText().replace("온흐림", "흐림") + ")")
                    .put("airQuality", airQuality)
                    .put("ventilation", ventilationAdvice);

            return objectMapper.writeValueAsString(finalResponse);
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"error\": \"현재 위치의 데이터를 불러올 수 없습니다.\"}";
        }
    }
}