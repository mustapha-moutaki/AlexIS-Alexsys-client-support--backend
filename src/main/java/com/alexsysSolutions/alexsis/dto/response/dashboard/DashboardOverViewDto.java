package com.alexsysSolutions.alexsis.dto.response.dashboard;

import com.alexsysSolutions.alexsis.model.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class DashboardOverViewDto {

    // client
    private int totalClients;
    private int totalClientsToDay;
    private int activeClients;
    private Double avgSatisfactionScore;
    private int lowSatisfactionClient;

    // agents
    private int totalAgents;
    private int totalAvailableAgents;
    private int totalBusyAgents;
    private int overloadAgents; // activeTicketCount > maxCapacity
    private Double averageResolutionTime;
    private Double avgPerformanceRating;
    private String bestAgent; // max_rating => should be dto -id+name
    // resk detected
    private User RiskDetectedAgent; // agent overloaded with low performance
    private double AvgLoadPerAgent;

    // ticket
    private int totalTickets;
    private int totalActiveTickets;
    private int totalResolvedTickets;
    private int totalClosedTickets;
    private Double avgResolutionTime; // average resolution time in hours (from agents)
    private int highPriorityTickets;
    private int totalTicketsToday;

    // category
    private int totalCategories;


    //  == client Dashboard stats ==

    // from clientTicketStatusProject
    private int countMyTotalTickets;
    private int countMyOpenTickets;
    private int countMyInProgressTickets;
    private int countMyResolvedTickets;
    private int myClosedTickets;
    private int myTicketsCreatedToday;

    // from ClientTicketPriorityProjection
    private int myHighPriorityTickets;
    private int myMediumPriorityTickets;
    private int myLowPriorityTickets;

    // from ClientTicketNeededProjection
    private int ticketsNeedingAttention;

}
