package com.alexsysSolutions.alexsis.dto.response.attachment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class AttachmentDtoResponse {
    private Long id;
    private String fileName;
    private String fileUrl;
    private String fileType;
    private LocalDateTime uploadedAt;
}
