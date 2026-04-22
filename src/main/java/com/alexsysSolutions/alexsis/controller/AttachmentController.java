package com.alexsysSolutions.alexsis.controller;

import com.alexsysSolutions.alexsis.dto.request.attachment.AttachmentCreateDtoRequest;
import com.alexsysSolutions.alexsis.dto.response.ApiResponse;
import com.alexsysSolutions.alexsis.dto.response.attachment.AttachmentDtoResponse;
import com.alexsysSolutions.alexsis.mapper.AttachmentMapper;
import com.alexsysSolutions.alexsis.service.AttachmentService;
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

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/attachments")
@Tag(name = "attachments management", description = "attachments management endpoints")
public class AttachmentController {

    private final AttachmentService attachmentService;
    private final AttachmentMapper attachmentMapper;
    private static final Logger logger = LoggerFactory.getLogger(AttachmentController.class);

    // Create new attachment
    @PostMapping
    @Operation(summary = "create new attachment")
    @PreAuthorize("isAuthenticated()")
    public AttachmentDtoResponse create(@ModelAttribute AttachmentCreateDtoRequest dto) {
        return attachmentService.create(dto);
    }
    // Get attachment by id
    @GetMapping("/{id}")
    @Operation(summary = "get attachment by id")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<AttachmentDtoResponse>> getById(
            @PathVariable Long id,
            HttpServletRequest request
    ) {
        logger.info("GET /api/v1/attachments/{} - Fetching attachment by id", id);
        AttachmentDtoResponse attachment = attachmentService.getById(id);
        ApiResponse<AttachmentDtoResponse> response = ApiResponse.success("Attachment retrieved successfully", attachment);
        response.setStatus(HttpStatus.OK.value());
        response.setPath(request.getRequestURI());
        logger.info("Attachment with ID {} retrieved successfully", id);

        return ResponseEntity.ok(response);
    }

    // Get all attachments by ticket id with pagination
    @GetMapping("/ticket/{ticketId}")
    @Operation(summary = "get all attachments by ticket id")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<Page<AttachmentDtoResponse>>> getAllByTicketId(
            @PathVariable long ticketId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request
    ) {
        logger.info("GET /api/v1/attachments/ticket/{} - Fetching attachments by ticket id. Page: {}, Size: {}",
                ticketId, page, size);
        Page<AttachmentDtoResponse> attachments = attachmentService.getAllByTicketId(ticketId, page, size);
        ApiResponse<Page<AttachmentDtoResponse>> response = ApiResponse.success("Attachments retrieved successfully", attachments);
        response.setStatus(HttpStatus.OK.value());
        response.setPath(request.getRequestURI());
        logger.info("Retrieved {} attachments for ticket {}", attachments.getTotalElements(), ticketId);

        return ResponseEntity.ok(response);
    }

    // Delete attachment by id
    @DeleteMapping("/{id}")
    @Operation(summary = "delete attachment by id")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<Void>> deleteById(
            @PathVariable Long id,
            HttpServletRequest request
    ) {
        logger.info("DELETE /api/v1/attachments/{} - Deleting attachment", id);
        attachmentService.deleteById(id);
        ApiResponse<Void> response = ApiResponse.success("Attachment deleted successfully", null);
        response.setStatus(HttpStatus.OK.value());
        response.setPath(request.getRequestURI());
        logger.info("Attachment with ID {} deleted successfully", id);

        return ResponseEntity.ok(response);
    }
}
