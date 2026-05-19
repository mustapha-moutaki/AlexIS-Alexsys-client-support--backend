package com.alexsysSolutions.alexsis.controller.ticket;

import com.alexsysSolutions.alexsis.dto.response.ApiResponse;
import com.alexsysSolutions.alexsis.dto.response.ticket.TicketDetailDtoResponse;
import com.alexsysSolutions.alexsis.dto.response.ticket.TicketSummaryDtoResponse;
import com.alexsysSolutions.alexsis.enums.TicketStatus;
import com.alexsysSolutions.alexsis.security.context.CurrentUserProvider;
import com.alexsysSolutions.alexsis.service.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}