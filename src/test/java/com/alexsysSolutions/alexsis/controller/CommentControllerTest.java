package com.alexsysSolutions.alexsis.controller;

import com.alexsysSolutions.alexsis.dto.request.comment.CommentDtoRequest;
import com.alexsysSolutions.alexsis.dto.response.ApiResponse;
import com.alexsysSolutions.alexsis.dto.response.comment.CommentDtoResponse;
import com.alexsysSolutions.alexsis.service.CommentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CommentControllerTest {

    @Mock
    private CommentService commentService;

    @Mock
    private HttpServletRequest httpServletRequest;

    @InjectMocks
    private CommentController commentController;

    private CommentDtoRequest commentRequest;
    private CommentDtoResponse commentResponse;
    private Page<CommentDtoResponse> commentPage;

    @BeforeEach
    void setUp() {
        // Setup test data
        commentRequest = CommentDtoRequest.builder()
                .content("Test comment content")
                .ticketId(1L)
                .build();

        commentResponse = CommentDtoResponse.builder()
                .id(1L)
                .content("Test comment content")
                .build();

        List<CommentDtoResponse> commentList = Arrays.asList(commentResponse);
        commentPage = new PageImpl<>(commentList);
    }

    @Test
    void create_shouldReturnCreatedComment() {
        // Arrange
        when(commentService.create(commentRequest)).thenReturn(commentResponse);
        when(httpServletRequest.getRequestURI()).thenReturn("/api/v1/comments");

        // Act
        ResponseEntity<ApiResponse<CommentDtoResponse>> response =
                commentController.create(commentRequest, httpServletRequest);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isSuccess());
        assertEquals("Comment created successfully", response.getBody().getMessage());
        assertEquals(commentResponse, response.getBody().getData());

        verify(commentService).create(commentRequest);
    }

    @Test
    void update_shouldReturnUpdatedComment() {
        // Arrange
        Long commentId = 1L;
        when(commentService.update(commentId, commentRequest)).thenReturn(commentResponse);
        when(httpServletRequest.getRequestURI()).thenReturn("/api/v1/comments/1");

        // Act
        ResponseEntity<ApiResponse<CommentDtoResponse>> response =
                commentController.update(commentId, commentRequest, httpServletRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isSuccess());
        assertEquals("Comment updated successfully", response.getBody().getMessage());
        assertEquals(commentResponse, response.getBody().getData());

        verify(commentService).update(commentId, commentRequest);
    }

    @Test
    void getById_shouldReturnComment() {
        // Arrange
        Long commentId = 1L;
        when(commentService.getById(commentId)).thenReturn(commentResponse);
        when(httpServletRequest.getRequestURI()).thenReturn("/api/v1/comments/1");

        // Act
        ResponseEntity<ApiResponse<CommentDtoResponse>> response =
                commentController.getById(commentId, httpServletRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isSuccess());
        assertEquals("Comment retrieved successfully", response.getBody().getMessage());
        assertEquals(commentResponse, response.getBody().getData());

        verify(commentService).getById(commentId);
    }

    @Test
    void getAll_shouldReturnPaginatedComments() {
        // Arrange
        int page = 0;
        int size = 10;
        when(commentService.getAll(page, size)).thenReturn(commentPage);
        when(httpServletRequest.getRequestURI()).thenReturn("/api/v1/comments");

        // Act
        ResponseEntity<ApiResponse<Page<CommentDtoResponse>>> response =
                commentController.getAll(page, size, httpServletRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isSuccess());
        assertEquals("Comments retrieved successfully", response.getBody().getMessage());
        assertEquals(commentPage, response.getBody().getData());
        assertEquals(1, response.getBody().getData().getContent().size());

        verify(commentService).getAll(page, size);
    }

    @Test
    void getAllByTicketId_shouldReturnCommentsForTicket() {
        // Arrange
        Long ticketId = 1L;
        int page = 0;
        int size = 10;
        when(commentService.getAllByTicketId(ticketId, page, size)).thenReturn(commentPage);
        when(httpServletRequest.getRequestURI()).thenReturn("/api/v1/comments/ticket/1");

        // Act
        ResponseEntity<ApiResponse<Page<CommentDtoResponse>>> response =
                commentController.getAllByTicketId(ticketId, page, size, httpServletRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isSuccess());
        assertEquals("Comments for ticket retrieved successfully", response.getBody().getMessage());
        assertEquals(commentPage, response.getBody().getData());

        verify(commentService).getAllByTicketId(ticketId, page, size);
    }

    @Test
    void delete_shouldReturnSuccessMessage() {
        // Arrange
        Long commentId = 1L;
        doNothing().when(commentService).deleteById(commentId);
        when(httpServletRequest.getRequestURI()).thenReturn("/api/v1/comments/1");

        // Act
        ResponseEntity<ApiResponse<Void>> response =
                commentController.delete(commentId, httpServletRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isSuccess());
        assertEquals("Comment deleted successfully", response.getBody().getMessage());
        assertNull(response.getBody().getData());

        verify(commentService).deleteById(commentId);
    }


}