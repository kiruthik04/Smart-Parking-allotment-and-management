package com.smartparking.smart_parking_backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

public class JwtUtil {

    // 🔐 MUST be at least 256 bits for HS256
    private static final Key SECRET_KEY =
            Keys.hmacShaKeyFor(
                    "smartparking_super_secure_secret_key_123456".getBytes()
            );

    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 24; // 24 hours

    // -------------------------------
    // Generate JWT
    // -------------------------------
    public static String generateToken(Long userId) {

        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    // -------------------------------
    // Extract userId from JWT
    // -------------------------------
    public static Long extractUserId(String token) {

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return Long.parseLong(claims.getSubject());
    }
}
