package com.alexsysSolutions.alexsis.service.dashboardService;

public interface TicketStatsService {

     int totalTickets();
     int totalActiveTickets(); // tickets with status OPEN or IN_PROGRESS
     int totalResolvedTickets();
     int totalClosedTickets();
     Double avgResolutionTime(); // average resolution time in hours (from agents)
     int highPriorityTickets();
     int totalTicketsToday();

}
