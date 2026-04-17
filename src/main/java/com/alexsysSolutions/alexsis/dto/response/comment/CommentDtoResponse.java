package com.alexsysSolutions.alexsis.dto.response.comment;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CommentDtoResponse {
    private Long id;
    private String content;
    private String authorName;
    private LocalDateTime createdAt;
}
