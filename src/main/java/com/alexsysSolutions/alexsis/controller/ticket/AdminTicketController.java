package com.alexsysSolutions.alexsis.controller.ticket;

import com.alexsysSolutions.alexsis.dto.request.ticket.TicketCreateByAdminDto;
import com.alexsysSolutions.alexsis.dto.request.ticket.TicketCreateCommand;
import com.alexsysSolutions.alexsis.dto.request.ticket.TicketUpdateByAdminDtoRequest;
import com.alexsysSolutions.alexsis.dto.response.ApiResponse;
import com.alexsysSolutions.alexsis.dto.response.ticket.TicketDetailDtoResponse;
import com.alexsysSolutions.alexsis.dto.response.ticket.TicketSummaryDtoResponse;
import com.alexsysSolutions.alexsis.mapper.TicketCommandMapper;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/tickets")
@Tag(name = "Admin Ticket Management", description = "Endpoints for admin ticket management")
@PreAuthorize("hasAnyRole('ADMIN')")
public class AdminTicketController {


    private final TicketService ticketService;
    private final TicketCommandMapper ticketCommandMapper;
    private static final Logger logger = LoggerFactory.getLogger(AdminTicketController.class);

    // Create a new ticket (admin can assign to agents and clients)
    @Operation(summary = "Create a new ticket as admin", description = "Create a ticket with full control over status, priority, assignment, etc.")
    @PostMapping
    public ResponseEntity<ApiResponse<TicketDetailDtoResponse>> create(
            @Valid
            @RequestBody TicketCreateByAdminDto dto,
            HttpServletRequest http
    ) {
        logger.info("POST /api/v1/admin/tickets - Creating ticket with title: {}", dto.getTitle());

            TicketCreateCommand command = ticketCommandMapper.fromAdminDto(dto);
            TicketDetailDtoResponse savedTicket = ticketService.create(command);
            ApiResponse<TicketDetailDtoResponse> response = ApiResponse.success("Ticket created successfully", savedTicket);
            response.setPath(http.getRequestURI());
            response.setStatus(HttpStatus.CREATED.value());
            logger.info("Ticket created successfully with ID: {}", savedTicket.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }

    // Update an existing ticket (admin can change status, priority, assignment, etc.)
    @Operation(summary = "Update a ticket as admin", description = "Update ticket details with full control")
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<TicketSummaryDtoResponse>> update(
            @PathVariable Long id,
            @Valid
            @RequestBody TicketUpdateByAdminDtoRequest dto,
            HttpServletRequest http
    ) {
        logger.info("PATCH /api/v1/admin/tickets/{} - Updating ticket", id);

            TicketCreateCommand command = ticketCommandMapper.fromAdminUpdateDto(dto, id);
            TicketSummaryDtoResponse updatedTicket = ticketService.update(command);
            ApiResponse<TicketSummaryDtoResponse> response = ApiResponse.success("Ticket updated successfully", updatedTicket);
            response.setPath(http.getRequestURI());
            response.setStatus(HttpStatus.OK.value());
            logger.info("Ticket with ID {} updated successfully", id);
            return ResponseEntity.ok(response);

    }

    // Get ticket details by ID
    @Operation(summary = "Get ticket details", description = "Retrieve full ticket details including comments and attachments")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TicketDetailDtoResponse>> getDetailsById(
            @PathVariable Long id,
            HttpServletRequest http
    ) {
        logger.info("GET /api/v1/admin/tickets/{} - Fetching ticket details", id);

            TicketDetailDtoResponse ticketDetail = ticketService.getDetailsForAdmin(id);
            ApiResponse<TicketDetailDtoResponse> response = ApiResponse.success("Ticket details retrieved successfully", ticketDetail);
            response.setPath(http.getRequestURI());
            response.setStatus(HttpStatus.OK.value());
            logger.info("Ticket with ID {} retrieved successfully", id);
            return ResponseEntity.ok(response);
    }

    // Get ticket summary by ID
    @Operation(summary = "Get ticket summary", description = "Retrieve ticket summary information")
    @GetMapping("/{id}/summary")
    public ResponseEntity<ApiResponse<TicketSummaryDtoResponse>> getSummaryById(
            @PathVariable Long id,
            HttpServletRequest http
    ) {
        logger.info("GET /api/v1/admin/tickets/{}/summary - Fetching ticket summary", id);

            TicketSummaryDtoResponse ticketSummary = ticketService.getSummaryForAdmin(id);
            ApiResponse<TicketSummaryDtoResponse> response = ApiResponse.success("Ticket summary retrieved successfully", ticketSummary);
            response.setPath(http.getRequestURI());
            response.setStatus(HttpStatus.OK.value());
            logger.info("Ticket summary with ID {} retrieved successfully", id);
            return ResponseEntity.ok(response);
    }

    // Get all tickets (detailed view with pagination)
    @Operation(summary = "Get all tickets (detailed)", description = "Retrieve paginated list of all tickets with full details")
    @GetMapping("/detailed")
    public ResponseEntity<ApiResponse<Page<TicketDetailDtoResponse>>> getAllDetailed(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest http
    ) {
        logger.info("GET /api/v1/admin/tickets/detailed - Fetching all tickets (detailed) - page: {}, size: {}", page, size);

            Page<TicketDetailDtoResponse> tickets = ticketService.getAllDetailsForAdmin(page, size);
            ApiResponse<Page<TicketDetailDtoResponse>> response = ApiResponse.success("Tickets retrieved successfully", tickets);
            response.setPath(http.getRequestURI());
            response.setStatus(HttpStatus.OK.value());
            logger.info("Retrieved {} total tickets (detailed)", tickets.getTotalElements());
            return ResponseEntity.ok(response);
    }

    // Get all tickets (summary view with pagination)
    @Operation(summary = "Get all tickets (summary)", description = "Retrieve paginated list of all tickets with summary view")
    @GetMapping
    public ResponseEntity<ApiResponse<Page<TicketSummaryDtoResponse>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest http
    ) {
        logger.info("GET /api/v1/admin/tickets - Fetching all tickets (summary) - page: {}, size: {}", page, size);

            Page<TicketSummaryDtoResponse> tickets = ticketService.getAllSummaryForAdmin(page, size);
            ApiResponse<Page<TicketSummaryDtoResponse>> response = ApiResponse.success("Tickets retrieved successfully", tickets);
            response.setPath(http.getRequestURI());
            response.setStatus(HttpStatus.OK.value());
            logger.info("Retrieved {} total tickets (summary)", tickets.getTotalElements());
            return ResponseEntity.ok(response);

    }

    // Delete a ticket
    @Operation(summary = "Delete a ticket", description = "Remove a ticket from the system")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable Long id,
            HttpServletRequest http
    ) {
        logger.info("DELETE /api/v1/admin/tickets/{} - Deleting ticket", id);

            ticketService.delete(id);
            ApiResponse<Void> response = ApiResponse.success("Ticket deleted successfully", null);
            response.setPath(http.getRequestURI());
            response.setStatus(HttpStatus.OK.value());
            logger.info("Ticket with ID {} deleted successfully", id);
            return ResponseEntity.ok(response);

    }
}
