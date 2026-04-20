package com.alexsysSolutions.alexsis.controller;

import com.alexsysSolutions.alexsis.dto.request.auth.LoginDtoRequest;
import com.alexsysSolutions.alexsis.dto.response.auth.AuthDtoResponse;
import com.alexsysSolutions.alexsis.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication Management", description = "Authentication endpoints for login, logout, and token refresh")
public class AuthController {

    private final AuthService authService;


    @PostMapping("/login")
    @Operation(summary = "User Login", description = "Authenticate user with email and password, returns access and refresh tokens")
    public ResponseEntity<AuthDtoResponse> login(
            @RequestBody LoginDtoRequest request
    ) {
        return ResponseEntity.ok(authService.login(request));
    }

   // st: Refresh token endpoint - generate new access token using refresh token
    @PostMapping("/refresh")
    @Operation(summary = "Refresh Access Token", description = "Generate new access token using valid refresh token")
    public ResponseEntity<AuthDtoResponse> refreshToken(
            @RequestHeader("Authorization") String refreshToken
    ) {
        // remove "Bearer " prefix
        String token = refreshToken.substring(7);

        return ResponseEntity.ok(authService.refreshToken(token));
    }


    @PostMapping("/logout")
    @Operation(summary = "User Logout", description = "Invalidate refresh token and logout user")
    public ResponseEntity<Void> logout(
            @RequestHeader("Authorization") String refreshToken
    ) {
        String token = refreshToken.substring(7);
        authService.logout(token);

        return ResponseEntity.noContent().build();
    }
}