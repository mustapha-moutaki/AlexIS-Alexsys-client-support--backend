package com.alexsysSolutions.alexsis.service.dashboardService;

import com.alexsysSolutions.alexsis.dto.response.dashboard.graphs.ResolutionTimeTrendStatsDtoResponse;
import com.alexsysSolutions.alexsis.dto.response.dashboard.graphs.WeeklyTicketsStatsDtoResponse;

import java.util.List;

public interface TicketStatsService {

     int totalTickets();
     int totalActiveTickets(); // tickets with status OPEN or IN_PROGRESS
     int totalResolvedTickets();
     int totalClosedTickets();
     Double avgResolutionTime(); // average resolution time in hours (from agents)
     int highPriorityTickets();
     int totalTicketsToday();

     // graphs
     List<ResolutionTimeTrendStatsDtoResponse> getResolutionTimeTrendStats();
     List<WeeklyTicketsStatsDtoResponse> getWeeklyTicketsStats();
}
