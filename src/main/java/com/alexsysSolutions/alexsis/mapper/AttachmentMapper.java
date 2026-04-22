package com.alexsysSolutions.alexsis.mapper;

import com.alexsysSolutions.alexsis.dto.request.attachment.AttachmentCreateDtoRequest;
import com.alexsysSolutions.alexsis.dto.response.attachment.AttachmentDtoResponse;
import com.alexsysSolutions.alexsis.model.Attachment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AttachmentMapper {

    AttachmentDtoResponse toDto(Attachment attachment);
    Attachment toEntity(AttachmentCreateDtoRequest dto);
}
