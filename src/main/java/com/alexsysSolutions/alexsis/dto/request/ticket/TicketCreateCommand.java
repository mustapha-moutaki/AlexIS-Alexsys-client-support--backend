package com.alexsysSolutions.alexsis.dto.request.ticket;

import com.alexsysSolutions.alexsis.enums.IssueType;
import com.alexsysSolutions.alexsis.enums.Priority;
import com.alexsysSolutions.alexsis.enums.TicketStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TicketCreateCommand {

    private String title;
    private String description;

    private Priority priority;
    private IssueType issueType;

    private Long categoryId;

    // optional (admin only)
    private TicketStatus status;
    private Long clientId;
    private Long assignedToId;
}
