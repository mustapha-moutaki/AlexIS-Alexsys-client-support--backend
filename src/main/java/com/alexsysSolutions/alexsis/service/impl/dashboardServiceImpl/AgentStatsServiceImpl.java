package com.alexsysSolutions.alexsis.service.impl.dashboardServiceImpl;

import com.alexsysSolutions.alexsis.model.User;
import com.alexsysSolutions.alexsis.reposiotry.AgentRepository;
import com.alexsysSolutions.alexsis.service.dashboardService.AgentStatsService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class AgentStatsServiceImpl implements AgentStatsService {
    private final AgentRepository agentRepository;

    @Override
    public int totalAgents() {
        return agentRepository.countTotalAgents();
    }

    @Override
    public int totalBusyAgents() {
        return agentRepository.countBusyAgent();
    }

    @Override
    public int totalAvailableAgents() {
        return agentRepository.countAvailableAgents();
    }

    @Override
    public int overloadAgents() {
        return agentRepository.overloadAgents();
    }

    @Override
    public Double averageResolutionTime() {
        return agentRepository.averageResolutionTime();
    }

    @Override
    public Double averagePerformanceRating() {
        return agentRepository.averagePerformanceRating();
    }

    @Override
    public String bestAgent() {
        return agentRepository.findBestAgent();
    }

    @Override
    public double avgLoadPerAgent() {
        return agentRepository.avgLoadPerAgent();
    }

    @Override
    public User riskDetected() {
        return agentRepository.findAll().stream()
                .filter(agent -> agent.getActiveTicketsCount() > agent.getMaxCapacity())
                .findFirst()
                .orElse(null);
    }
}
