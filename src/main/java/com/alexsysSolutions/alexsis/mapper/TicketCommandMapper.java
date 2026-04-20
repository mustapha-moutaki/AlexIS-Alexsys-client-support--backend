package com.alexsysSolutions.alexsis.mapper;

import com.alexsysSolutions.alexsis.dto.request.ticket.TicketCreateByAdminDto;
import com.alexsysSolutions.alexsis.dto.request.ticket.TicketCreateByClientDto;
import com.alexsysSolutions.alexsis.dto.request.ticket.TicketCreateCommand;
import org.springframework.stereotype.Component;

@Component
public class TicketCommandMapper {

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
}