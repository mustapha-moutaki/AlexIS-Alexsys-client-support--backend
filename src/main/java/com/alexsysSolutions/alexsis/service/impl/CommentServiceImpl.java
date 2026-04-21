package com.alexsysSolutions.alexsis.service.impl;

import com.alexsysSolutions.alexsis.dto.request.comment.CommentDtoRequest;
import com.alexsysSolutions.alexsis.dto.response.comment.CommentDtoResponse;
import com.alexsysSolutions.alexsis.exception.ResourceNotFoundException;
import com.alexsysSolutions.alexsis.exception.ValidationException;
import com.alexsysSolutions.alexsis.mapper.CommentMapper;
import com.alexsysSolutions.alexsis.model.Comment;
import com.alexsysSolutions.alexsis.model.Ticket;
import com.alexsysSolutions.alexsis.reposiotry.CommentRepository;
import com.alexsysSolutions.alexsis.reposiotry.TicketRepository;
import com.alexsysSolutions.alexsis.security.context.CurrentUserProvider;
import com.alexsysSolutions.alexsis.service.CommentService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private static final Logger logger = LoggerFactory.getLogger(CommentServiceImpl.class);
    private final CurrentUserProvider currentUser;
    private final TicketRepository ticketRepository;


    @Override
    public CommentDtoResponse create(CommentDtoRequest dto) {
        logger.info("Creating new comment");

        // validate comment content is not empty
        if (!StringUtils.hasText(dto.getContent())) {
            logger.warn("Attempted to create comment with empty content");
            throw new ValidationException("Comment content cannot be empty");
        }

        Ticket ticket = ticketRepository.findById(dto.getTicketId()).orElseThrow(
                ()->new ValidationException(
                        String.format("ticket with this id: %s is not exist", dto.getTicketId())
                )
        );

        Comment comment = commentMapper.toEntity(dto);

        comment.setCreatedBy(currentUser.getEmail());
        comment.setTicket(ticket);
        comment.setAuthor(currentUser.getCurrentUser().getUser());
        // save and return
        Comment savedComment = commentRepository.save(comment);
        logger.info("Comment created successfully with ID: {}", savedComment.getId());

        return commentMapper.toDto(savedComment);
    }

    @Override
    public CommentDtoResponse update(Long id, CommentDtoRequest dto) {
        logger.info("Updating comment with ID: {}", id);

        // validate id
        if (id == null || id <= 0) {
            logger.warn("Invalid comment ID provided for update: {}", id);
            throw new ValidationException("Comment ID is invalid");
        }

        // validate comment content is not empty
        if (!StringUtils.hasText(dto.getContent())) {
            logger.warn("Attempted to update comment {} with empty content", id);
            throw new ValidationException("Comment content cannot be empty");
        }

        // check if comment exists
        Comment existingComment = commentRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Comment with ID {} not found for update", id);
                    return new ResourceNotFoundException(String.format("Comment with ID %d not found", id));
                });

        // update comment fields
        existingComment.setContent(dto.getContent());
        Comment updatedComment = commentRepository.save(existingComment);

        logger.info("Comment with ID {} updated successfully", id);
        return commentMapper.toDto(updatedComment);
    }

    @Override
    public Page<CommentDtoResponse> getAll(int page, int size) {
        logger.info("Fetching all comments with page: {}, size: {}", page, size);

        Pageable pageable = PageRequest.of(page, size);
        Page<CommentDtoResponse> comments = commentRepository.findAll(pageable)
                .map(commentMapper::toDto);

        logger.info("Retrieved {} total comments", comments.getTotalElements());
        return comments;
    }

    @Override
    public CommentDtoResponse getById(Long id) {
        logger.info("Fetching comment with ID: {}", id);

        // validate id
        if (id == null || id <= 0) {
            logger.warn("Invalid comment ID provided: {}", id);
            throw new ValidationException("Comment ID is invalid");
        }

        CommentDtoResponse comment = commentRepository.findById(id)
                .map(commentMapper::toDto)
                .orElseThrow(() -> {
                    logger.error("Comment with ID {} not found", id);
                    return new ResourceNotFoundException(String.format("Comment with ID %d not found", id));
                });

        logger.info("Comment with ID {} retrieved successfully", id);
        return comment;
    }

    @Override
    public Page<CommentDtoResponse> getAllByTicketId(Long ticketId, int page, int size) {
        logger.info("Fetching all comments for ticket ID: {} with page: {}, size: {}", ticketId, page, size);

        // validate ticket id
        if (ticketId == null || ticketId <= 0) {
            logger.warn("Invalid ticket ID provided: {}", ticketId);
            throw new ValidationException("Ticket ID is invalid");
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<CommentDtoResponse> comments = commentRepository.findByTicketId(ticketId, pageable)
                .map(commentMapper::toDto);

        logger.info("Retrieved {} total comments for ticket ID {}", comments.getTotalElements(), ticketId);
        return comments;
    }

    @Override
    public void deleteById(Long id) {
        logger.info("Deleting comment with ID: {}", id);

        // validate id
        if (id == null || id <= 0) {
            logger.warn("Invalid comment ID provided for deletion: {}", id);
            throw new ValidationException("Comment ID is invalid");
        }

        // check if comment exists before deleting
        if (!commentRepository.existsById(id)) {
            logger.error("Comment with ID {} not found for deletion", id);
            throw new ResourceNotFoundException(String.format("Comment with ID %d not found", id));
        }

        commentRepository.deleteById(id);
        logger.info("Comment with ID {} deleted successfully", id);
    }
}
