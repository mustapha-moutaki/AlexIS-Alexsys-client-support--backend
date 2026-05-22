package com.alexsysSolutions.alexsis.controller;


import com.alexsysSolutions.alexsis.dto.response.ApiResponse;
import com.alexsysSolutions.alexsis.dto.response.dashboard.AgentDashboardOverviewDtoResponse;
import com.alexsysSolutions.alexsis.service.dashboardService.IAgentDashboardService;
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

import java.nio.file.AccessDeniedException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/dashboard/agent/stats/summary")
@Tag(
        name = "agent dashboard statistics",
        description = "display the authenticated agent his stats"
)
@PreAuthorize("hasRole('AGENT')")
public class AgentDashboardController {

    private final IAgentDashboardService iAgentDashboardService;
    private final Logger logger = LoggerFactory.getLogger(AgentDashboardController.class);

    @GetMapping
    @Operation(summary = "get all stats for the agent like his total tickets account, avgResolution time, and status")
    public ResponseEntity<ApiResponse<AgentDashboardOverviewDtoResponse>> getAgentDashboardOverviewStats(
            HttpServletRequest http
    ) throws AccessDeniedException {
        AgentDashboardOverviewDtoResponse stats = iAgentDashboardService.getAgentDashboardOverview();
        logger.info("getting all agent stats for dashboard agentStats: {}", stats);
        ApiResponse<AgentDashboardOverviewDtoResponse>response = ApiResponse.success("Agent dashboard stats fetched successfully", stats);
        response.setStatus(HttpStatus.OK.value());
        response.setPath(http.getRequestURI());
        return ResponseEntity.ok(response);
    }

}
