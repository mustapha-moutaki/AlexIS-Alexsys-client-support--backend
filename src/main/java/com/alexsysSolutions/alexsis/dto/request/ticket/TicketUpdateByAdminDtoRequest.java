package com.alexsysSolutions.alexsis.dto.request.ticket;

import com.alexsysSolutions.alexsis.enums.IssueType;
import com.alexsysSolutions.alexsis.enums.Priority;
import com.alexsysSolutions.alexsis.enums.TicketStatus;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class TicketUpdateByAdminDtoRequest {
    private String title;
    private String description;
    private TicketStatus status;
    private Priority priority;
    private IssueType issueType;
    private Long categoryId;

    private Long clientId;
    private Long assignedToId;
}
