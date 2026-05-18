package com.alexsysSolutions.alexsis.service.impl.dashboardServiceImpl;

import com.alexsysSolutions.alexsis.dto.response.dashboard.graphs.SatisfactionTrendStatsDtoResponse;
import com.alexsysSolutions.alexsis.reposiotry.ClientRepository;
import com.alexsysSolutions.alexsis.service.dashboardService.ClientStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientStatsServiceImpl implements ClientStatsService {

    private final ClientRepository clientRepository;

    @Override
    public int totalClients() {
        return clientRepository.countTotalClients();
    }

    @Override
    public int totalActiveClients() {
        return clientRepository.countActiveClients();
    }

    @Override
    public int totalClientsToday() {
        return clientRepository.countClientsRegisteredToday();
    }

    @Override
    public Double avgSatisfactionScore() {
        return clientRepository.averageSatisfactionScore();
    }

    @Override
    public int lowSatisfactionClients() {
        return clientRepository.countLowSatisfactionClients();
    }

    @Override
    public List<SatisfactionTrendStatsDtoResponse> getSatisfactionTrendStats() {
        return clientRepository.getSatisfactionTrend().stream()
                .map(row -> new SatisfactionTrendStatsDtoResponse(
                        (String) row[0],
                        String.valueOf(row[1])
                ))
                .toList();
    }
}
