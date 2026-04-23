package com.alexsysSolutions.alexsis.dto.response.dashboard;

import com.alexsysSolutions.alexsis.model.User;
import lombok.Data;

@Data
public class AgentStatsDto {

    private int totalAvailableAgents;
    private int totalBusyAgents;
    private int overloadAgents; // activeTicketCount > maxCapacity
    private Double avgPerformanceRating;
    private String bestAgent; // "Agent Name (ID: 123)" based on max rating
    private double avgLoadPerAgent; // average active tickets per agent
    private User riskDetected; // agent overloaded with low performance ( based on score: performance_rating - (activeTicketCount - maxCapacity))
    private int totalAgents; // total number of agents

}

