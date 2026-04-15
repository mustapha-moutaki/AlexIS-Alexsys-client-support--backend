package com.alexsysSolutions.alexsis.controller;

import com.alexsysSolutions.alexsis.dto.request.user.UserCreateDtoRequest;
import com.alexsysSolutions.alexsis.dto.request.user.UserUpdateDtoRequest;
import com.alexsysSolutions.alexsis.dto.response.ApiResponse;
import com.alexsysSolutions.alexsis.dto.response.user.UserDtoResponse;
import com.alexsysSolutions.alexsis.service.UserService;
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
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    // create
    @PostMapping
    public ResponseEntity<ApiResponse<UserDtoResponse>>create(
            @Valid
            @RequestBody UserCreateDtoRequest dto,
            HttpServletRequest request
            ){
        UserDtoResponse user = userService.create(dto);
        ApiResponse<UserDtoResponse>response = ApiResponse.success("user created successfully", user);
        response.setStatus(HttpStatus.CREATED.value());
        response.setPath(request.getRequestURI());
        logger.info("User created with ID: {}", user.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // update
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDtoResponse>>update(
            @PathVariable Long id,
            @Valid
            @RequestBody UserUpdateDtoRequest dto,
            HttpServletRequest http
            ){
        UserDtoResponse user = userService.update(id, dto);
        ApiResponse<UserDtoResponse> response = ApiResponse.success("User updated successfully", user);
        response.setPath(http.getRequestURI());
        response.setStatus(HttpStatus.OK.value());
        logger.info("User with ID {} updated", id);
        return ResponseEntity.ok(response);
    }

    // getById
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDtoResponse>>getById(
            @PathVariable Long id,
            HttpServletRequest http
    ){
        UserDtoResponse user = userService.getById(id);
        ApiResponse<UserDtoResponse> response = ApiResponse.success("User retrieved successfully", user);
        response.setPath(http.getRequestURI());
        response.setStatus(HttpStatus.OK.value());
        logger.info("User with ID {} retrieved", id);
        return ResponseEntity.ok(response);
    }


    // get all
    @GetMapping
    public ResponseEntity<ApiResponse<Page<UserDtoResponse>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) LocalDateTime startDate,
            @RequestParam(required = false) LocalDateTime endDate,
            @RequestParam(defaultValue = "false") boolean includeDeleted,
            HttpServletRequest request
    ) {
        Page<UserDtoResponse> users;

        if (includeDeleted) {
            users = userService.getAllIncludingDeleted(page, size);
        } else {
            users = userService.getAll(page, size, sortBy, direction, role, startDate, endDate);
        }

        ApiResponse<Page<UserDtoResponse>> response = ApiResponse.success("Users retrieved", users);
        response.setPath(request.getRequestURI());
        response.setStatus(HttpStatus.OK.value());

        return ResponseEntity.ok(response);
    }

    // delete
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable Long id,
            HttpServletRequest http
    ){
        userService.delete(id);
        ApiResponse<Void> response = ApiResponse.success("User deleted successfully", null);
        response.setPath(http.getRequestURI());
        response.setStatus(HttpStatus.NO_CONTENT.value());
        logger.info("User with ID {} deleted", id);
        return ResponseEntity.noContent().build();
    }

}
