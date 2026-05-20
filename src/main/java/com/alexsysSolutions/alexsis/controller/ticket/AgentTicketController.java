package com.alexsysSolutions.alexsis.controller.ticket;

import com.alexsysSolutions.alexsis.dto.request.ticket.TicketUpdateStatusDtoRequest;
import com.alexsysSolutions.alexsis.dto.response.ApiResponse;
import com.alexsysSolutions.alexsis.dto.response.ticket.TicketDetailDtoResponse;
import com.alexsysSolutions.alexsis.dto.response.ticket.TicketSummaryDtoResponse;
import com.alexsysSolutions.alexsis.enums.TicketStatus;
import com.alexsysSolutions.alexsis.security.context.CurrentUserProvider;
import com.alexsysSolutions.alexsis.service.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/agent/tickets")
@Tag(
        name = "Agent Ticket Management",
        description = "Endpoints for agent ticket management"
)
public class AgentTicketController {

    private final TicketService ticketService;

    private static final Logger logger =
            LoggerFactory.getLogger(AgentTicketController.class);

    private final CurrentUserProvider currentUserProvider;


    @GetMapping
    @Operation(
            summary = "Get all tickets assigned to current agent",
            description = "Retrieve all tickets assigned to the authenticated agent"
    )
    public ResponseEntity<ApiResponse<List<TicketSummaryDtoResponse>>> getAllTicketsForSingleAgent(
            @RequestParam (required = false) TicketStatus status,
            HttpServletRequest http
    ) {

        logger.info("----------------------------------------------------------------------");
        Long currentUserId = currentUserProvider.getUserId();

        logger.info(
                "GET /api/v1/agent/tickets - Fetching all tickets for agent with id: {} with the status {}",
                currentUserId, status
        );

        List<TicketSummaryDtoResponse> ticketDetailDtoResponseList =
                ticketService.getAllAgentTickets(currentUserId, status);

        logger.info(
                "Successfully fetched {} tickets for agent with id: {}",
                ticketDetailDtoResponseList.size(),
                currentUserId
        );
        logger.info("----------------------------------------------------------------------");


        ApiResponse<List<TicketSummaryDtoResponse>> response =
                ApiResponse.success(
                        "Fetched all agent Tickets successfully",
                        ticketDetailDtoResponseList
                );

        response.setStatus(HttpStatus.OK.value());
        response.setPath(http.getRequestURI());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get ticket by ID for agent",
            description = "Retrieve a single ticket assigned to the authenticated agent"
    )
    public ResponseEntity<ApiResponse<TicketDetailDtoResponse>>getTicketByIdForAgent(
            @PathVariable Long id,
           HttpServletRequest http
    ){
        logger.info("getting ticket by id {} that assigned to agent with id {}", id, currentUserProvider.getUserId());
        TicketDetailDtoResponse ticket = ticketService.getTicketByIdForAgent(id);

        ApiResponse<TicketDetailDtoResponse>response = ApiResponse.success("Ticket received successfully", ticket);
        response.setStatus(HttpStatus.OK.value());
        response.setPath(http.getRequestURI());
        logger.info("ticket received successfully");
        logger.warn(response.toString());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update ticket status",
            description = "Update the status of a ticket assigned to the authenticated agent"
    )
    public ResponseEntity<ApiResponse<TicketSummaryDtoResponse>>updateTicketStatus(
            @Valid
            @PathVariable Long id,
          @RequestBody TicketUpdateStatusDtoRequest dto,
          HttpServletRequest http
          ){
        logger.info("PUT /api/v1/agent/tickets/{} - Updating ticket status | agentId: {} | newStatus: {}",
                id,
                currentUserProvider.getUserId(),
                dto.getStatus()
        );
        TicketSummaryDtoResponse updatedTicketStatus = ticketService.updateTicketStatus(id, dto);
        ApiResponse<TicketSummaryDtoResponse> response = ApiResponse.success("Ticket status updated successfully", updatedTicketStatus);
        response.setPath(http.getRequestURI());
        response.setStatus(HttpStatus.OK.value());
        return ResponseEntity.ok(response);
    }
}