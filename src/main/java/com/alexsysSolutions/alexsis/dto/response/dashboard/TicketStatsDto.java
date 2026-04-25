package com.alexsysSolutions.alexsis.dto.response.dashboard;

import lombok.Data;

@Data
public class TicketStatsDto {

    private Long totalTickets;
    private Long totalActiveTickets; // tickets with status OPEN or IN_PROGRESS
    private Long totalResolvedTickets;
    private Long totalClosedTickets;
    private Double avgResolutionTime; // average resolution time in hours (from agents)
    private int highPriorityTickets;
    private int totalTicketsToday;
}
