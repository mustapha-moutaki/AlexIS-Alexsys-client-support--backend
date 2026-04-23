package com.alexsysSolutions.alexsis.dto.request.ticket;

import com.alexsysSolutions.alexsis.enums.IssueType;
import com.alexsysSolutions.alexsis.enums.Priority;
import com.alexsysSolutions.alexsis.enums.TicketStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
// Admin Agent create ticket
public class TicketCreateByAdminDto {
    @NotBlank(message = "title is required")
    private String title;

    private String description;

    @NotNull
    private TicketStatus status;

    @NotNull
    private Priority priority;

    @NotNull
    private IssueType issueType;
    @NotNull(message = "category is required")
    private Long categoryId;
    @NotNull(message = "client is missing")
    private Long clientId;

    private Long assignedToId;
}
