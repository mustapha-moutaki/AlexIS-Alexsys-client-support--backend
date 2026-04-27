package com.alexsysSolutions.alexsis.service.dashboardService;

public interface ClientStatsService {
     int totalClients();
     int totalActiveClients(); // active clients (not deleted)
     int totalClientsToday(); // clients registered today
     Double avgSatisfactionScore(); // average satisfaction score across clients
     int lowSatisfactionClients();

}
