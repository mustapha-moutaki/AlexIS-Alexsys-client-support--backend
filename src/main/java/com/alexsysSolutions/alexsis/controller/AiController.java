package com.alexsysSolutions.alexsis.controller;

import com.alexsysSolutions.alexsis.dto.request.ai.AiDtoRequest;

import com.alexsysSolutions.alexsis.service.impl.AiService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ai")
public class AiController {

    private final AiService aiService;

    public AiController(AiService aiService) {
        this.aiService = aiService;
    }

    @PostMapping("/ask")
    public ResponseEntity<?> ask(@RequestBody AiDtoRequest request) {

        String response = aiService.ask(request.getMessage());

        return ResponseEntity.ok(response);
    }
}