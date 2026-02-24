package com.example.Repetition_7.service;

import com.example.Repetition_7.entity.UserEntity;
import com.example.Repetition_7.entity.roles.UserRole;
import com.example.Repetition_7.exception.UserAlreadyExistsException;
import com.example.Repetition_7.mapper.UserMapper;
import com.example.Repetition_7.repository.UserRepository;
import com.example.Repetition_7.request.CreateUserRequest;
import com.example.Repetition_7.response.UserResponse;
import com.example.Repetition_7.specification.UserSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public Page<UserResponse> search(Pageable pageable, Long userId, String query) {
        UserEntity currentUser = currentUserService.getCurrentUser();

        String q = (query == null || query.isBlank()) ? null : query.trim();

        Specification<UserEntity> spec = (root, queryObject, cb) -> cb.conjunction();

        var byUserId = UserSpecification.idEqual(userId);
        if(byUserId != null) spec = spec.and(byUserId);

        var byUsername = UserSpecification.usernameContains(q);
        if(byUsername != null) spec = spec.and(byUsername);

        return userRepository.findAll(spec, pageable).map(userMapper::toResponse);
    }
}
