package com.alexsysSolutions.alexsis.service.dashboardService;

import com.alexsysSolutions.alexsis.dto.response.dashboard.graphs.AgentLoadStatsDtoResponse;
import com.alexsysSolutions.alexsis.model.User;

import java.util.List;

public interface AgentStatsService {
     int totalAgents();
     int totalBusyAgents();
     int totalAvailableAgents();
     int overloadAgents();
     Double averageResolutionTime();
     Double averagePerformanceRating();
     String bestAgent();
     Double avgLoadPerAgent();
     User riskDetected();

     // for graghs
     List<AgentLoadStatsDtoResponse> getAgentLoadStats();
}
