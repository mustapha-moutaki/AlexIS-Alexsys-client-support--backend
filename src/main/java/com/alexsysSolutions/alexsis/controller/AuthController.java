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


    // LOGIN

    @PostMapping("/login")
    public ResponseEntity<AuthDtoResponse> login(
            @RequestBody LoginDtoRequest request,
            HttpServletResponse response
    ) {

        AuthDtoResponse authResponse = authService.login(request);

        ResponseCookie cookie = ResponseCookie.from("refreshToken", authResponse.getRefreshToken())
                .httpOnly(true)
                .secure(false) // true in production (HTTPS)
                .path("/api/v1/auth")
                .maxAge(7 * 24 * 60 * 60)
                .sameSite("Lax")
                .build();

        response.addHeader("Set-Cookie", cookie.toString());

        authResponse.setRefreshToken(null); // because we store it in the cookies httpOnly

        return ResponseEntity.ok(authResponse);
    }


    // REFRESH TOKEN

    @PostMapping("/refresh")
    public ResponseEntity<AuthDtoResponse> refreshToken(HttpServletRequest request) {

        String refreshToken = extractRefreshToken(request);

        return ResponseEntity.ok(authService.refreshToken(refreshToken));
    }


    // LOGOUT

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {

        String refreshToken = extractRefreshToken(request);

        if (refreshToken != null) {
            authService.logout(refreshToken);
        }

        // clear cookie
        ResponseCookie cookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(false)
                .path("/api/v1/auth")
                .maxAge(0)
                .build();

        response.addHeader("Set-Cookie", cookie.toString());

        return ResponseEntity.noContent().build();
    }


    // HELPER

    private String extractRefreshToken(HttpServletRequest request) {
        if (request.getCookies() == null) return null;

        for (Cookie cookie : request.getCookies()) {
            if ("refreshToken".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }
}