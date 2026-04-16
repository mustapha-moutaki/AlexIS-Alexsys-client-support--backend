package com.alexsysSolutions.alexsis.controller;

import com.alexsysSolutions.alexsis.dto.request.agent.AgentCreateDtoRequest;
import com.alexsysSolutions.alexsis.dto.request.agent.AgentUpdateDtoRequest;
import com.alexsysSolutions.alexsis.dto.response.ApiResponse;
import com.alexsysSolutions.alexsis.dto.response.agent.AgentDtoResponse;
import com.alexsysSolutions.alexsis.model.Agent;
import com.alexsysSolutions.alexsis.service.AgentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;  // st: Added import
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Builder
@Getter
@Setter
@RequestMapping("/api/v1/agents")
public class AgentController {

    private final AgentService agentService;
    private final Logger logger = LoggerFactory.getLogger(AgentController.class);

    @PostMapping
    public ResponseEntity<ApiResponse<AgentDtoResponse>>create(
            @Valid
            @RequestBody AgentCreateDtoRequest dto,
            HttpServletRequest http
    ){
        logger.info("Creating new agent from controller");
        AgentDtoResponse agentDtoResponse = agentService.create(dto);
        ApiResponse<AgentDtoResponse>response = ApiResponse.success("Agent created successfully", agentDtoResponse);
        response.setStatus(HttpStatus.CREATED.value());
        response.setPath(http.getRequestURI());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<AgentDtoResponse>>update(
            @PathVariable Long id,
            @Valid
            @RequestBody AgentUpdateDtoRequest dto,
            HttpServletRequest http
    ){
        logger.info("st: Updating agent with id: {}", id);
        AgentDtoResponse agentDtoResponse = agentService.update(id, dto);
        ApiResponse<AgentDtoResponse> response = ApiResponse.success("Agent updated successfully", agentDtoResponse);
        response.setPath(http.getRequestURI());
        response.setStatus(HttpStatus.OK.value());
        return ResponseEntity.ok(response);
    }


    @GetMapping
    public ResponseEntity<ApiResponse<Page<AgentDtoResponse>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest http
    ){
        logger.info(" Fetching all agents - page: {}, size: {}", page, size);
        Page<AgentDtoResponse> agents = agentService.getAll(page, size);
        ApiResponse<Page<AgentDtoResponse>> response = ApiResponse.success("Agents retrieved successfully", agents);
        response.setStatus(HttpStatus.OK.value());
        response.setPath(http.getRequestURI());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AgentDtoResponse>> getById(
            @PathVariable Long id,
            HttpServletRequest http
    ){
        logger.info("st: Fetching agent with id: {}", id);
        AgentDtoResponse agent = agentService.getById(id);
        ApiResponse<AgentDtoResponse> response = ApiResponse.success("Agent retrieved successfully", agent);
        response.setStatus(HttpStatus.OK.value());
        response.setPath(http.getRequestURI());
        return ResponseEntity.ok(response);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable Long id,
            HttpServletRequest http
    ){
        logger.info("st: Deleting agent with id: {}", id);
        agentService.delete(id);
        ApiResponse<Void> response = ApiResponse.success("Agent deleted successfully", null);
        response.setStatus(HttpStatus.NO_CONTENT.value());
        response.setPath(http.getRequestURI());
        return ResponseEntity.noContent().build();
    }
}
