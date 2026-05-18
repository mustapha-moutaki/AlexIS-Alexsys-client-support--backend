package com.alexsysSolutions.alexsis.dto.response.dashboard.graphs;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class DashboardGraphsDtoResponse {

    private List<AgentLoadStatsDtoResponse> agentLoad;

    private List<ResolutionTimeTrendStatsDtoResponse> resolutionTimeTrend;

    private List<SatisfactionTrendStatsDtoResponse> satisfactionTrend;

    private TicketDistributionStatsDtoResponse ticketDistribution;

    private List<WeeklyTicketsStatsDtoResponse> weeklyTickets;
}
