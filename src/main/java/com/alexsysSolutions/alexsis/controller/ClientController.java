package com.alexsysSolutions.alexsis.controller;

import com.alexsysSolutions.alexsis.dto.request.Client.ClientCreateDtoRequest;
import com.alexsysSolutions.alexsis.dto.request.Client.ClientUpdateByAdminDtoRequest;
import com.alexsysSolutions.alexsis.dto.request.Client.ClientUpdatePasswordDtoRequest;
import com.alexsysSolutions.alexsis.dto.request.Client.ClientUpdateProfileDtoRequest;
import com.alexsysSolutions.alexsis.dto.response.ApiResponse;
import com.alexsysSolutions.alexsis.dto.response.client.ClientDtoResponse;
import com.alexsysSolutions.alexsis.dto.response.client.ClientUpdateProfileDtoResponse;
import com.alexsysSolutions.alexsis.exception.ValidationException;
import com.alexsysSolutions.alexsis.security.context.CurrentUserProvider;
import com.alexsysSolutions.alexsis.service.ClientService;
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

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/admin/clients")
@RequiredArgsConstructor
@Tag(name = "Client Management", description = "Endpoints for managing clients")
@PreAuthorize("hasAnyRole('ADMIN')")
public class ClientController {

    private final ClientService clientService;
    private final CurrentUserProvider currentUser;
    private static final Logger logger = LoggerFactory.getLogger(ClientController.class);

    // Create a new client
    @Operation(summary = "Create a new client", description = "Create a client with basic user details")
    @PostMapping
    public ResponseEntity<ApiResponse<ClientDtoResponse>> create(
            @Valid @RequestBody ClientCreateDtoRequest dto,
            HttpServletRequest http
    ) {
        logger.info("POST /api/v1/admin/clients - Creating client with email: {}", dto.getEmail());

        ClientDtoResponse createdClient = clientService.create(dto);
        ApiResponse<ClientDtoResponse> response = ApiResponse.success("Client created successfully", createdClient);
        response.setPath(http.getRequestURI());
        response.setStatus(HttpStatus.CREATED.value());
        logger.info("Client created successfully with ID: {}", createdClient.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Update client by admin
    @Operation(summary = "Update client by admin", description = "Update client details including VIP status and satisfaction score")
    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<ClientDtoResponse>> updateByAdmin(
            @PathVariable Long id,
            @Valid @RequestBody ClientUpdateByAdminDtoRequest dto,
            HttpServletRequest http
    ) {
        logger.info("PATCH /api/v1/admin/clients/{} - Updating client by admin", id);

        ClientDtoResponse updatedClient = clientService.updateByAdmin(id, dto);
        ApiResponse<ClientDtoResponse> response = ApiResponse.success("Client updated successfully", updatedClient);
        response.setPath(http.getRequestURI());
        response.setStatus(HttpStatus.OK.value());
        logger.info("Client with ID {} updated successfully by admin", id);
        return ResponseEntity.ok(response);
    }

    // Get client by ID
    @Operation(summary = "Get client by ID", description = "Retrieve client details by ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ClientDtoResponse>> getById(
            @PathVariable Long id,
            HttpServletRequest http
    ) {
        logger.info("GET /api/v1/admin/clients/{} - Fetching client details", id);

        ClientDtoResponse client = clientService.getById(id);
        ApiResponse<ClientDtoResponse> response = ApiResponse.success("Client retrieved successfully", client);
        response.setPath(http.getRequestURI());
        response.setStatus(HttpStatus.OK.value());
        logger.info("Client with ID {} retrieved successfully", id);
        return ResponseEntity.ok(response);
    }

    // Get all clients with filters
    @Operation(summary = "Get all clients", description = "Retrieve paginated list of clients with optional filters")
    @GetMapping
    public ResponseEntity<ApiResponse<Page<ClientDtoResponse>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Boolean isVip,
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(defaultValue = "desc") String sortDirection,
            HttpServletRequest http
    ) {
        logger.info("GET /api/v1/admin/clients - Fetching clients - page: {}, size: {}, isVip: {}, isActive: {}", page, size, isVip, isActive);

        Page<ClientDtoResponse> clients = clientService.getAll(page, size, isVip, isActive, sortDirection);
        ApiResponse<Page<ClientDtoResponse>> response = ApiResponse.success("Clients retrieved successfully", clients);
        response.setPath(http.getRequestURI());
        response.setStatus(HttpStatus.OK.value());
        logger.info("Retrieved {} clients", clients.getTotalElements());
        return ResponseEntity.ok(response);
    }

    // Delete client
    @Operation(summary = "Delete client", description = "Remove a client from the system")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable Long id,
            HttpServletRequest http
    ) {
        logger.info("DELETE /api/v1/admin/clients/{} - Deleting client", id);

        clientService.delete(id);
        ApiResponse<Void> response = ApiResponse.success("Client deleted successfully", null);
        response.setPath(http.getRequestURI());
        response.setStatus(HttpStatus.OK.value());
        logger.info("Client with ID {} deleted successfully", id);
        return ResponseEntity.ok(response);
    }

    // Update client profile (accessible by client themselves)
    @Operation(summary = "Update client profile", description = "Allow clients to update their own profile")
    @PatchMapping("/profile")
    @PreAuthorize("hasRole('CLIENT')")  // Override to allow clients
    public ResponseEntity<ApiResponse<ClientUpdateProfileDtoResponse>> updateProfile(
            @Valid @RequestBody ClientUpdateProfileDtoRequest dto,
            HttpServletRequest http
    ) {
        logger.info("PATCH /api/v1/admin/clients/profile - Updating client profile");

        Long clientId = currentUser.getUserId();
        ClientUpdateProfileDtoResponse updatedProfile = clientService.updateProfile(clientId, dto);
        ApiResponse<ClientUpdateProfileDtoResponse> response = ApiResponse.success("Profile updated successfully", updatedProfile);
        response.setPath(http.getRequestURI());
        response.setStatus(HttpStatus.OK.value());
        logger.info("Profile updated successfully for client {}", clientId);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/change-password")
    @PreAuthorize("hasRole('CLIENT')")  // Allow clients to change their own password
    @Operation(summary = "Update client password", description = "Allow clients to update their own password")

    public ResponseEntity<ApiResponse<Void>> changePassword(
            @Valid @RequestBody ClientUpdatePasswordDtoRequest dto,
            HttpServletRequest http
    ) {

        Long userId = currentUser.getUserId();

        //  validate passwords match
        if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
            throw new ValidationException("Passwords do not match");
        }

        //  call service
        clientService.changePassword(
                userId,
                dto.getCurrentPassword(),
                dto.getNewPassword()
        );

        //  response
        ApiResponse<Void> response =
                ApiResponse.success("Password changed successfully", null);

        response.setPath(http.getRequestURI());
        response.setStatus(HttpStatus.OK.value());

        logger.info("Password changed successfully for client {}", userId);

        return ResponseEntity.ok(response);
    }
}