package com.example.Repetition_7.controller;

import com.example.Repetition_7.request.LoginRequest;
import com.example.Repetition_7.response.LoginResponse;
import com.example.Repetition_7.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.Instant;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    @Value("${app.jwt.refresh-ttl-days}")
    private long ttl;

    @PostMapping(value = "/login",
    consumes =  "application/json",
    produces = "application/json")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest, HttpServletResponse httpServletResponse) {
        var result = authService.login(loginRequest.getUsername(), loginRequest.getPassword());
        ResponseCookie cookie = ResponseCookie
                .from("refreshToken", result.refreshTokenPlain())
                .httpOnly(true)
                .secure(false)
                .path("/api/auth")
                .maxAge(Duration.ofDays(ttl))
                .sameSite("Lax")
                .build();

        httpServletResponse.addHeader("Set-Cookie", cookie.toString());

        return ResponseEntity.ok(new LoginResponse(result.accessToken()));
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refresh(@CookieValue(name = "refreshToken", required = false) String refreshToken, HttpServletResponse httpServletResponse) {
        var result = authService.refresh(refreshToken);
        ResponseCookie cookie = ResponseCookie
                .from("refreshToken", result.refreshTokenPlain())
                .httpOnly(true)
                .secure(false)
                .path("/api/auth")
                .maxAge(Duration.ofDays(ttl))
                .sameSite("Lax")
                .build();

        httpServletResponse.addHeader("Set-Cookie", cookie.toString());

        return ResponseEntity.ok(new LoginResponse(result.accessToken()));
    }
}
