package com.alexsysSolutions.alexsis.dto.response.dashboard;


import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder
@Getter
@Setter
public class ClientStatsDto {
    private int totalClients; // total number of clients
    private int totalActiveClients; // active clients (not deleted)
    private int totalClientsToday; // clients registered today
    private Double avgSatisfactionScore; // average satisfaction score across clients
    private int lowSatisfactionClients; // count of clients with low satisfaction (e.g., score < threshold)
}

