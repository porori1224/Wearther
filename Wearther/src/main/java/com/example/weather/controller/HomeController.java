package com.example.weather.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/") // ✅ 기본 페이지 매핑
    public String home(Model model) {
        model.addAttribute("message", "Hello from Spring Boot!");
        return "wearther"; // templates/weather.html을 렌더링
    }
}