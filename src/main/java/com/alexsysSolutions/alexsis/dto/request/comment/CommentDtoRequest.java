package com.alexsysSolutions.alexsis.dto.request.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CommentDtoRequest {

    @NotBlank(message = "the comment is empty")
    private String content;
    @NotNull(message = "ticket is required")
    private Long ticketId;
    @NotNull(message = "author id is required")
    private Long userId;

}
