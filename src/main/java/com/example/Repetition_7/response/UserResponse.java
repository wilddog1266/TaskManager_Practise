package com.example.Repetition_7.response;

import com.example.Repetition_7.entity.roles.UserRole;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class UserResponse {

    private Long id;

    private String username;

    private UserRole role;

    private Instant createdAt;
}
