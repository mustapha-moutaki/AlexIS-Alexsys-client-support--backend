package com.alexsysSolutions.alexsis.dto.response.dashboard;


import lombok.Data;

@Data
public class ClientStatsDto {
    private int totalClients; // total number of clients
    private int totalActiveClients; // active clients (not deleted)
    private int totalClientsToday; // clients registered today
    private int avgSatisfactionScore; // average satisfaction score across clients
    private int lowSatisfactionClients; // count of clients with low satisfaction (e.g., score < threshold)
}

