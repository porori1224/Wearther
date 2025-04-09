package com.example.weather.service;
import org.springframework.stereotype.Service;

@Service
public class OutfitService {
    public String recommendOutfit(double temperature, String weatherCondition, int dustLevel) {
        StringBuilder recommendation = new StringBuilder();

        // 기온 기준 기본 옷차림
        if (temperature >= 28) {
            recommendation.append("민소매, 반팔, 반바지, 원피스");
        } else if (temperature >= 23) {
            recommendation.append("반팔, 얇은 셔츠, 반바지, 면바지");
        } else if (temperature >= 20) {
            recommendation.append("얇은 가디건, 긴팔, 면바지, 청바지");
        } else if (temperature >= 17) {
            recommendation.append("얇은 니트, 맨투맨, 가디건, 청바지");
        } else if (temperature >= 12) {
            recommendation.append("자켓, 가디건, 야상, 스타킹, 청바지, 면바지");
        } else if (temperature >= 9) {
            recommendation.append("자켓, 트렌치코트, 야상, 니트, 청바지, 스타킹");
        } else if (temperature >= 5) {
            recommendation.append("코트, 가죽자켓, 히트텍, 니트, 레킹스");
        } else {
            recommendation.append("패딩, 두꺼운 코트, 목도리, 기모 제품");
        }


        // 날씨 상태 고려
        if (weatherCondition.toLowerCase().contains("rain") || weatherCondition.contains("비")) {
            recommendation.append(", 우산, 레인 부츠");
        } else if (weatherCondition.toLowerCase().contains("snow") || weatherCondition.contains("눈")) {
            recommendation.append(", 장갑, 우산");
        }


        // 미세먼지 상태 고려
        if (dustLevel >= 80) {
            recommendation.append(", 마스크 착용 권장");
        }

        return recommendation.toString();
    }
}
