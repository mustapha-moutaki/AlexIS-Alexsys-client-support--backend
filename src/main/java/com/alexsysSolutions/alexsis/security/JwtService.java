package com.alexsysSolutions.alexsis.security;

import com.alexsysSolutions.alexsis.config.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Getter
@Setter
@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtProperties jwtProperties;
    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);

    // st: Create JWT token with user details
    public String generateToken(UserDetails user) {
        logger.info("st: Generating JWT token for user: {}", user.getUsername());

        String token = Jwts.builder()
                .setSubject(user.getUsername())
                .claim("roles", user.getAuthorities())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()
                        + jwtProperties.getAccessTokenExpiration()))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();

        logger.debug("st: JWT token generated successfully for user: {}", user.getUsername());
        return token;
    }

    // st: Extract username from JWT token
    public String extractUsername(String token) {
        logger.debug("st: Extracting username from JWT token");
        return extractAllClaims(token).getSubject();
    }

    // st: Validate JWT token against user details
    public boolean isValid(String token, UserDetails user) {
        logger.debug("st: Validating JWT token for user: {}", user.getUsername());
        try {
            boolean isValid = extractUsername(token).equals(user.getUsername())
                    && !isTokenExpired(token);

            if (isValid) {
                logger.debug("st: Token is valid for user: {}", user.getUsername());
            } else {
                logger.warn("st: Token validation failed for user: {}", user.getUsername());
            }
            return isValid;
        } catch (Exception e) {
            logger.error("st: Token validation error for user: {}", user.getUsername(), e);
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        boolean expired = extractAllClaims(token)
                .getExpiration()
                .before(new Date());

        if (expired) {
            logger.warn("st: Token has expired");
        }
        return expired;
    }

    private Claims extractAllClaims(String token) {
        logger.debug("st: Extracting all claims from JWT token");
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            logger.error("st: JWT token has expired: {}", e.getMessage());
            throw e;
        } catch (UnsupportedJwtException e) {
            logger.error("st: JWT token is unsupported: {}", e.getMessage());
            throw e;
        } catch (MalformedJwtException e) {
            logger.error("st: Invalid JWT token: {}", e.getMessage());
            throw e;
        } catch (SignatureException e) {
            logger.error("st: JWT signature validation failed: {}", e.getMessage());
            throw e;
        } catch (IllegalArgumentException e) {
            logger.error("st: JWT claims string is empty: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("st: Unexpected error while parsing JWT token: {}", e.getMessage());
            throw e;
        }
    }

    // Generate signing key with robust Base64 handling for both standard and URL-safe Base64
    private Key getSigningKey() {
        String secret = jwtProperties.getSecret();

        // st: Use default dev secret if not configured
        if (secret == null || secret.trim().isEmpty() || secret.length() < 32) {
            logger.warn("st: JWT_SECRET not properly configured. Using development default secret.");
            secret = "MyDevelopmentJwtSecretKeyIsAtLeast32BytesLongForTesting123456789";
        }

        logger.debug("st: JwtService.getSigningKey() - Processing secret with length: {}", secret.length());

        // ...existing code...
        // Check if secret contains URL-safe Base64 characters (- and _)
        boolean hasUrlSafeChars = secret.contains("-") || secret.contains("_");
        logger.debug("st: Secret contains URL-safe Base64 characters: {}", hasUrlSafeChars);

        byte[] keyBytes = null;

        // st: First, try URL-safe Base64 if it contains URL-safe characters
        if (hasUrlSafeChars) {
            try {
                logger.debug("st: Attempting URL-safe Base64 decoding");
                keyBytes = Decoders.BASE64URL.decode(secret);
                logger.info("st: Successfully decoded secret as URL-safe Base64");
            } catch (IllegalArgumentException e) {
                logger.warn("st: URL-safe Base64 decoding failed: {}", e.getMessage());
                keyBytes = null;
            }
        }

        // st: If URL-safe decoding failed or no URL-safe chars, try standard Base64
        if (keyBytes == null) {
            try {
                logger.debug("st: Attempting standard Base64 decoding");
                keyBytes = Decoders.BASE64.decode(secret);
                logger.info("st: Successfully decoded secret as standard Base64");
            } catch (IllegalArgumentException e) {
                logger.warn("st: Standard Base64 decoding failed: {}", e.getMessage());
                keyBytes = null;
            }
        }

        // st: If both Base64 decodings fail, treat as plain text (fallback - not recommended for production)
        if (keyBytes == null) {
            logger.warn("st: Base64 decoding failed, treating secret as plain text UTF-8");
            keyBytes = secret.getBytes(java.nio.charset.StandardCharsets.UTF_8);
        }

        // st: Validate key length for HMAC-SHA256 (minimum 256 bits = 32 bytes)
        if (keyBytes.length < 32) {
            String errorMsg = String.format(
                "st: JWT secret is too short for HMAC-SHA256 security. " +
                "Current: %d bytes, Required: 32 bytes (256 bits). " +
                "Set JWT_SECRET environment variable or use: " +
                "openssl rand -base64 32",
                keyBytes.length
            );
            logger.error(errorMsg);
            throw new IllegalArgumentException(errorMsg);
        }

        logger.debug("st: Key generated successfully with length: {} bytes", keyBytes.length);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}

