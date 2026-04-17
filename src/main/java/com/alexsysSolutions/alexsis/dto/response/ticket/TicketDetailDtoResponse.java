package com.alexsysSolutions.alexsis.dto.response.ticket;

import com.alexsysSolutions.alexsis.dto.response.Category.CategoryLiteDto;
import com.alexsysSolutions.alexsis.dto.response.attachment.AttachmentDtoResponse;
import com.alexsysSolutions.alexsis.dto.response.comment.CommentDtoResponse;
import com.alexsysSolutions.alexsis.enums.IssueType;
import com.alexsysSolutions.alexsis.enums.Priority;
import com.alexsysSolutions.alexsis.enums.TicketStatus;
import com.alexsysSolutions.alexsis.model.User;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class TicketDetailDtoResponse {

    private Long id;
    private String title;
    private String description;
    private TicketStatus status;
    private Priority priority;
    private IssueType issueType;

    private CategoryLiteDto category;

    private String clientName;
    private String assignedToName;

    private List<CommentDtoResponse> comments;
    private List<AttachmentDtoResponse> attachments;

    private LocalDateTime assignedAt;
    private LocalDateTime resolvedAt;
    private LocalDateTime closedAt;

}
