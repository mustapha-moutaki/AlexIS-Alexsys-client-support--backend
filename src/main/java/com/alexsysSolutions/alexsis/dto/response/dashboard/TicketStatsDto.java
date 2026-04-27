package com.alexsysSolutions.alexsis.dto.response.dashboard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TicketStatsDto {

    private int totalTickets;
    private int totalActiveTickets; // tickets with status OPEN or IN_PROGRESS
    private int totalResolvedTickets;
    private int totalClosedTickets;
    private Double avgResolutionTime; // average resolution time in hours (from agents)
    private int highPriorityTickets;
    private int totalTicketsToday;
}
