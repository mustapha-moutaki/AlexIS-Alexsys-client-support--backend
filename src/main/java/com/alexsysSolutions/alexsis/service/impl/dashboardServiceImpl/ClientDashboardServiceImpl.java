package com.alexsysSolutions.alexsis.service.impl.dashboardServiceImpl;

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
    public ClientTicketStatusProjection getTicketStats(Long clientId) {
        return clientRepository.getStatsOfTicketsStat(clientId);
    }
    @Override
    public ClientPriorityProjection getTicketPriority(Long clientId){
        return clientRepository.getMyPriorityStats(clientId);
    }

    @Override
    public ClientTicketsNeedingAttentionProjection getTicketsNeedingAttention(Long clientId) {
        return clientRepository.countTicketsNeedingAttention(clientId);
    }
}

