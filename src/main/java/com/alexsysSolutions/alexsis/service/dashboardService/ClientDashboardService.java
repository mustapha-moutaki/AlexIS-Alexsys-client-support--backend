package com.alexsysSolutions.alexsis.service.dashboardService;

import com.alexsysSolutions.alexsis.dto.response.dashboard.ClientDashboardOverViewDtoResponse;


public interface ClientDashboardService {

    ClientDashboardOverViewDtoResponse getClientOwnStats(Long clientId);

}
