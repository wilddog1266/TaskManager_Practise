package com.example.Repetition_7.controller;

import com.example.Repetition_7.request.LoginRequest;
import com.example.Repetition_7.response.AuthResponse;
import com.example.Repetition_7.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        var response = authService.login(
                loginRequest.getUsername().trim().toLowerCase(),
                loginRequest.getPassword()
        );

        return ResponseEntity.ok(response);
    }
}
