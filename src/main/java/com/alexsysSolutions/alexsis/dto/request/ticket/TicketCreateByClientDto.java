package com.alexsysSolutions.alexsis.dto.request.ticket;

import com.alexsysSolutions.alexsis.enums.IssueType;
import com.alexsysSolutions.alexsis.enums.Priority;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
// Client create ticket
public class TicketCreateByClientDto {
    private String title;
    private String description;
    private Priority priority;
    private IssueType issueType;
    private Long categoryId;
}
