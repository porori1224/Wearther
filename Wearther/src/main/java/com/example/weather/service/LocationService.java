package com.example.weather.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.cdimascio.dotenv.Dotenv;

@Service
public class LocationService {
    private final RestTemplate restTemplate = new RestTemplate();
    private final String KAKAO_API_KEY;

    public LocationService() {
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
        this.KAKAO_API_KEY = dotenv.get("KAKAO_API_KEY");
    }

    public double[] getCoordinates(String cityName) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String kakaoGeoUrl = "https://dapi.kakao.com/v2/local/search/address.json?query=" + cityName;

            // ✅ Kakao API 요청 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "KakaoAK " + KAKAO_API_KEY);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<String> kakaoResponse = restTemplate.exchange(kakaoGeoUrl, HttpMethod.GET, entity, String.class);
            JsonNode kakaoJson = objectMapper.readTree(kakaoResponse.getBody());

            // ✅ 위도/경도 추출
            JsonNode firstResult = kakaoJson.path("documents").path(0);
            double lat = firstResult.path("y").asDouble();
            double lon = firstResult.path("x").asDouble();

            return new double[]{lat, lon}; // ✅ 변환된 좌표 반환
        } catch (Exception e) {
            e.printStackTrace();
            return new double[]{0, 0}; // ✅ 좌표 변환 실패 시 기본값 반환
        }
    }
}
