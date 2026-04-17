package com.alexsysSolutions.alexsis.controller;

import com.alexsysSolutions.alexsis.dto.request.category.CategoryDtoRequest;
import com.alexsysSolutions.alexsis.dto.response.Category.CategoryDtoResponse;
import com.alexsysSolutions.alexsis.dto.response.ApiResponse;
import com.alexsysSolutions.alexsis.service.CategoryService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    // Create a new category
    @PostMapping
    public ResponseEntity<ApiResponse<CategoryDtoResponse>> create(
            @Valid
            @RequestBody CategoryDtoRequest dto,
            HttpServletRequest request
    ) {
        logger.info("POST /api/v1/categories - Creating category with name: {}", dto.getName());
        try {
            CategoryDtoResponse category = categoryService.create(dto);
            ApiResponse<CategoryDtoResponse> response = ApiResponse.success("Category created successfully", category);
            response.setStatus(HttpStatus.CREATED.value());
            response.setPath(request.getRequestURI());
            logger.info("Category created successfully with ID: {}", category.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            logger.error("Error creating category", e);
            throw e;
        }
    }

    // Update an existing category
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryDtoResponse>> update(
            @PathVariable Long id,
            @Valid
            @RequestBody CategoryDtoRequest dto,
            HttpServletRequest request
    ) {
        logger.info("PATCH /api/v1/categories/{} - Updating category", id);
        try {
            CategoryDtoResponse category = categoryService.update(id, dto);
            ApiResponse<CategoryDtoResponse> response = ApiResponse.success("Category updated successfully", category);
            response.setPath(request.getRequestURI());
            response.setStatus(HttpStatus.OK.value());
            logger.info("Category with ID {} updated successfully", id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error updating category with id: {}", id, e);
            throw e;
        }
    }

    // Get category by id
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryDtoResponse>> getById(
            @PathVariable Long id,
            HttpServletRequest request
    ) {
        logger.info("GET /api/v1/categories/{} - Fetching category by id", id);
        try {
            CategoryDtoResponse category = categoryService.getById(id);
            ApiResponse<CategoryDtoResponse> response = ApiResponse.success("Category retrieved successfully", category);
            response.setPath(request.getRequestURI());
            response.setStatus(HttpStatus.OK.value());
            logger.info("Category with ID {} retrieved successfully", id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error fetching category with id: {}", id, e);
            throw e;
        }
    }

    // Get all categories with pagination and search
    @GetMapping
    public ResponseEntity<ApiResponse<Page<CategoryDtoResponse>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String searchKeyword,
            HttpServletRequest request
    ) {
        logger.info("GET /api/v1/categories - Fetching all categories with page: {}, size: {}, searchKeyword: {}",
                page, size, searchKeyword);
        try {
            Page<CategoryDtoResponse> categories = categoryService.getAll(page, size, searchKeyword);
            ApiResponse<Page<CategoryDtoResponse>> response = ApiResponse.success("Categories retrieved successfully", categories);
            response.setPath(request.getRequestURI());
            response.setStatus(HttpStatus.OK.value());
            logger.info("Retrieved {} total categories", categories.getTotalElements());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error fetching categories", e);
            throw e;
        }
    }

    // Delete a category
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable Long id,
            HttpServletRequest request
    ) {
        logger.info("DELETE /api/v1/categories/{} - Deleting category", id);
        try {
            categoryService.delete(id);
            ApiResponse<Void> response = ApiResponse.success("Category deleted successfully", null);
            response.setPath(request.getRequestURI());
            response.setStatus(HttpStatus.OK.value());
            logger.info("Category with ID {} deleted successfully", id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error deleting category with id: {}", id, e);
            throw e;
        }
    }
}

