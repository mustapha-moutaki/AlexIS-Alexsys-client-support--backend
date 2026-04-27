package com.alexsysSolutions.alexsis.service.impl;

import com.alexsysSolutions.alexsis.enums.Priority;
import com.alexsysSolutions.alexsis.model.Agent;
import com.alexsysSolutions.alexsis.model.Ticket;
import com.alexsysSolutions.alexsis.model.User;
import com.alexsysSolutions.alexsis.reposiotry.TicketRepository;
import com.alexsysSolutions.alexsis.service.TicketAutoAssignmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketAutoAssignmentSchedulerTest {

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private TicketAutoAssignmentService assignmentService;

    @Mock
    private Logger log;

    @InjectMocks
    private TicketAutoAssignmentScheduler scheduler;

    @Captor
    private ArgumentCaptor<Ticket> ticketCaptor;

    @Captor
    private ArgumentCaptor<LocalDateTime> dateTimeCaptor;

    private Ticket unassignedTicket;
    private Ticket assignedTicket;
    private Agent agent;
    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        now = LocalDateTime.now();

        // Create test agent
        agent = Agent.builder()
                .id(1L)
                .firstName("John")
                .lastName("Agent")
                .email("john.agent@test.com")
                .build();

        // Create unassigned ticket
        unassignedTicket = Ticket.builder()
                .id(1L)
                .title("Test Ticket")
                .priority(Priority.HIGH)
                .assignedTo(null)
                .createdAt(now.minusMinutes(65)) // Created 65 minutes ago (past delay limit)
                .build();

        // Create assigned ticket
        assignedTicket = Ticket.builder()
                .id(2L)
                .title("Assigned Ticket")
                .priority(Priority.MEDIUM)
                .assignedTo(agent)
                .createdAt(now.minusMinutes(30))
                .build();
    }

    @Test
    void assignTickets_shouldAssignTicketsPastDelayLimit() {
        // ARRANGE
        List<Ticket> unassignedTickets = Arrays.asList(unassignedTicket);
        when(ticketRepository.findUnassignedTickets()).thenReturn(unassignedTickets);
        when(ticketRepository.findById(unassignedTicket.getId())).thenReturn(Optional.of(unassignedTicket));
        when(assignmentService.assignAgent(Priority.HIGH)).thenReturn(agent);

        // ACT
        scheduler.assignTickets();

        // ASSERT
        verify(ticketRepository).findUnassignedTickets();
        verify(ticketRepository).findById(unassignedTicket.getId());
        verify(assignmentService).assignAgent(Priority.HIGH);
        verify(ticketRepository).save(ticketCaptor.capture());

        // Verify the captured ticket was properly assigned
        Ticket savedTicket = ticketCaptor.getValue();
        assertEquals(agent, savedTicket.getAssignedTo());
        assertNotNull(savedTicket.getAssignedAt());
        assertTrue(savedTicket.getAssignedAt().isAfter(now.minusSeconds(10))); // Assigned recently
    }

    @Test
    void assignTickets_shouldNotAssignTicketsWithinDelayLimit() {
        // ARRANGE
        Ticket recentTicket = Ticket.builder()
                .id(3L)
                .title("Recent Ticket")
                .priority(Priority.CRITICAL) // 7 min delay
                .assignedTo(null)
                .createdAt(now.minusMinutes(5)) // Only 5 minutes ago (within limit)
                .build();

        List<Ticket> unassignedTickets = Arrays.asList(recentTicket);
        when(ticketRepository.findUnassignedTickets()).thenReturn(unassignedTickets);

        // ACT
        scheduler.assignTickets();

        // ASSERT
        verify(ticketRepository).findUnassignedTickets();
        verify(ticketRepository, never()).findById(any());
        verify(assignmentService, never()).assignAgent(any());
        verify(ticketRepository, never()).save(any());
    }

    @Test
    void assignTickets_shouldSkipAlreadyAssignedTickets() {
        // ARRANGE
        List<Ticket> tickets = Arrays.asList(unassignedTicket, assignedTicket);
        when(ticketRepository.findUnassignedTickets()).thenReturn(tickets);
        when(ticketRepository.findById(unassignedTicket.getId())).thenReturn(Optional.of(unassignedTicket));
        when(assignmentService.assignAgent(Priority.HIGH)).thenReturn(agent);

        // ACT
        scheduler.assignTickets();

        // ASSERT
        verify(ticketRepository).findUnassignedTickets();
        verify(ticketRepository).findById(unassignedTicket.getId());
        verify(ticketRepository, never()).findById(assignedTicket.getId()); // Already assigned
        verify(assignmentService).assignAgent(Priority.HIGH);
        verify(ticketRepository).save(ticketCaptor.capture());

        // Verify only the unassigned ticket was saved
        Ticket savedTicket = ticketCaptor.getValue();
        assertEquals(unassignedTicket.getId(), savedTicket.getId());
        assertEquals(agent, savedTicket.getAssignedTo());
    }

    @Test
    void assignTickets_shouldHandleTicketAssignedDuringProcessing() {
        // ARRANGE - Simulate ticket being assigned by another process
        Ticket concurrentlyAssigned = Ticket.builder()
                .id(4L)
                .title("Concurrent Ticket")
                .priority(Priority.LOW)
                .assignedTo(null) // Initially unassigned
                .createdAt(now.minusMinutes(1500)) // Past delay (1440 min = 1 day)
                .build();

        List<Ticket> unassignedTickets = Arrays.asList(concurrentlyAssigned);
        when(ticketRepository.findUnassignedTickets()).thenReturn(unassignedTickets);

        // When we re-check, ticket is now assigned
        Ticket assignedDuringProcess = Ticket.builder()
                .id(4L)
                .title("Concurrent Ticket")
                .priority(Priority.LOW)
                .assignedTo(agent) // Now assigned
                .createdAt(now.minusMinutes(1500))
                .build();

        when(ticketRepository.findById(concurrentlyAssigned.getId())).thenReturn(Optional.of(assignedDuringProcess));

        // ACT
        scheduler.assignTickets();

        // ASSERT
        verify(ticketRepository).findUnassignedTickets();
        verify(ticketRepository).findById(concurrentlyAssigned.getId());
        verify(assignmentService, never()).assignAgent(any()); // Should not assign
        verify(ticketRepository, never()).save(any()); // Should not save
    }

    @Test
    void assignTickets_shouldHandleTicketNotFoundDuringReCheck() {
        // ARRANGE
        List<Ticket> unassignedTickets = Arrays.asList(unassignedTicket);
        when(ticketRepository.findUnassignedTickets()).thenReturn(unassignedTickets);
        when(ticketRepository.findById(unassignedTicket.getId())).thenReturn(Optional.empty()); // Ticket deleted

        // ACT
        scheduler.assignTickets();

        // ASSERT
        verify(ticketRepository).findUnassignedTickets();
        verify(ticketRepository).findById(unassignedTicket.getId());
        verify(assignmentService, never()).assignAgent(any());
        verify(ticketRepository, never()).save(any());
    }

    @Test
    void assignTickets_shouldHandleEmptyUnassignedTicketsList() {
        // ARRANGE
        when(ticketRepository.findUnassignedTickets()).thenReturn(Arrays.asList());

        // ACT
        scheduler.assignTickets();

        // ASSERT
        verify(ticketRepository).findUnassignedTickets();
        verify(ticketRepository, never()).findById(any());
        verify(assignmentService, never()).assignAgent(any());
        verify(ticketRepository, never()).save(any());
    }

    @Test
    void assignTickets_shouldHandleMultipleTicketsWithDifferentPriorities() {
        // ARRANGE
        Ticket criticalTicket = Ticket.builder()
                .id(5L)
                .title("Critical Ticket")
                .priority(Priority.CRITICAL) // 7 min delay
                .assignedTo(null)
                .createdAt(now.minusMinutes(10)) // Past delay
                .build();

        Ticket mediumTicket = Ticket.builder()
                .id(6L)
                .title("Medium Ticket")
                .priority(Priority.MEDIUM) // 240 min delay
                .assignedTo(null)
                .createdAt(now.minusMinutes(250)) // Past delay
                .build();

        List<Ticket> unassignedTickets = Arrays.asList(criticalTicket, mediumTicket);
        when(ticketRepository.findUnassignedTickets()).thenReturn(unassignedTickets);
        when(ticketRepository.findById(criticalTicket.getId())).thenReturn(Optional.of(criticalTicket));
        when(ticketRepository.findById(mediumTicket.getId())).thenReturn(Optional.of(mediumTicket));
        when(assignmentService.assignAgent(Priority.CRITICAL)).thenReturn(agent);
        when(assignmentService.assignAgent(Priority.MEDIUM)).thenReturn(agent);

        // ACT
        scheduler.assignTickets();

        // ASSERT
        verify(ticketRepository).findUnassignedTickets();
        verify(ticketRepository).findById(criticalTicket.getId());
        verify(ticketRepository).findById(mediumTicket.getId());
        verify(assignmentService).assignAgent(Priority.CRITICAL);
        verify(assignmentService).assignAgent(Priority.MEDIUM);
        verify(ticketRepository, times(2)).save(ticketCaptor.capture()); // Should save both tickets

        // Verify both tickets were captured and assigned
        List<Ticket> savedTickets = ticketCaptor.getAllValues();
        assertEquals(2, savedTickets.size());

        // Check that both tickets have agents assigned
        assertTrue(savedTickets.stream().allMatch(ticket -> ticket.getAssignedTo() != null));
        assertTrue(savedTickets.stream().allMatch(ticket -> ticket.getAssignedAt() != null));
    }
}