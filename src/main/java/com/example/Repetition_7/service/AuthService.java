package com.example.Repetition_7.service;

import com.example.Repetition_7.exception.InvalidRefreshTokenException;
import com.example.Repetition_7.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final RefreshTokenService refreshTokenService;

    public LoginResult login(String username, String password) {
        String normalized = username.trim().toLowerCase();

        var token = new UsernamePasswordAuthenticationToken(normalized, password);
        authenticationManager.authenticate(token);

        var user = userRepository.findByUsername(normalized)
                .orElseThrow(() -> new IllegalArgumentException("User not found after authentication"));
        String accessToken = jwtService.generateAccessToken(user.getId(), user.getUsername(), user.getRole());
        String refreshTokenPlain = refreshTokenService.issue(user);

        return new LoginResult(accessToken, refreshTokenPlain);
    }

    public LoginResult refresh(String refreshPlain) {
        if(refreshPlain == null || refreshPlain.isBlank()) {
            throw new InvalidRefreshTokenException();
        }

        var rotateResult = refreshTokenService.rotate(refreshPlain);

        String newAccessToken = jwtService.generateAccessToken(rotateResult.userId(), rotateResult.username(), rotateResult.role());

        return new LoginResult(newAccessToken, rotateResult.newPlain());
    }

    public record LoginResult(String accessToken, String refreshTokenPlain) {}
}
