package com.alexsysSolutions.alexsis.service.impl;

import com.alexsysSolutions.alexsis.enums.Priority;
import com.alexsysSolutions.alexsis.exception.ResourceNotFoundException;
import com.alexsysSolutions.alexsis.model.Agent;
import com.alexsysSolutions.alexsis.model.User;
import com.alexsysSolutions.alexsis.reposiotry.AgentRepository;
import com.alexsysSolutions.alexsis.service.TicketAutoAssignmentService;
import com.alexsysSolutions.alexsis.service.impl.assignment.factory.AssignmentStrategyFactory;
import com.alexsysSolutions.alexsis.service.strategy.AssignmentStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketAutoAssignmentServiceImpl implements TicketAutoAssignmentService {
    private final AgentRepository agentRepository;
    private final AssignmentStrategyFactory strategyFactory;

    @Override
    public User assignAgent(Priority priority) {

        List<Agent> agents = agentRepository.findAvailableAgents();

        if (agents.isEmpty()) {
            throw new ResourceNotFoundException("No available agents in system");
        }

        AssignmentStrategy strategy = strategyFactory.getStrategy(priority);

        return strategy.assign(agents);
    }
}
