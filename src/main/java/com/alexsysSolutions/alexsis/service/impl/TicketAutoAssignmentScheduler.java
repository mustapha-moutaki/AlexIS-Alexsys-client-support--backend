package com.alexsysSolutions.alexsis.service.impl;

import com.alexsysSolutions.alexsis.model.Ticket;
import com.alexsysSolutions.alexsis.model.User;
import com.alexsysSolutions.alexsis.reposiotry.TicketRepository;
import com.alexsysSolutions.alexsis.service.TicketAutoAssignmentService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketAutoAssignmentScheduler {

    private final TicketRepository ticketRepository;
    private final TicketAutoAssignmentService assignmentService;
    private final Logger log = LoggerFactory.getLogger(TicketAutoAssignmentScheduler.class);

    @Scheduled(fixedDelay = 60000) // every minute
    public void assignTickets() {
//        log.info("Itellegent Assign Ticket is starting ...)
        List<Ticket> tickets = ticketRepository.findUnassignedTickets();

        LocalDateTime now = LocalDateTime.now();

        for (Ticket ticket : tickets) {

            int delayMinutes = ticket.getPriority().getDelayInMinutes();

            LocalDateTime limit = ticket.getCreatedAt().plusMinutes(delayMinutes);

            if (now.isAfter(limit)) {
                // I FORGOT TO VERIFY RE-VERIFY TICKET IS STILL UNASSIGNED
                Ticket refreshed = ticketRepository.findById(ticket.getId()).orElse(null);
                if (refreshed != null && refreshed.getAssignedTo() == null) {
                    User agent = assignmentService.assignAgent(ticket.getPriority());
                    refreshed.setAssignedTo(agent);
                    refreshed.setAssignedAt(now);
                    ticketRepository.save(refreshed);
                }
            }
        }
    }
}
