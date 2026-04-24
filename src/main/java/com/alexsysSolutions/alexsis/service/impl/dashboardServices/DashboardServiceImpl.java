package com.alexsysSolutions.alexsis.service.impl.dashboardServices;

import com.alexsysSolutions.alexsis.dto.response.dashboard.DashboardOverViewDto;
import com.alexsysSolutions.alexsis.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    @Override
    public DashboardOverViewDto getOverview() {
        return null;
    }
}
