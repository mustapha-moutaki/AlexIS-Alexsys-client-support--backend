package com.alexsysSolutions.alexsis.service.impl.dashboardServiceImpl;

import com.alexsysSolutions.alexsis.dto.response.dashboard.DashboardOverViewDto;
import com.alexsysSolutions.alexsis.service.CategoryService;
import com.alexsysSolutions.alexsis.service.DashboardService;
import com.alexsysSolutions.alexsis.service.dashboardService.AgentStatsService;
import com.alexsysSolutions.alexsis.service.dashboardService.CategoryStatsService;
import com.alexsysSolutions.alexsis.service.dashboardService.ClientStatsService;
import com.alexsysSolutions.alexsis.service.dashboardService.TicketStatsService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Getter
@Setter
@Service
public class DashboardServiceImpl implements DashboardService {

    private final AgentStatsService agentStatsService;
    private final ClientStatsService clientStats;
    private final TicketStatsService ticketStats;
    private final CategoryStatsService categoryStats;

    @Override
    public DashboardOverViewDto getOverview() {

        DashboardOverViewDto dto = new DashboardOverViewDto();

        //  agent
        dto.setTotalAgents(agentStatsService.totalAgents());
        dto.setTotalBusyAgents(agentStatsService.totalBusyAgents());
        dto.setTotalAvailableAgents(agentStatsService.totalAvailableAgents());
        dto.setOverloadAgents(agentStatsService.overloadAgents());
        dto.setAverageResolutionTime(agentStatsService.averageResolutionTime());
        dto.setAvgPerformanceRating(agentStatsService.averagePerformanceRating());
        dto.setBestAgent(agentStatsService.bestAgent());
        dto.setAvgLoadPerAgent(agentStatsService.avgLoadPerAgent());
        dto.setRiskDetectedAgent(agentStatsService.riskDetected());

        // client
        dto.setTotalClients(clientStats.totalClients());
        dto.setActiveClients(clientStats.totalActiveClients());
        dto.setTotalClientsToDay(clientStats.totalClientsToday());
        dto.setAvgSatisfactionScore(clientStats.avgSatisfactionScore());
        dto.setLowSatisfactionClient(clientStats.lowSatisfactionClients());

        // ticket
        dto.setTotalTickets(ticketStats.totalTickets());
        dto.setTotalActiveTickets(ticketStats.totalActiveTickets());
        dto.setTotalResolvedTickets(ticketStats.totalResolvedTickets());
        dto.setTotalClosedTickets(ticketStats.totalClosedTickets());
        dto.setAvgResolutionTime(ticketStats.avgResolutionTime());
        dto.setHighPriorityTickets(ticketStats.highPriorityTickets());
        dto.setTotalTicketsToday(ticketStats.totalTicketsToday());

        // category
        dto.setTotalCategories(categoryStats.totalCategories());
        return dto;
    }
}