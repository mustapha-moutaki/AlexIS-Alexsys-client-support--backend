package com.alexsysSolutions.alexsis.dto.request.ticket;

import com.alexsysSolutions.alexsis.enums.IssueType;
import com.alexsysSolutions.alexsis.enums.Priority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
// Client create ticket
public class TicketCreateByClientDto {
    @NotBlank(message = "title is required")
    private String title;
    @NotBlank(message = "description is required")
    private String description;
    @NotNull(message = "priority is required")
    private Priority priority;
    @NotNull(message = "issue type is required")
    private IssueType issueType;
    @NotBlank(message = "category is required")
    private Long categoryId;
    private List<Long> attachmentIds = new ArrayList<>();
}
