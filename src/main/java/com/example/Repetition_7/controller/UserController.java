package com.example.Repetition_7.controller;

import com.example.Repetition_7.request.CreateUserRequest;
import com.example.Repetition_7.response.UserResponse;
import com.example.Repetition_7.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Tag(name = "Users", description = "Operations related to user management")
@Validated
public class UserController {

    private final UserService userRegisterService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerNewUser(@Valid @RequestBody CreateUserRequest request) {
        UserResponse userResponse = userRegisterService.register(request);

        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser() {
        UserResponse currentUser = userRegisterService.getCurrentUser();
        return new ResponseEntity<>(currentUser, HttpStatus.OK);
    }
}
