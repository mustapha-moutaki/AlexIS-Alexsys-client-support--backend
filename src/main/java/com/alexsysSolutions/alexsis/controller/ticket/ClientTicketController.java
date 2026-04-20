package com.alexsysSolutions.alexsis.controller.ticket;

import com.alexsysSolutions.alexsis.dto.request.ticket.TicketCreateCommand;
import com.alexsysSolutions.alexsis.dto.response.ticket.TicketDetailDtoResponse;
import com.alexsysSolutions.alexsis.mapper.TicketCommandMapper;
import com.alexsysSolutions.alexsis.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/client/tickets")
@RequiredArgsConstructor
public class ClientTicketController {

    private final TicketService ticketService;
    private final TicketCommandMapper commandMapper;


//    @PostMapping
//    public ResponseEntity<TicketDetailDtoResponse> create(
//            @Valid @RequestBody TicketCreateByClientDto dto,
//            Authentication authentication) {
//
//        Long clientId = ((UserPrincipal) authentication.getPrincipal()).getId();
//
//        TicketCreateCommand command = commandMapper.fromClientDto(dto, clientId);
//
//        return ResponseEntity.ok(ticketService.create(command));
//    }
}
