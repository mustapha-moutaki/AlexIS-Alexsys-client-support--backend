package com.alexsysSolutions.alexsis.controller;

import com.alexsysSolutions.alexsis.dto.response.ApiResponse;
import com.alexsysSolutions.alexsis.dto.response.dashboard.ClientDashboardOverViewDtoResponse;
import com.alexsysSolutions.alexsis.security.context.CurrentUserProvider;
import com.alexsysSolutions.alexsis.service.dashboardService.ClientDashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/dashboard/client/statistics")
@RequiredArgsConstructor
@Tag(name = "Client Dashboard", description = "Endpoints for client dashboard statistics")
public class ClientDashboardController {

    private final ClientDashboardService clientDashboardService;
    private final CurrentUserProvider currentUserProvider;

    private static final Logger logger = LoggerFactory.getLogger(ClientDashboardController.class);

    @Operation(
            summary = "Get client dashboard summary",
            description = "Retrieve dashboard statistics for the authenticated client"
    )
    @GetMapping("/summary")
    public ResponseEntity<ApiResponse<ClientDashboardOverViewDtoResponse>> getMyDashboard(
            HttpServletRequest http
    ) {

        Long clientId = currentUserProvider.getCurrentUser().getUser().getId();

        logger.info("GET /client/dashboard/summary - Fetching dashboard for clientId={}", clientId);

        ClientDashboardOverViewDtoResponse clientDashboard =
                clientDashboardService.getClientOwnStats(clientId);

        ApiResponse<ClientDashboardOverViewDtoResponse> response =
                ApiResponse.success("Client dashboard overview retrieved successfully", clientDashboard);

        response.setStatus(HttpStatus.OK.value());
        response.setPath(http.getRequestURI());

        logger.info("Dashboard retrieved successfully for clientId={}", clientId);

        return ResponseEntity.ok(response);
    }
}