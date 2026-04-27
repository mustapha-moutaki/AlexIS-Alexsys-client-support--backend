package com.alexsysSolutions.alexsis.service.impl;

import com.alexsysSolutions.alexsis.enums.Priority;
import com.alexsysSolutions.alexsis.exception.ResourceNotFoundException;
import com.alexsysSolutions.alexsis.model.Agent;
import com.alexsysSolutions.alexsis.reposiotry.AgentRepository;
import com.alexsysSolutions.alexsis.reposiotry.TicketRepository;
import com.alexsysSolutions.alexsis.service.WorkloadService;
import com.alexsysSolutions.alexsis.service.impl.assignment.factory.AssignmentStrategyFactory;
import com.alexsysSolutions.alexsis.service.strategy.AssignmentStrategy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketAutoAssignmentServiceImplTest {

    @Mock
    private TicketRepository ticketRepository;
    @Mock
    private AssignmentStrategyFactory strategyFactory;

    @Mock
    private AssignmentStrategy strategy;

    @Mock
    private WorkloadService workloadService;
    @Mock
    private AgentRepository agentRepository;

    @InjectMocks
    private TicketAutoAssignmentServiceImpl service;


    @Test
    void shouldAssignAgentSuccessfully() {
        // GIVEN
        Priority priority = Priority.HIGH;

        Agent agent = new Agent();
        agent.setId(1L);

        List<Agent> agents = List.of(agent);

        when(agentRepository.findAvailableAgents()).thenReturn(agents);
        when(strategyFactory.getStrategy(priority)).thenReturn(strategy);
        when(strategy.assign(agents)).thenReturn(agent);

        when(agentRepository.save(agent)).thenReturn(agent);

        Agent result = service.assignAgent(priority);

        assertNotNull(result);
        assertEquals(1L, result.getId());

        verify(agentRepository).findAvailableAgents();
        verify(strategyFactory).getStrategy(priority);
        verify(strategy).assign(agents);
        verify(workloadService).incrementTicketsActiveCount(agent);
        verify(agentRepository).save(agent);

    }

    @Test
    void shouldThrowExceptionWhenNoAgents() {

        when(agentRepository.findAvailableAgents()).thenReturn(List.of());

        assertThrows(ResourceNotFoundException.class,
                () -> service.assignAgent(Priority.HIGH));

        verify(agentRepository).findAvailableAgents();
        verifyNoInteractions(strategyFactory);
        verifyNoInteractions(workloadService);
    }
}