package com.alexsysSolutions.alexsis.dto.response.dashboard;

import com.alexsysSolutions.alexsis.model.User;

public class DashboardOverViewDto {

    // users table
    private int totalUsers;
    private int totalAgents;
    private int totalClients;
    private int totalActiveUsers;

    // agents
    private int totalAvailableAgents;
    private int totalBusyAgents;
    private int overloadAgents; // activeTicketCount > maxCapacity

    private Double avgPerformanceRating;
    private String bestAgent; // max_rating => should be dto -id+name

    private Long totalActiveTickets;
    private double avgLoadPerAgent;

    private int totalClientsToDay;
    private int avgSatisfactionScore;
    private int lowSatisfactionClient;

    // agent score = performance_rating - (activeTicketCount - maxCapacity)

    // resk detected
    private User reskDetected; // agent overloaded with low performance




}
