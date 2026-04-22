package com.alexsysSolutions.alexsis.service;

import com.alexsysSolutions.alexsis.dto.request.attachment.AttachmentCreateDtoRequest;
import com.alexsysSolutions.alexsis.dto.response.attachment.AttachmentDtoResponse;
import com.alexsysSolutions.alexsis.model.Attachment;
import org.springframework.data.domain.Page;

public interface AttachmentService {
    AttachmentDtoResponse create(AttachmentCreateDtoRequest dto);
    Page<AttachmentDtoResponse> getAllByTicketId(long id, int page, int size);
    AttachmentDtoResponse getById(Long id);
    void deleteById(Long id);
}
