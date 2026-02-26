package com.example.Repetition_7.service;

import com.example.Repetition_7.entity.UserEntity;
import com.example.Repetition_7.entity.roles.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    private final SecretKey secretKey;
    private final long ttlMills;
    private final String issuer;

    public JwtService(@Value("${app.jwt.secret}") String base64Secret,
                      @Value("${app.jwt.access-ttl-minutes}") long ttlMinutes,
                      @Value("${app.jwt.issuer}") String issuer) {

        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(base64Secret));
        this.ttlMills = ttlMinutes * 60_000L;
        this.issuer = issuer;
    }

    public String generateAccessToken(Long userId, String username, UserRole role) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .subject(username)
                .claim("uid", userId)
                .claim("role", role.name())
                .issuedAt(new Date(now))
                .expiration(new Date(now + ttlMills))
                .issuer(issuer)
                .signWith(secretKey)
                .compact();
    }


    public String extractUsername(String token) {
        return parseClaims(token).getSubject();
    }

    public boolean isValid(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .requireIssuer(issuer)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String extractRole(String token) {
        return parseClaims(token).get("role", String.class);
    }

    public Long extractUserId(String token) {
        Object v = parseClaims(token).get("uid");
        if(v instanceof Integer i) return i.longValue();
        if(v instanceof Long l) return l;
        if(v instanceof String str) return Long.parseLong(str);
        throw new IllegalArgumentException("Invalid uid in token");
    }
}
