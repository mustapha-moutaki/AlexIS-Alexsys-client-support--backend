package com.alexsysSolutions.alexsis.service.impl.dashboardServiceImpl;

import com.alexsysSolutions.alexsis.dto.response.dashboard.ClientDashboardOverViewDtoResponse;
import com.alexsysSolutions.alexsis.reposiotry.ClientPriorityProjection;
import com.alexsysSolutions.alexsis.reposiotry.ClientRepository;
import com.alexsysSolutions.alexsis.reposiotry.ClientTicketStatusProjection;
import com.alexsysSolutions.alexsis.reposiotry.ClientTicketsNeedingAttentionProjection;
import com.alexsysSolutions.alexsis.service.dashboardService.ClientDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ClientDashboardServiceImpl implements ClientDashboardService {

    private final ClientRepository clientRepository;

    @Override
    public ClientDashboardOverViewDtoResponse getClientOwnStats(Long clientId) {

        ClientTicketStatusProjection ticketStats = clientRepository.getStatsOfTicketsStat(clientId);
        ClientPriorityProjection priorityStats = clientRepository.getMyPriorityStats(clientId);
        ClientTicketsNeedingAttentionProjection attentionStats = clientRepository.countTicketsNeedingAttention(clientId);

        return ClientDashboardOverViewDtoResponse.builder()

                // Ticket stats
                .countMyTotalTickets(safe(ticketStats != null ? ticketStats.getTotalTickets() : null))
                .countMyOpenTickets(safe(ticketStats != null ? ticketStats.getOpenTickets() : null))
                .countMyInProgressTickets(safe(ticketStats != null ? ticketStats.getInProgressTickets() : null))
                .countMyResolvedTickets(safe(ticketStats != null ? ticketStats.getResolvedTickets() : null))
                .myClosedTickets(safe(ticketStats != null ? ticketStats.getClosedTickets() : null))

                // Priority
                .myHighPriorityTickets(safe(priorityStats != null ? priorityStats.getHighPriorityTickets() : null))
                .myMediumPriorityTickets(safe(priorityStats != null ? priorityStats.getMediumPriorityTickets() : null))
                .myLowPriorityTickets(safe(priorityStats != null ? priorityStats.getLowPriorityTickets() : null))

                // Attention
                .ticketsNeedingAttention(safe(attentionStats != null ? attentionStats.getTicketsNeedingAttention() : null))

                // Profile
                .registrationDate(clientRepository.findRegistrationDateById(clientId))

                .build();
    }

    private int safe(Integer value) {
        return value != null ? value : 0;
    }
}