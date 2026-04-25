package com.alexsysSolutions.alexsis.service.dashboardService;

import com.alexsysSolutions.alexsis.reposiotry.ClientPriorityProjection;
import com.alexsysSolutions.alexsis.reposiotry.ClientTicketStatusProjection;
import com.alexsysSolutions.alexsis.reposiotry.ClientTicketsNeedingAttentionProjection;

public interface ClientDashboardService {

    ClientTicketStatusProjection getTicketStats(Long clientId);

    ClientPriorityProjection getTicketPriority(Long clientId);

    ClientTicketsNeedingAttentionProjection getTicketsNeedingAttention(Long clientId);
}
