package com.example.Repetition_7.service;

import com.example.Repetition_7.response.LoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Locale;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public LoginResponse login(String username, String password) {
        String u = username.toLowerCase().trim();

        var token = new UsernamePasswordAuthenticationToken(u, password);
        authenticationManager.authenticate(token);

        String jwt = jwtService.generateAccessToken(u);
        return new LoginResponse(jwt);
    }
}
