package com.alexsysSolutions.alexsis.service.dashboardService;

import com.alexsysSolutions.alexsis.dto.response.dashboard.graphs.SatisfactionTrendStatsDtoResponse;

import java.util.List;

public interface ClientStatsService {
     int totalClients();
     int totalActiveClients(); // active clients (not deleted)
     int totalClientsToday(); // clients registered today
     Double avgSatisfactionScore(); // average satisfaction score across clients
     int lowSatisfactionClients();

     // for graphs
     List<SatisfactionTrendStatsDtoResponse> getSatisfactionTrendStats();
}
