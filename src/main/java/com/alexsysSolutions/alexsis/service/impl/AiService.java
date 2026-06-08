package com.alexsysSolutions.alexsis.service.impl;

import com.alexsysSolutions.alexsis.client.AiProvider;
import com.alexsysSolutions.alexsis.security.ai.AiPrompts;
import org.springframework.stereotype.Service;

@Service
public class AiService {

    private final AiProvider aiProvider;

    public AiService(AiProvider aiProvider) {
        this.aiProvider = aiProvider;
    }

    public String ask(String message) {

        try {
            return aiProvider.ask(
                    AiPrompts.SYSTEM_PROMPT,
                    message
            );

        } catch (Exception e) {

            // handle OpenAI rate limit gracefully
            if (e.getMessage() != null && e.getMessage().contains("429")) {
                return "AI is busy right now. Please try again in a few seconds.";
            }

            return "AI service error.";
        }
    }
}