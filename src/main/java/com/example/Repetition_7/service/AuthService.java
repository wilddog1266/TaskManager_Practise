package com.example.Repetition_7.service;

import com.example.Repetition_7.repository.UserRepository;
import com.example.Repetition_7.response.LoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Locale;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    public LoginResponse login(String username, String password) {
        var token = new UsernamePasswordAuthenticationToken(username, password);
        authenticationManager.authenticate(token);

        var user = userRepository.findByUsername(username.trim().toLowerCase())
                .orElseThrow(() -> new RuntimeException("User not found after authentication"));

        return new LoginResponse(jwtService.generateAccessToken(user));
    }
}
