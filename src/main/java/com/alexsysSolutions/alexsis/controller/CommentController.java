package com.alexsysSolutions.alexsis.controller;

import com.alexsysSolutions.alexsis.dto.request.comment.CommentDtoRequest;
import com.alexsysSolutions.alexsis.dto.response.comment.CommentDtoResponse;
import com.alexsysSolutions.alexsis.dto.response.ApiResponse;
import com.alexsysSolutions.alexsis.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
@Tag(name = "comments management", description = "endpoints for managing ticket comments")
public class CommentController {

    private final CommentService commentService;
    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);

    // create a new comment
    @Operation(summary = "create new comment")
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<CommentDtoResponse>> create(
            @Valid
            @RequestBody CommentDtoRequest dto,
            HttpServletRequest request
    ) {
        logger.info("POST /api/v1/comments - Creating new comment");

        CommentDtoResponse comment = commentService.create(dto);
        ApiResponse<CommentDtoResponse> response = ApiResponse.success("Comment created successfully", comment);
        response.setStatus(HttpStatus.CREATED.value());
        response.setPath(request.getRequestURI());
        logger.info("Comment created successfully with ID: {}", comment.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // update an existing comment
    @Operation(summary = "update comment")
    @PatchMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<CommentDtoResponse>> update(
            @PathVariable Long id,
            @Valid
            @RequestBody CommentDtoRequest dto,
            HttpServletRequest request
    ) {
        logger.info("PATCH /api/v1/comments/{} - Updating comment", id);

        CommentDtoResponse comment = commentService.update(id, dto);
        ApiResponse<CommentDtoResponse> response = ApiResponse.success("Comment updated successfully", comment);
        response.setStatus(HttpStatus.OK.value());
        response.setPath(request.getRequestURI());
        logger.info("Comment with ID {} updated successfully", id);
        return ResponseEntity.ok(response);
    }

    // get comment by id
    @Operation(summary = "get comment by id")
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<CommentDtoResponse>> getById(
            @PathVariable Long id,
            HttpServletRequest request
    ) {
        logger.info("GET /api/v1/comments/{} - Fetching comment by id", id);

        CommentDtoResponse comment = commentService.getById(id);
        ApiResponse<CommentDtoResponse> response = ApiResponse.success("Comment retrieved successfully", comment);
        response.setStatus(HttpStatus.OK.value());
        response.setPath(request.getRequestURI());
        logger.info("Comment with ID {} retrieved successfully", id);
        return ResponseEntity.ok(response);
    }

    // get all comments with pagination
    @Operation(summary = "get all comments")
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<Page<CommentDtoResponse>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request
    ) {
        logger.info("GET /api/v1/comments - Fetching all comments with page: {}, size: {}", page, size);

        Page<CommentDtoResponse> comments = commentService.getAll(page, size);
        ApiResponse<Page<CommentDtoResponse>> response = ApiResponse.success("Comments retrieved successfully", comments);
        response.setStatus(HttpStatus.OK.value());
        response.setPath(request.getRequestURI());
        logger.info("Retrieved {} total comments", comments.getTotalElements());
        return ResponseEntity.ok(response);
    }

    // get all comments by ticket id with pagination
    @Operation(summary = "get all comments for a specific ticket")
    @GetMapping("/ticket/{ticketId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<Page<CommentDtoResponse>>> getAllByTicketId(
            @PathVariable Long ticketId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request
    ) {
        logger.info("GET /api/v1/comments/ticket/{} - Fetching all comments for ticket with page: {}, size: {}", ticketId, page, size);

        Page<CommentDtoResponse> comments = commentService.getAllByTicketId(ticketId, page, size);
        ApiResponse<Page<CommentDtoResponse>> response = ApiResponse.success("Comments for ticket retrieved successfully", comments);
        response.setStatus(HttpStatus.OK.value());
        response.setPath(request.getRequestURI());
        logger.info("Retrieved {} total comments for ticket ID {}", comments.getTotalElements(), ticketId);
        return ResponseEntity.ok(response);
    }

    // delete a comment
    @Operation(summary = "delete comment")
    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable Long id,
            HttpServletRequest request
    ) {
        logger.info("DELETE /api/v1/comments/{} - Deleting comment", id);

        commentService.deleteById(id);
        ApiResponse<Void> response = ApiResponse.success("Comment deleted successfully", null);
        response.setStatus(HttpStatus.OK.value());
        response.setPath(request.getRequestURI());
        logger.info("Comment with ID {} deleted successfully", id);
        return ResponseEntity.ok(response);
    }
}

