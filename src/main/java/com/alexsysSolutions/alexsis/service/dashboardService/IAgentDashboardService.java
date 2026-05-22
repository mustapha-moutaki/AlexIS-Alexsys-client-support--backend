package com.alexsysSolutions.alexsis.service.dashboardService;

import com.alexsysSolutions.alexsis.dto.response.dashboard.AgentDashboardOverviewDtoResponse;

import java.nio.file.AccessDeniedException;

public interface IAgentDashboardService {
    AgentDashboardOverviewDtoResponse getAgentDashboardOverview() throws AccessDeniedException;
}
