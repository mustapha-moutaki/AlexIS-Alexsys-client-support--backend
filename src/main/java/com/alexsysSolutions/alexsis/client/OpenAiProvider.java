package com.alexsysSolutions.alexsis.client;

import com.alexsysSolutions.alexsis.config.OpenAiProperties;
import com.alexsysSolutions.alexsis.dto.request.ai.GeminiRequest;
import com.alexsysSolutions.alexsis.dto.request.ai.OpenAiRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
public class OpenAiProvider implements AiProvider {

    private final WebClient webClient;
    private final OpenAiProperties properties;
    private final ObjectMapper objectMapper;

    private final Logger logger = LoggerFactory.getLogger(OpenAiProvider.class);

    public OpenAiProvider(WebClient webClient,
                          OpenAiProperties properties,
                          ObjectMapper objectMapper) {
        this.webClient = webClient;
        this.properties = properties;
        this.objectMapper = objectMapper;
    }

    @Override
    public String ask(String systemPrompt, String userMessage) {

        try {
            logger.info("==== AI REQUEST START ====");
            logger.info("URL: {}", properties.getUrl());
            logger.info("API KEY (masked): {}", properties.getKey().substring(0, 8) + "****");
            logger.info("User message: {}", userMessage);

            String fullPrompt = systemPrompt + "\n\nUser: " + userMessage;

            GeminiRequest request = new GeminiRequest(fullPrompt);
            String url = "https://generativelanguage.googleapis.com/v1/models/gemini-1.5-flash:generateContent";

            String raw = webClient.post()
                    .uri(properties.getUrl() + "?key=" + properties.getKey())
                    .header("Content-Type", "application/json")
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            logger.info("RAW RESPONSE: {}", raw);

            if (raw == null) {
                return "AI returned null response";
            }

            return raw;

        } catch (WebClientResponseException e) {

            logger.error("HTTP STATUS: {}", e.getStatusCode());
            logger.error("RESPONSE BODY: {}", e.getResponseBodyAsString());
            logger.error("HEADERS: {}", e.getHeaders());

            return "AI HTTP error: " + e.getStatusCode();

        } catch (Exception e) {

            logger.error("UNKNOWN ERROR: ", e);

            return "AI service failed";
        }
    }
}