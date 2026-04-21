package com.alexsysSolutions.alexsis.controller;

import com.alexsysSolutions.alexsis.dto.request.auth.LoginDtoRequest;
import com.alexsysSolutions.alexsis.dto.response.auth.AuthDtoResponse;
import com.alexsysSolutions.alexsis.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication Management", description = "Authentication endpoints for login, logout, and token refresh")
public class AuthController {

    private final AuthService authService;


    @PostMapping("/login")
    @Operation(summary = "User Login")
    public ResponseEntity<AuthDtoResponse> login(
            @RequestBody LoginDtoRequest request,
            HttpServletResponse response
    ) {

        AuthDtoResponse authResponse = authService.login(request);

        ResponseCookie cookie = ResponseCookie.from("refreshToken", authResponse.getRefreshToken())
                .httpOnly(true)
                .secure(false)
                .path("/api/v1/auth/refresh")
                .maxAge(7 * 24 * 60 * 60) // 7 days
                .sameSite("Lax")
                .build();

        response.addHeader("Set-Cookie", cookie.toString());

        // to avoid returning refresh token in the body
        authResponse.setRefreshToken(null);

        return ResponseEntity.ok(authResponse);
    }

   // Refresh token endpoint - generate new access token using refresh token
   @PostMapping("/refresh")
   @Operation(summary = "Refresh Access Token")
   public ResponseEntity<AuthDtoResponse> refreshToken(
           HttpServletRequest request
   ) {

       String refreshToken = null;

       if (request.getCookies() != null) {
           for (Cookie cookie : request.getCookies()) {
               if ("refreshToken".equals(cookie.getName())) {
                   refreshToken = cookie.getValue();
               }
           }
       }

       if (refreshToken == null) {
           throw new RuntimeException("Refresh token not found");
       }

       return ResponseEntity.ok(authService.refreshToken(refreshToken));
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