package com.alexsysSolutions.alexsis.dto.request.attachment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AttachmentCreateDtoRequest {

    MultipartFile file;
    String uploadedBy;
}
