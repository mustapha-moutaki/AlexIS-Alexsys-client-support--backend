package com.alexsysSolutions.alexsis.controller;

import com.alexsysSolutions.alexsis.dto.request.user.UserCreateDtoRequest;
import com.alexsysSolutions.alexsis.dto.request.user.UserUpdateDtoRequest;
import com.alexsysSolutions.alexsis.dto.response.ApiResponse;
import com.alexsysSolutions.alexsis.dto.response.user.UserDtoResponse;
import com.alexsysSolutions.alexsis.enums.UserRole;
import com.alexsysSolutions.alexsis.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name="users management", description = "users management endpoints")
public class UserController {

    private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    // create
    @Operation(summary = "create new user admin")
    @PostMapping
    public ResponseEntity<ApiResponse<UserDtoResponse>>create(
            @Valid
            @RequestBody UserCreateDtoRequest dto,
            HttpServletRequest request
    ){
        logger.info("st: POST /api/v1/users - Creating user with email: {}", dto.getEmail());   
        UserDtoResponse user = userService.create(dto);
        ApiResponse<UserDtoResponse>response = ApiResponse.success("user created successfully", user);
        response.setStatus(HttpStatus.CREATED.value());
        response.setPath(request.getRequestURI());
        logger.info("st: User created successfully with ID: {}", user.getId());   

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "update user data")
    // update
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDtoResponse>>update(
            @PathVariable Long id,
            @Valid
            @RequestBody UserUpdateDtoRequest dto,
            HttpServletRequest http
    ){
        logger.info("st: PATCH /api/v1/users/{} - Updating user", id);   
        UserDtoResponse user = userService.update(id, dto);
        ApiResponse<UserDtoResponse> response = ApiResponse.success("User updated successfully", user);
        response.setPath(http.getRequestURI());
        response.setStatus(HttpStatus.OK.value());
        logger.info("st: User with ID {} updated successfully", id);   
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "get single user by id")
    // getById
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDtoResponse>>getById(
            @PathVariable Long id,
            HttpServletRequest http
    ){
        logger.info("st: GET /api/v1/users/{} - Fetching user by id", id);   
        UserDtoResponse user = userService.getById(id);
        ApiResponse<UserDtoResponse> response = ApiResponse.success("User retrieved successfully", user);
        response.setPath(http.getRequestURI());
        response.setStatus(HttpStatus.OK.value());
        logger.info("st: User with ID {} retrieved successfully", id);   
        return ResponseEntity.ok(response);
    }


    @Operation(summary = "Get all users")
    // get all
    @GetMapping
    public ResponseEntity<ApiResponse<Page<UserDtoResponse>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            @RequestParam(required = false) UserRole role,
            @RequestParam(required = false)
            @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false)
            @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(defaultValue = "false") boolean includeDeleted,
            HttpServletRequest request
    ) {
        logger.info("st: GET /api/v1/users - Fetching all users with filters. Page: {}, Size: {}, Role: {}",
                page, size, role);   
        // the service handles all parameters
        Page<UserDtoResponse> users = userService.getAll(
                page, size, sortBy, direction, role, startDate, endDate, includeDeleted
        );

        ApiResponse<Page<UserDtoResponse>> response = ApiResponse.success("Users retrieved", users);
        response.setPath(request.getRequestURI());
        response.setStatus(HttpStatus.OK.value());

        logger.info("st: Retrieved {} users total", users.getTotalElements());   
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete user")
    // delete
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id, HttpServletRequest http) {
        logger.info("st: DELETE /api/v1/users/{} - Deleting user", id);   
        userService.delete(id);

        ApiResponse<Void> response = ApiResponse.success("User deleted successfully", null);
        response.setPath(http.getRequestURI());
        response.setStatus(HttpStatus.OK.value());

        logger.info("st: User with ID {} deleted successfully", id);   
        return ResponseEntity.ok(response);
    }

}
