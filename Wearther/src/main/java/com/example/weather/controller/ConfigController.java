package com.example.weather.controller;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
public class ConfigController {
    private final String API_KEY;

    public ConfigController() {
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
        this.API_KEY = dotenv.get("API_KEY");
    }

    @GetMapping("/api/config")
    public Map<String, String> getConfig() {
        return Map.of("apiKey", API_KEY);
    }
}