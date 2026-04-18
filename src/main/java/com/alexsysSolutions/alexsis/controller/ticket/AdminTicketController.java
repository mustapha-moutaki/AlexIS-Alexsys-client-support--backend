package com.alexsysSolutions.alexsis.controller.ticket;

import com.alexsysSolutions.alexsis.dto.request.ticket.TicketCreateByAdminDto;
import com.alexsysSolutions.alexsis.dto.request.ticket.TicketCreateCommand;
import com.alexsysSolutions.alexsis.dto.response.ApiResponse;
import com.alexsysSolutions.alexsis.dto.response.ticket.TicketDetailDtoResponse;
import com.alexsysSolutions.alexsis.mapper.TicketCommandMapper;
import com.alexsysSolutions.alexsis.mapper.TicketMapper;
import com.alexsysSolutions.alexsis.service.TicketService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/tickets")
public class AdminTicketController {
    private final TicketService ticketService;
    private final TicketMapper ticketMapper;
    private final TicketCommandMapper ticketCommandMapper;
    private final Logger logger = LoggerFactory.getLogger(AdminTicketController.class);
    @PostMapping
    public ResponseEntity<ApiResponse<TicketDetailDtoResponse>>create(
            @Valid
            @RequestBody TicketCreateByAdminDto dto,
            HttpServletRequest http
            ){
        logger.info("create ticket by admin {}", dto);
        TicketCreateCommand command = ticketCommandMapper.fromAdminDto(dto);
        TicketDetailDtoResponse savedTicket = ticketService.create(command);
        ApiResponse<TicketDetailDtoResponse> response = ApiResponse.success("Ticket Created successfully", savedTicket);
        response.setPath(http.getRequestURI());
        response.setStatus(HttpStatus.CREATED.value());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);


    }

}
