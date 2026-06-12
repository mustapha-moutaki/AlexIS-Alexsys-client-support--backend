package com.alexsysSolutions.alexsis.controller;

import com.alexsysSolutions.alexsis.service.impl.GroqService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;


import java.util.Map;

@RestController
@RequestMapping("/api/v1/ai")
@Tag(name = "Ai assistant")
public class AiController {

    private final GroqService groqService;

    public AiController(GroqService groqService){
        this.groqService = groqService;
    }

    @PostMapping("/chat")
    @Operation(summary = "Chat with AI", description = "get help with alex intelligent support")
    public Map<String, String> chat(@RequestBody Map<String, String> request) {

        // Extract "question" from the incoming JSON
        String userQuestion = request.get("question");

        // Get only the text response from the service
        String aiResponse = groqService.askGroq(userQuestion);

        // Return as a clean JSON object: {"reply": "..."}
        return Map.of("reply", aiResponse);
    }
}