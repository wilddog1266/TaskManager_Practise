package com.example.Repetition_7.service;

import com.example.Repetition_7.entity.UserEntity;
import com.example.Repetition_7.entity.roles.UserRole;
import com.example.Repetition_7.exception.UserAlreadyExistsException;
import com.example.Repetition_7.mapper.UserMapper;
import com.example.Repetition_7.repository.UserRepository;
import com.example.Repetition_7.request.CreateUserRequest;
import com.example.Repetition_7.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CurrentUserService currentUserService;

    public UserResponse register(CreateUserRequest request) {
        String currentUsername = request.getUsername().trim().toLowerCase();

        if(userRepository.existsByUsername(currentUsername)) {
            throw new UserAlreadyExistsException(currentUsername);
        }

        String passwordHash = passwordEncoder.encode(request.getPassword());

        UserEntity newUser = new UserEntity();
        newUser.setRole(UserRole.USER);
        newUser.setUsername(currentUsername);
        newUser.setPasswordHash(passwordHash);

        UserEntity saved = userRepository.save(newUser);

        return userMapper.toResponse(saved);
    }

    public UserResponse getCurrentUser() {
        UserEntity currentUser = currentUserService.getCurrentUser();

        return userMapper.toResponse(currentUser);
    }
}
