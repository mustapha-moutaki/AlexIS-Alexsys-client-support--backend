package com.alexsysSolutions.alexsis.dto.response.ticket;

import com.alexsysSolutions.alexsis.enums.IssueType;
import com.alexsysSolutions.alexsis.enums.Priority;
import com.alexsysSolutions.alexsis.enums.TicketStatus;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class TicketSummaryDtoResponse {
    private Long id;
    private String title;
    private String description;
    private TicketStatus status;
    private Priority priority;
    private IssueType issueType;
    private Long categoryId; // category id
    private Long clientId; // client id
    private Long assignedToId; // assignedTo

    private int commentCount;
    private int attachmentCount;

}
