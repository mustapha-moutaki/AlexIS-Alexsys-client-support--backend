package com.alexsysSolutions.alexsis.service.impl;

import com.alexsysSolutions.alexsis.dto.request.auth.LoginDtoRequest;
import com.alexsysSolutions.alexsis.dto.response.auth.AuthDtoResponse;
import com.alexsysSolutions.alexsis.exception.ResourceNotFoundException;
import com.alexsysSolutions.alexsis.mapper.UserMapper;
import com.alexsysSolutions.alexsis.model.RefreshToken;
import com.alexsysSolutions.alexsis.model.User;
import com.alexsysSolutions.alexsis.reposiotry.RefreshTokenRepository;
import com.alexsysSolutions.alexsis.reposiotry.UserRepository;
import com.alexsysSolutions.alexsis.security.CustomUserDetails;
import com.alexsysSolutions.alexsis.security.JwtService;
import com.alexsysSolutions.alexsis.service.AuthService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserMapper userMapper;
    @Override
    public AuthDtoResponse login(LoginDtoRequest dto) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        dto.getEmail(),
                        dto.getPassword()
                )
        );

        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String accessToken = jwtService.generateToken(new CustomUserDetails(user));

        RefreshToken refreshToken = createRefreshToken(user);

        return AuthDtoResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .user(userMapper.toDto(user))
                .build();
    }

@Override
public AuthDtoResponse refreshToken(String refreshToken) {

    System.out.println("Refresh token received: " + refreshToken);

    RefreshToken storedToken = refreshTokenRepository.findByToken(refreshToken)
            .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

    System.out.println("Stored token found");
    System.out.println("Revoked: " + storedToken.isRevoked());
    System.out.println("Expires at: " + storedToken.getExpiryDate());

    if (storedToken.isRevoked()) {
        System.out.println("Refresh token rejected: revoked");
        return null;
    }

    if (storedToken.getExpiryDate().isBefore(LocalDateTime.now())) {
        System.out.println("Refresh token rejected: expired");
        return null;
    }

    User user = userRepository.findByEmail(storedToken.getEmail())
            .orElseThrow(() -> new RuntimeException("User not found"));

    storedToken.setRevoked(true);
    refreshTokenRepository.save(storedToken);

    String newAccessToken = jwtService.generateToken(new CustomUserDetails(user));
    RefreshToken newRefreshToken = createRefreshToken(user);

    System.out.println("New refresh token created: " + newRefreshToken.getToken());

    return AuthDtoResponse.builder()
            .accessToken(newAccessToken)
            .refreshToken(newRefreshToken.getToken())
            .user(userMapper.toDto(user))
            .build();
}
    @Override
    public void logout(String refreshToken) {

        RefreshToken token = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid refresh token"));

        if (token.isRevoked()) {
            return;
        }

        token.setRevoked(true);
        token.setExpiryDate(LocalDateTime.now());

        refreshTokenRepository.save(token);
    }

    // =========================
    // PRIVATE HELPERS
    // =========================

    private RefreshToken createRefreshToken(User user) {
        RefreshToken token = new RefreshToken();
        token.setToken(java.util.UUID.randomUUID().toString());
        token.setEmail(user.getEmail());
        token.setRevoked(false);
        token.setExpiryDate(java.time.LocalDateTime.now().plusDays(7));

        return refreshTokenRepository.save(token);
    }

}