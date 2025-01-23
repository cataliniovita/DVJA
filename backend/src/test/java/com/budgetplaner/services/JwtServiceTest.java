package com.budgetplaner.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Date;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtServiceTest {

    private JwtService jwtService;

    @Mock
    private UserDetails userDetails;

    private String secretKeyBase64 = "c29tZS1zZWNyZXQta2V5LWZvci10ZXN0aW5nLXB1cnBvc2Vz"; // Example valid Base64 key
    private long jwtExpiration = 1000 * 60 * 60; // 1 hour

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Create a new instance of JwtService
        jwtService = new JwtService();

        // Use reflection to set private fields
        try {
            var secretKeyField = JwtService.class.getDeclaredField("secretKey");
            secretKeyField.setAccessible(true);
            secretKeyField.set(jwtService, secretKeyBase64);

            var jwtExpirationField = JwtService.class.getDeclaredField("jwtExpiration");
            jwtExpirationField.setAccessible(true);
            jwtExpirationField.set(jwtService, jwtExpiration);
        } catch (Exception e) {
            throw new RuntimeException("Failed to set private fields in JwtService", e);
        }

        // Mock userDetails
        when(userDetails.getUsername()).thenReturn("testuser@example.com");
    }

    @Test
    void extractUsername_shouldReturnCorrectUsername() {
        String token = jwtService.generateToken(userDetails);
        String username = jwtService.extractUsername(token);

        assertEquals("testuser@example.com", username);
    }

    @Test
    void generateToken_shouldCreateValidToken() {
        String token = jwtService.generateToken(userDetails);

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void isTokenValid_shouldReturnTrueForValidToken() {
        String token = jwtService.generateToken(userDetails);

        boolean isValid = jwtService.isTokenValid(token, userDetails);

        assertTrue(isValid);
    }

    @Test
    void isTokenValid_shouldReturnFalseForExpiredToken() throws Exception {
        // Use reflection to set the jwtExpiration field to a short duration
        Field expirationField = JwtService.class.getDeclaredField("jwtExpiration");
        expirationField.setAccessible(true);
        expirationField.set(jwtService, 100L); // 100 ms expiration

        // Generate a token with the short expiration time
        String token = jwtService.generateToken(new HashMap<>(), userDetails);

        // Wait for the token to expire
        Thread.sleep(150);

        // Validate the token
        boolean isValid;
        try {
            isValid = jwtService.isTokenValid(token, userDetails);
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            isValid = false; // Token expired as expected
        }

        // Reset the jwtExpiration field to its original value (optional, for other tests)
        expirationField.set(jwtService, 3600000L); // Replace with the default value in your config

        // Assert that the token is expired
        assertFalse(isValid);
    }

    @Test
    void generateToken_withExtraClaims_shouldIncludeClaims() {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", "ADMIN");

        String token = jwtService.generateToken(extraClaims, userDetails);

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(io.jsonwebtoken.io.Decoders.BASE64.decode(secretKeyBase64)) // Proper Base64 decoding
                .build()
                .parseClaimsJws(token)
                .getBody();

        assertEquals("ADMIN", claims.get("role"));
    }

    @Test
    void extractClaim_shouldReturnCorrectClaim() {
        String token = jwtService.generateToken(userDetails);
        Date issuedAt = jwtService.extractClaim(token, Claims::getIssuedAt);

        assertNotNull(issuedAt);
        assertTrue(issuedAt.before(new Date()));
    }
}