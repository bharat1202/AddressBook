package org.example.addressbook;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    // Note: This key is generated at application startup.
    // For persistent tokens across restarts, consider using a fixed secret loaded from properties.
    private final SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // Generates a JWT token for the given username with a 1-hour expiration.
    public String generateToken(String username) {
        long expirationMillis = 1000 * 60 * 60; // 1 hour
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationMillis);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // Extracts the username (subject) from the JWT token.
    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    // Validates the JWT token; returns true if valid, false otherwise.
    public boolean validateToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (JwtException e) {
            // You can log the exception here for debugging purposes.
            return false;
        }
    }

    // Helper method to parse and retrieve claims from the token.
    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
