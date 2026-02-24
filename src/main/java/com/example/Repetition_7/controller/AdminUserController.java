package com.example.Repetition_7.controller;

import com.example.Repetition_7.response.UserResponse;
import com.example.Repetition_7.service.UserService;
import com.example.Repetition_7.validation.UserPageableValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/users")
public class AdminUserController {

    private final UserService userService;
    private final UserPageableValidator userPageableValidator;

    @GetMapping
    public Page<UserResponse> getAll(@PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                                     @RequestParam(required = false) String username,
                                     @RequestParam (required = false) Long userId) {

        userPageableValidator.validate(pageable);

        return userService.search(pageable, userId, username);
    }
}
