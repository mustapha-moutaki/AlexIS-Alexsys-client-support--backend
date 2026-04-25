package com.alexsysSolutions.alexsis.service.dashboardService;

import com.alexsysSolutions.alexsis.model.User;

public interface AgentStatsService {
     int totalAgents();
     int totalBusyAgents();
     int totalAvailableAgents();
     int overloadAgents();
     Double averageResolutionTime();
     Double averagePerformanceRating();
     String bestAgent();
     double avgLoadPerAgent();
     User riskDetected();

}
