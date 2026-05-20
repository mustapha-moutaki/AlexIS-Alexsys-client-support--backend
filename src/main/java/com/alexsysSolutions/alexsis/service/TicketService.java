package com.alexsysSolutions.alexsis.service;

import com.alexsysSolutions.alexsis.dto.request.ticket.*;
import com.alexsysSolutions.alexsis.dto.response.ticket.TicketDetailDtoResponse;
import com.alexsysSolutions.alexsis.dto.response.ticket.TicketSummaryDtoResponse;
import com.alexsysSolutions.alexsis.enums.Priority;
import com.alexsysSolutions.alexsis.enums.TicketStatus;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TicketService {

    TicketDetailDtoResponse create(TicketCreateCommand command);

    TicketSummaryDtoResponse update(TicketCreateCommand command);

    TicketDetailDtoResponse getDetailsForAdmin(Long id);
    TicketDetailDtoResponse getDetailsForClient(Long id);

    TicketSummaryDtoResponse getSummaryForAdmin(Long id);
    TicketSummaryDtoResponse getSummaryForClient(Long id);

    Page<TicketSummaryDtoResponse> getAllSummaryForAdmin(int page, int size);
    Page<TicketSummaryDtoResponse> getAllSummaryForClient(int page, int size);

    Page<TicketDetailDtoResponse> getAllDetailsForAdmin(int page, int size);

    void delete(Long id);

    // admin change ticket status and agent and priority
     TicketSummaryDtoResponse updateTicketStatus(Long ticketId, TicketUpdateStatusDtoRequest dto);
     TicketSummaryDtoResponse reAssignedTicket(Long ticketId, Long agentId);
     TicketSummaryDtoResponse updateTicketPriority(Long ticketId, Priority priority);

     // for agent
     List<TicketSummaryDtoResponse> getAllAgentTickets(Long id, TicketStatus status);

    /**

     * @return ticket by the id but assigned to agent id
     */
     TicketDetailDtoResponse getTicketByIdForAgent(Long ticketId);
}
