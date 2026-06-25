package com.alexsysSolutions.alexsis.service.impl;

import com.alexsysSolutions.alexsis.config.GroqProperties;
import com.alexsysSolutions.alexsis.security.ai.AiPrompts;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.UUID;
import org.springframework.data.redis.core.StringRedisTemplate;


@Service
public class GroqService {

    private final RestTemplate restTemplate;
    private final GroqProperties groqProperties;
    private final ObjectMapper objectMapper;


    private final StringRedisTemplate redisTemplate;



    public GroqService(
            GroqProperties groqProperties,
            StringRedisTemplate redisTemplate
    ) {
        this.groqProperties = groqProperties;
        this.redisTemplate = redisTemplate;
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }


    private static final int DAILY_LIMIT = 5;



    // ⭐ START: UPDATED METHOD (LIMIT + COOKIE)
    public String askGroq(String question,
                          HttpServletRequest request,
                          HttpServletResponse response
    ) {

        String visitorId = getOrCreateVisitorId(request, response);

        String key = "visitor:" + visitorId + ":" + LocalDate.now();

        String value = redisTemplate.opsForValue().get(key);
        int count = (value == null) ? 0 : Integer.parseInt(value);


        if (count >= DAILY_LIMIT) {
            return "You have reached your limit for today. Try again tomorrow or contact us for fast access.";
        }


        String reply = askGroqInternal(question);


        redisTemplate.opsForValue().increment(key);
        redisTemplate.expire(key, Duration.ofDays(1));

        return reply;
    }



    public String askGroqInternal(String question) {

        if (question == null || question.isBlank()) {
            return "Alexis AI is active. How can I help you today?";
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(groqProperties.getApiKey());

            List<Map<String, String>> messages = List.of(
                    Map.of("role", "system", "content", AiPrompts.ALEXIS_ASSISTANT),
                    Map.of("role", "user", "content", question)
            );

            Map<String, Object> body = new HashMap<>();
            body.put("model", groqProperties.getModel());
            body.put("messages", messages);
            body.put("temperature", 0.3);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(
                    groqProperties.getUrl(),
                    request,
                    String.class
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                return extractContentFromResponse(response.getBody());
            } else {
                return "AI Error: Received status " + response.getStatusCode();
            }

        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }



    private String getOrCreateVisitorId(HttpServletRequest request,
                                        HttpServletResponse response) {

        if (request.getCookies() != null) {
            for (Cookie c : request.getCookies()) {
                if ("visitor_id".equals(c.getName())) {
                    return c.getValue();
                }
            }
        }

        String visitorId = UUID.randomUUID().toString();

        Cookie cookie = new Cookie("visitor_id", visitorId);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60 * 24 * 30);

        response.addCookie(cookie);

        return visitorId;
    }



    private String extractContentFromResponse(String jsonResponse) {
        try {
            JsonNode root = objectMapper.readTree(jsonResponse);
            JsonNode contentNode = root.path("choices").get(0).path("message").path("content");

            if (contentNode.isMissingNode()) {
                return "AI returned an empty response.";
            }

            return contentNode.asText();
        } catch (Exception e) {
            return "Error parsing response text.";
        }
    }
}