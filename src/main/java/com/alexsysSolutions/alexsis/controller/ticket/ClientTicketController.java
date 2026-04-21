package com.alexsysSolutions.alexsis.controller.ticket;

import com.alexsysSolutions.alexsis.dto.request.ticket.TicketCreateByClientDto;
import com.alexsysSolutions.alexsis.dto.request.ticket.TicketCreateCommand;
import com.alexsysSolutions.alexsis.dto.request.ticket.TicketUpdateByClientDtoRequest;
import com.alexsysSolutions.alexsis.dto.response.ApiResponse;
import com.alexsysSolutions.alexsis.dto.response.ticket.TicketDetailDtoResponse;
import com.alexsysSolutions.alexsis.dto.response.ticket.TicketSummaryDtoResponse;
import com.alexsysSolutions.alexsis.mapper.TicketCommandMapper;
import com.alexsysSolutions.alexsis.security.context.CurrentUserProvider;
import com.alexsysSolutions.alexsis.service.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/client/tickets")
@RequiredArgsConstructor
@Tag(name = "Client Ticket Management", description = "Endpoints for client ticket management")
public class ClientTicketController {

    private final TicketService ticketService;
    private final TicketCommandMapper commandMapper;
    private static final Logger logger = LoggerFactory.getLogger(ClientTicketController.class);
    private final CurrentUserProvider currentUser;
    private String format;

    // Create a new ticket as client (limited fields - category, title, description, priority, issue type)
    @Operation(summary = "Create a new ticket as client", description = "Create a support ticket with limited control (no status/assignment)")
    @PostMapping
    public ResponseEntity<ApiResponse<TicketDetailDtoResponse>> create(
            @Valid
            @RequestBody TicketCreateByClientDto dto,
            HttpServletRequest http
    ) {
        logger.info("POST /api/v1/client/tickets - Creating ticket by client with title: {}", dto.getTitle());
        try {
            // Extract client ID from authentication principal
            // Assuming your Authentication returns a principal with getId() method
            Long clientId = currentUser.getUserId();
            logger.debug("Client ID extracted: {}", clientId);

            TicketCreateCommand command = commandMapper.fromClientDto(dto, clientId);
            TicketDetailDtoResponse savedTicket = ticketService.create(command);

            ApiResponse<TicketDetailDtoResponse> response = ApiResponse.success("Ticket created successfully", savedTicket);
            response.setPath(http.getRequestURI());
            response.setStatus(HttpStatus.CREATED.value());
            logger.info("Ticket created successfully by client with ID: {} and ticket ID: {}", clientId, savedTicket.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            logger.error("Error creating ticket by client", e);
            throw e;
        }
    }

    // Update own ticket (client can only update title, description, priority, issue type - not status)
    @Operation(summary = "Update own ticket", description = "Client can update their own ticket details (limited fields)")
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<TicketSummaryDtoResponse>> update(
            @PathVariable Long id,
            @Valid
            @RequestBody TicketUpdateByClientDtoRequest dto,
            HttpServletRequest http
    ) {
        logger.info("PATCH /api/v1/client/tickets/{} - Updating ticket by client", id);
        try {
            Long clientId = currentUser.getUserId();
            logger.debug("Client ID extracted: {}", clientId);

            // The service should verify that the client owns this ticket
            TicketCreateCommand command = commandMapper.fromClientUpdateDto(dto, id, clientId);
            TicketSummaryDtoResponse updatedTicket = ticketService.update(command);

            ApiResponse<TicketSummaryDtoResponse> response = ApiResponse.success("Ticket updated successfully", updatedTicket);
            response.setPath(http.getRequestURI());
            response.setStatus(HttpStatus.OK.value());
            logger.info("Ticket with ID {} updated successfully by client {}", id, clientId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error updating ticket with ID: {}", id, e);
            throw e;
        }
    }

    // Get own ticket details
    @Operation(summary = "Get ticket details", description = "Retrieve full details of a ticket owned by the client")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TicketDetailDtoResponse>> getDetailsById(
            @PathVariable Long id,
            HttpServletRequest http
    ) {
        logger.info("GET /api/v1/client/tickets/{} - Fetching ticket details by client", id);
        try {
            Long clientId = currentUser.getUserId();
            logger.debug("Client ID extracted: {}", clientId);

            TicketDetailDtoResponse ticketDetail = ticketService.getDetailsById(id);

            // Verify that the client owns this ticket
            // This should be handled in the service layer with authorization
            ApiResponse<TicketDetailDtoResponse> response = ApiResponse.success("Ticket details retrieved successfully", ticketDetail);
            response.setPath(http.getRequestURI());
            response.setStatus(HttpStatus.OK.value());
            logger.info("Ticket with ID {} retrieved successfully by client {}", id, clientId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error fetching ticket details with ID: {}", id, e);
            throw e;
        }
    }

    // Get all client's own tickets (detailed view)
    @Operation(summary = "Get my tickets (detailed)", description = "Retrieve all tickets created by the client with full details")
    @GetMapping("/my-tickets/detailed")
    public ResponseEntity<ApiResponse<Page<TicketDetailDtoResponse>>> getMyTicketsDetailed(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication,
            HttpServletRequest http
    ) {
        logger.info(format, page, size);
        try {
            Long clientId = currentUser.getUserId();
            logger.debug("Client ID extracted: {}", clientId);

            // This should be implemented in the service to filter by clientId
            Page<TicketDetailDtoResponse> tickets = ticketService.getAllTicketDetailed(page, size);

            ApiResponse<Page<TicketDetailDtoResponse>> response = ApiResponse.success("Tickets retrieved successfully", tickets);
            response.setPath(http.getRequestURI());
            response.setStatus(HttpStatus.OK.value());
            logger.info("Retrieved {} tickets (detailed) for client {}", tickets.getTotalElements(), clientId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error fetching client's tickets (detailed)", e);
            throw e;
        }
    }

    // Get all client's own tickets (summary view)
    @Operation(summary = "Get my tickets (summary)", description = "Retrieve all tickets created by the client with summary view")
    @GetMapping
    public ResponseEntity<ApiResponse<Page<TicketSummaryDtoResponse>>> getMyTickets(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication,
            HttpServletRequest http
    ) {
        logger.info("GET /api/v1/client/tickets - Fetching client's tickets (summary) - page: {}, size: {}", page, size);
        try {
            Long clientId = currentUser.getUserId();
            logger.debug("Client ID extracted: {}", clientId);

            // This should be implemented in the service to filter by clientId
            Page<TicketSummaryDtoResponse> tickets = ticketService.getAllTicketsSummary(page, size);

            ApiResponse<Page<TicketSummaryDtoResponse>> response = ApiResponse.success("Tickets retrieved successfully", tickets);
            response.setPath(http.getRequestURI());
            response.setStatus(HttpStatus.OK.value());
            logger.info("Retrieved {} tickets (summary) for client {}", tickets.getTotalElements(), clientId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error fetching client's tickets (summary)", e);
            throw e;
        }
    }

}
