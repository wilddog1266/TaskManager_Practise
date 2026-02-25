package com.example.Repetition_7.service;

import com.example.Repetition_7.entity.RefreshTokenEntity;
import com.example.Repetition_7.entity.UserEntity;
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


}
