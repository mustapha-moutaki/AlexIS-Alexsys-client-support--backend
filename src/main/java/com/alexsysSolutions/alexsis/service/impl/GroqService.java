package com.alexsysSolutions.alexsis.service.impl;

import com.alexsysSolutions.alexsis.config.GroqProperties;
import com.alexsysSolutions.alexsis.security.ai.AiPrompts;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class GroqService {

    private final RestTemplate restTemplate;
    private final GroqProperties groqProperties;
    private final ObjectMapper objectMapper;

    public GroqService(GroqProperties groqProperties) {
        this.groqProperties = groqProperties;
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    public String askGroq(String question) {
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

    private String extractContentFromResponse(String jsonResponse) {
        try {
            JsonNode root = objectMapper.readTree(jsonResponse);
            // This navigates the OpenAI/Groq JSON structure: choices[0].message.content
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