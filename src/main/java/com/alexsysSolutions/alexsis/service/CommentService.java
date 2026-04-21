package com.alexsysSolutions.alexsis.service;

import com.alexsysSolutions.alexsis.dto.request.comment.CommentDtoRequest;
import com.alexsysSolutions.alexsis.dto.response.comment.CommentDtoResponse;
import org.springframework.data.domain.Page;

public interface CommentService {
    CommentDtoResponse create(CommentDtoRequest dto);
    CommentDtoResponse update(Long id, CommentDtoRequest dto);
    Page<CommentDtoResponse> getAll(int page, int size);
    CommentDtoResponse getById(Long id);
    Page<CommentDtoResponse> getAllByTicketId(Long ticketId, int page, int size);
    void deleteById(Long id);
}
