package com.alexsysSolutions.alexsis.service.impl.dashboardServiceImpl;

import com.alexsysSolutions.alexsis.dto.response.dashboard.graphs.*;
import com.alexsysSolutions.alexsis.reposiotry.AgentRepository;
import com.alexsysSolutions.alexsis.reposiotry.ClientRepository;
import com.alexsysSolutions.alexsis.reposiotry.TicketRepository;
import com.alexsysSolutions.alexsis.service.DashboardGraphsService;
import com.alexsysSolutions.alexsis.service.dashboardService.AgentStatsService;
import com.alexsysSolutions.alexsis.service.dashboardService.CategoryStatsService;
import com.alexsysSolutions.alexsis.service.dashboardService.ClientStatsService;
import com.alexsysSolutions.alexsis.service.dashboardService.TicketStatsService;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
@Builder
@Getter
@Setter
public class DashboardGraphsServiceImpl implements DashboardGraphsService{

    private final AgentStatsService agentStatsService;
    private final TicketStatsService ticketStatsService;
    private final ClientStatsService clientStatsService;

    @Override
    public DashboardGraphsDtoResponse getDashboardGraphsData() {

         return DashboardGraphsDtoResponse.builder()
                .agentLoad(agentStatsService.getAgentLoadStats())
                .resolutionTimeTrend(ticketStatsService.getResolutionTimeTrendStats())
                .satisfactionTrend(clientStatsService.getSatisfactionTrendStats())
                .weeklyTickets(ticketStatsService.getWeeklyTicketsStats())
                .build();
    }
}
