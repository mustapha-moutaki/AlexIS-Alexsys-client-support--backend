package com.alexsysSolutions.alexsis.controller;

import com.alexsysSolutions.alexsis.dto.response.ApiResponse;
import com.alexsysSolutions.alexsis.dto.response.dashboard.*;
import com.alexsysSolutions.alexsis.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/dashboard/statistics")
@Tag(name = "Dashboard Statistics", description = "Endpoints for retrieving dashboard statistics")
@PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
public class DashboardStatisticsController {

    private final DashboardService dashboardService;
    private static final Logger logger = LoggerFactory.getLogger(DashboardStatisticsController.class);

    @Operation(summary = "Get dashboard overview", description = "Retrieve comprehensive dashboard statistics including agents, clients, tickets, and categories")
    @GetMapping("/overview")
    public ResponseEntity<ApiResponse<DashboardOverViewDto>> getOverview(HttpServletRequest http) {
        logger.info("GET /api/v1/dashboard/statistics/overview - Fetching dashboard overview statistics");

            DashboardOverViewDto overview = dashboardService.getOverview();
            ApiResponse<DashboardOverViewDto> response = ApiResponse.success("Dashboard overview retrieved successfully", overview);
            response.setPath(http.getRequestURI());
            response.setStatus(HttpStatus.OK.value());
            logger.info("Dashboard overview retrieved successfully");
            return ResponseEntity.ok(response);

    }

    @Operation(summary = "Get agents statistics", description = "Retrieve statistics specifically for agents")
    @GetMapping("/agents")
    public ResponseEntity<ApiResponse<AgentStatsDto>> getAgentsStatistics(HttpServletRequest http) {
        logger.info("GET /api/v1/dashboard/statistics/agents - Fetching agents statistics");


            AgentStatsDto overview = dashboardService.getAgentStats();
            ApiResponse<AgentStatsDto> response = ApiResponse.success("Agents statistics retrieved successfully", overview);
            response.setPath(http.getRequestURI());
            response.setStatus(HttpStatus.OK.value());
            logger.info("Agents statistics retrieved successfully");
            return ResponseEntity.ok(response);

    }

    @Operation(summary = "Get clients statistics", description = "Retrieve statistics specifically for clients")
    @GetMapping("/clients")
    public ResponseEntity<ApiResponse<ClientStatsDto>> getClientsStatistics(HttpServletRequest http) {
        logger.info("GET /api/v1/dashboard/statistics/clients - Fetching clients statistics");


            ClientStatsDto overview = dashboardService.getClientStats();
            ApiResponse<ClientStatsDto> response = ApiResponse.success("Clients statistics retrieved successfully", overview);
            response.setPath(http.getRequestURI());
            response.setStatus(HttpStatus.OK.value());
            logger.info("Clients statistics retrieved successfully");
            return ResponseEntity.ok(response);

    }

    @Operation(summary = "Get tickets statistics", description = "Retrieve statistics specifically for tickets")
    @GetMapping("/tickets")
    public ResponseEntity<ApiResponse<TicketStatsDto>> getTicketsStatistics(HttpServletRequest http) {
        logger.info("GET /api/v1/dashboard/statistics/tickets - Fetching tickets statistics");


            TicketStatsDto overview = dashboardService.getTicketStats();
            ApiResponse<TicketStatsDto> response = ApiResponse.success("Tickets statistics retrieved successfully", overview);
            response.setPath(http.getRequestURI());
            response.setStatus(HttpStatus.OK.value());
            logger.info("Tickets statistics retrieved successfully");
            return ResponseEntity.ok(response);

    }

    @Operation(summary = "Get categories statistics", description = "Retrieve statistics specifically for categories")
    @GetMapping("/categories")
    public ResponseEntity<ApiResponse<CategoryStatsDto>> getCategoriesStatistics(HttpServletRequest http) {
        logger.info("GET /api/v1/dashboard/statistics/categories - Fetching categories statistics");


        CategoryStatsDto overview = dashboardService.getCategoryStats();
            ApiResponse<CategoryStatsDto> response = ApiResponse.success("Categories statistics retrieved successfully", overview);
            response.setPath(http.getRequestURI());
            response.setStatus(HttpStatus.OK.value());
            logger.info("Categories statistics retrieved successfully");
            return ResponseEntity.ok(response);

    }
}