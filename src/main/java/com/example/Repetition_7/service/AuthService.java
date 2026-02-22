package com.example.Repetition_7.service;

import com.example.Repetition_7.response.AuthResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthResponse login(String username, String password) {
        var token = new UsernamePasswordAuthenticationToken(username, password);
        authenticationManager.authenticate(token);

        String jwt = jwtService.generateAccessToken(username);
        return new AuthResponse(jwt);
    }
}
