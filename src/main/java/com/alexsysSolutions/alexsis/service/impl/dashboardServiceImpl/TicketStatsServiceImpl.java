package com.alexsysSolutions.alexsis.service.impl.dashboardServiceImpl;

import com.alexsysSolutions.alexsis.reposiotry.TicketRepository;
import com.alexsysSolutions.alexsis.service.dashboardService.TicketStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TicketStatsServiceImpl implements TicketStatsService {

    private final TicketRepository ticketRepository;

    @Override
    public int totalTickets() {
        return ticketRepository.totalTickets();
    }

    @Override
    public int totalActiveTickets() {
        return ticketRepository.totalActiveTickets();
    }

    @Override
    public int totalResolvedTickets() {
        return ticketRepository.totalResolvedTickets();
    }

    @Override
    public int totalClosedTickets() {
        return ticketRepository.totalClosedTickets();
    }

    @Override
    public Double avgResolutionTime() {
        return ticketRepository.avgResolutionTime();
    }

    @Override
    public int highPriorityTickets() {
        return ticketRepository.highPriorityTickets();
    }

    @Override
    public int totalTicketsToday() {
        return ticketRepository.totalTicketsToday();
    }
}
