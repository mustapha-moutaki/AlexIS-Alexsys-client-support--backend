package com.alexsysSolutions.alexsis.controller;


import com.alexsysSolutions.alexsis.dto.response.ApiResponse;
import com.alexsysSolutions.alexsis.dto.response.dashboard.graphs.DashboardGraphsDtoResponse;
import com.alexsysSolutions.alexsis.service.DashboardGraphsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/dashboard/graphs/stats")
@RequiredArgsConstructor
@Tag(name = "Dashboard Graphs Statistics", description = "Endpoints for retrieving dashboard Graphs statistics")
public class DashboardGraphsStatsController {
    private final DashboardGraphsService dashboardGraphsService;
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(DashboardGraphsStatsController.class);

    @GetMapping
    @Operation(summary = "Get dashboard overview", description = "Retrieve comprehensive dashboard statistics for Graphs")
    public ResponseEntity<ApiResponse<DashboardGraphsDtoResponse>>getAllGraphsStats(
            HttpServletRequest httpServletRequest
    ){
        logger.info("GET /api/v1/dashboard/graphs/stats - Fetching dashboard overview statistics");
        DashboardGraphsDtoResponse graphsStats = dashboardGraphsService.getDashboardGraphsData();
        ApiResponse<DashboardGraphsDtoResponse> response = ApiResponse.success("Dashboard graphs statistics retrieved successfully", graphsStats);
        response.setPath(httpServletRequest.getRequestURI());
        response.setStatus(HttpStatus.OK.value());
        return ResponseEntity.ok(response);
    }
}
