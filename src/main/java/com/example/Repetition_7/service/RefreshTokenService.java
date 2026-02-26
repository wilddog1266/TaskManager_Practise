package com.example.Repetition_7.service;

import com.example.Repetition_7.entity.RefreshTokenEntity;
import com.example.Repetition_7.entity.UserEntity;
import com.example.Repetition_7.entity.roles.UserRole;
import com.example.Repetition_7.exception.InvalidRefreshTokenException;
import com.example.Repetition_7.repository.RefreshTokenRepository;
import com.example.Repetition_7.security.RefreshTokenCodec;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final RefreshTokenCodec refreshTokenCodec;
    private final Clock clock;
    @Value("${app.jwt.refresh-ttl-days}")
    private long refreshDays;

    @Transactional
    public String issue(UserEntity user) {
        if(user == null) {
            throw new IllegalArgumentException("User must not be null");
        }

        Instant now = Instant.now(clock);
        String plain = refreshTokenCodec.generatePlain();
        String hash = refreshTokenCodec.hash(plain);

        RefreshTokenEntity refreshToken = new RefreshTokenEntity();
        refreshToken.setUser(user);
        refreshToken.setTokenHash(hash);
        refreshToken.setCreatedAt(now);
        refreshToken.setExpiresAt(now.plus(Duration.ofDays(refreshDays)));

        refreshTokenRepository.save(refreshToken);

        return plain;
    }

    @Transactional
    public RotateResult rotate(String oldPlain) {
        if(oldPlain == null || oldPlain.isBlank()) {
            throw new InvalidRefreshTokenException();
        }

        Instant now = Instant.now(clock);
        String oldHash = refreshTokenCodec.hash(oldPlain);
        RefreshTokenEntity token = refreshTokenRepository.findByTokenHash(oldHash)
                .orElseThrow(InvalidRefreshTokenException::new);

        if(!token.isActive(now)) {
            throw new InvalidRefreshTokenException();
        }

        UserEntity user = token.getUser();

        String newPlain = refreshTokenCodec.generatePlain();
        String newHash = refreshTokenCodec.hash(newPlain);

        token.setRevokedAt(now);
        token.setReplacedByTokenHash(newHash);

        RefreshTokenEntity refreshToken = new RefreshTokenEntity();
        refreshToken.setUser(user);
        refreshToken.setTokenHash(newHash);
        refreshToken.setCreatedAt(now);
        refreshToken.setExpiresAt(now.plus(Duration.ofDays(refreshDays)));

        refreshTokenRepository.save(token);
        refreshTokenRepository.save(refreshToken);
        return new RotateResult(user.getId(), user.getUsername(), user.getRole(), newPlain);
    }

    public record RotateResult(Long userId, String username, UserRole role, String newPlain) {}

}
