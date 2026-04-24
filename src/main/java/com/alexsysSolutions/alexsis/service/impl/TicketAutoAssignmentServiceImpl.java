package com.alexsysSolutions.alexsis.service.impl;

import com.alexsysSolutions.alexsis.enums.Priority;
import com.alexsysSolutions.alexsis.exception.ResourceNotFoundException;
import com.alexsysSolutions.alexsis.model.Agent;
import com.alexsysSolutions.alexsis.model.User;
import com.alexsysSolutions.alexsis.reposiotry.AgentRepository;
import com.alexsysSolutions.alexsis.service.TicketAutoAssignmentService;
import com.alexsysSolutions.alexsis.service.WorkloadService;
import com.alexsysSolutions.alexsis.service.impl.assignment.factory.AssignmentStrategyFactory;
import com.alexsysSolutions.alexsis.service.strategy.AssignmentStrategy;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TicketAutoAssignmentServiceImpl implements TicketAutoAssignmentService {
    private final AgentRepository agentRepository;
    private final AssignmentStrategyFactory strategyFactory;
    private final WorkloadService workloadService;

    @Override
    public Agent assignAgent(Priority priority) {

        List<Agent> agents = agentRepository.findAvailableAgents();

        if (agents.isEmpty()) {
            throw new ResourceNotFoundException("No available agents in system");
        }

        AssignmentStrategy strategy = strategyFactory.getStrategy(priority);

        Agent agent = strategy.assign(agents);

        workloadService.incrementTicketsActiveCount(agent);

        agentRepository.save(agent);

        return agent;
    }
}
