package com.testapp.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class HealthController {
    
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("application", "Vulnerable API Test Application");
        response.put("version", "1.0.0");
        response.put("warning", "This application contains intentional security vulnerabilities for testing purposes only!");
        return ResponseEntity.ok(response);
    }
    
    @GetMapping
    public ResponseEntity<String> welcome() {
        return ResponseEntity.ok("Welcome to the Vulnerable API Test Application. Visit /api/health for status.");
    }
}
