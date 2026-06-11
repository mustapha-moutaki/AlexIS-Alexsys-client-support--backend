package com.alexsysSolutions.alexsis.service.impl;

import com.alexsysSolutions.alexsis.config.GroqProperties;
import com.alexsysSolutions.alexsis.security.ai.AiPrompts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GrokService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final GroqProperties groqProperties;
    public GrokService(
            GroqProperties groqProperties
    ){
        this.groqProperties = groqProperties;
    }


    private String apiKey = groqProperties.getApiKey();

    private static final String GROQ_URL = "https://api.groq.com/openai/v1/chat/completions";

    private static final String MODEL = "openai/gpt-oss-120b";

    public String askGrok(String question) {

        if (question == null || question.isBlank()) {
            return "Alexis AI is active. How can I help you with the platform today?";
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        List<Map<String, String>> messages = List.of(
                Map.of(
                        "role",
                        "system",
                        "content",
                        AiPrompts.ALEXIS_ASSISTANT
                ),
                Map.of(
                        "role",
                        "user",
                        "content",
                        question
                )
        );

        Map<String, Object> body = new HashMap<>();
        body.put("model", MODEL);
        body.put("messages", messages);
        body.put("temperature", 0.3);

        HttpEntity<Map<String, Object>> request =
                new HttpEntity<>(body, headers);

        ResponseEntity<String> response =
                restTemplate.postForEntity(
                        GROQ_URL,
                        request,
                        String.class
                );

        return response.getBody();
    }
}