package com.alexsysSolutions.alexsis.controller;

import com.alexsysSolutions.alexsis.dto.request.agent.AgentCreateDtoRequest;
import com.alexsysSolutions.alexsis.dto.request.agent.AgentUpdateDtoRequest;
import com.alexsysSolutions.alexsis.dto.response.ApiResponse;
import com.alexsysSolutions.alexsis.dto.response.agent.AgentDtoResponse;
import com.alexsysSolutions.alexsis.service.AgentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/agents")
public class AgentController {

    private final AgentService agentService;
    private static final Logger logger = LoggerFactory.getLogger(AgentController.class);

    @PostMapping
    public ResponseEntity<ApiResponse<AgentDtoResponse>> create(
            @Valid
            @RequestBody AgentCreateDtoRequest dto,
            HttpServletRequest http
    ){
        // Log agent creation request with email
        logger.info("POST /api/v1/agents - Creating new agent with email: {}", dto.getEmail());
        try {
            AgentDtoResponse agentDtoResponse = agentService.create(dto);
            ApiResponse<AgentDtoResponse> response = ApiResponse.success("Agent created successfully", agentDtoResponse);
            response.setStatus(HttpStatus.CREATED.value());
            response.setPath(http.getRequestURI());
            logger.info("Agent created successfully with ID: {}", agentDtoResponse.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            logger.error("Error creating agent", e);
            throw e;
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<AgentDtoResponse>> update(
            @PathVariable Long id,
            @Valid
            @RequestBody AgentUpdateDtoRequest dto,
            HttpServletRequest http
    ){
        // Log agent update request with id
        logger.info("PATCH /api/v1/agents/{} - Updating agent", id);
        try {
            AgentDtoResponse agentDtoResponse = agentService.update(id, dto);
            ApiResponse<AgentDtoResponse> response = ApiResponse.success("Agent updated successfully", agentDtoResponse);
            response.setPath(http.getRequestURI());
            response.setStatus(HttpStatus.OK.value());
            logger.info("Agent with ID {} updated successfully", id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error updating agent with id: {}", id, e);
            throw e;
        }
    }


    @GetMapping
    public ResponseEntity<ApiResponse<Page<AgentDtoResponse>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest http
    ){
        logger.info("GET /api/v1/agents - Fetching all agents with page: {}, size: {}", page, size);
        try {
            Page<AgentDtoResponse> agents = agentService.getAll(page, size);
            ApiResponse<Page<AgentDtoResponse>> response = ApiResponse.success("Agents retrieved successfully", agents);
            response.setStatus(HttpStatus.OK.value());
            response.setPath(http.getRequestURI());
            logger.info("Retrieved {} total agents", agents.getTotalElements());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error fetching all agents", e);
            throw e;
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AgentDtoResponse>> getById(
            @PathVariable Long id,
            HttpServletRequest http
    ){
        logger.info("GET /api/v1/agents/{} - Fetching agent by id", id);
        try {
            AgentDtoResponse agent = agentService.getById(id);
            ApiResponse<AgentDtoResponse> response = ApiResponse.success("Agent retrieved successfully", agent);
            response.setStatus(HttpStatus.OK.value());
            response.setPath(http.getRequestURI());
            logger.info("Agent with ID {} retrieved successfully", id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error fetching agent with id: {}", id, e);
            throw e;
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable Long id,
            HttpServletRequest http
    ){

        logger.info("DELETE /api/v1/agents/{} - Deleting agent", id);
        try {
            agentService.delete(id);
            ApiResponse<Void> response = ApiResponse.success("Agent deleted successfully", null);
            response.setStatus(HttpStatus.OK.value());
            response.setPath(http.getRequestURI());
            logger.info("Agent with ID {} deleted successfully", id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error deleting agent with id: {}", id, e);
            throw e;
        }
    }
}
