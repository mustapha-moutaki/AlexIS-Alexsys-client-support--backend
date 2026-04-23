package com.alexsysSolutions.alexsis.mapper;

import com.alexsysSolutions.alexsis.dto.request.ticket.TicketCreateByAdminDto;
import com.alexsysSolutions.alexsis.dto.request.ticket.TicketCreateByClientDto;
import com.alexsysSolutions.alexsis.dto.request.ticket.TicketCreateCommand;
import com.alexsysSolutions.alexsis.dto.request.ticket.TicketUpdateByAdminDtoRequest;
import com.alexsysSolutions.alexsis.dto.request.ticket.TicketUpdateByClientDtoRequest;
import org.springframework.stereotype.Component;

@Component
public class TicketCommandMapper {

    //  Map admin DTO to create command - admin has full control
    public TicketCreateCommand fromAdminDto(TicketCreateByAdminDto dto) {
        return TicketCreateCommand.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .priority(dto.getPriority())
                .issueType(dto.getIssueType())
                .categoryId(dto.getCategoryId())
                .status(dto.getStatus())
                .clientId(dto.getClientId())
                .assignedToId(dto.getAssignedToId())
                .build();
    }

    //  Map client DTO to create command - client has limited control
    public TicketCreateCommand fromClientDto(TicketCreateByClientDto dto, Long clientId) {
        return TicketCreateCommand.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .priority(dto.getPriority())
                .issueType(dto.getIssueType())
                .categoryId(dto.getCategoryId())
                .clientId(clientId)
                .build();
    }

    // Map admin update DTO to create command for update operation
    public TicketCreateCommand fromAdminUpdateDto(TicketUpdateByAdminDtoRequest dto, Long ticketId) {
        return TicketCreateCommand.builder()
                .id(ticketId)
                .title(dto.getTitle())
                .description(dto.getDescription())
                .priority(dto.getPriority())
                .issueType(dto.getIssueType())
                .categoryId(dto.getCategoryId())
                .status(dto.getStatus())
                .clientId(dto.getClientId())
                .assignedToId(dto.getAssignedToId())
                .build();
    }

    // Map client update DTO to create command for update operation - limited fields
    public TicketCreateCommand fromClientUpdateDto(TicketUpdateByClientDtoRequest dto, Long ticketId, Long clientId) {
        return TicketCreateCommand.builder()
                .id(ticketId)
                .title(dto.getTitle())
                .description(dto.getDescription())
                .priority(dto.getPriority())
                .issueType(dto.getIssueType())
                .categoryId(dto.getCategoryId())
                .clientId(clientId)
                // Note: Client cannot update status, assignment, or other sensitive fields
                .build();
    }
}