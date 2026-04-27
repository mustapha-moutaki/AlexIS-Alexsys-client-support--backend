package com.alexsysSolutions.alexsis.service;

import com.alexsysSolutions.alexsis.dto.response.dashboard.*;


public interface DashboardService {
    DashboardOverViewDto getOverview();
    CategoryStatsDto getCategoryStats();

    ClientStatsDto getClientStats();
    AgentStatsDto getAgentStats();
    TicketStatsDto getTicketStats();
}
