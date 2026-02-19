package com.example.Repetition_7.mapper;

import com.example.Repetition_7.entity.UserEntity;
import com.example.Repetition_7.response.UserResponse;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponse toResponse(UserEntity e) {
        UserResponse response = new UserResponse();

        response.setId(e.getId());
        response.setUsername(e.getUsername());
        response.setRole(e.getRole());
        response.setCreatedAt(e.getCreatedAt());

        return response;
    }
}
