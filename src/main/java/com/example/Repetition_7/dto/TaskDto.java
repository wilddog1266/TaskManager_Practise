package com.example.Repetition_7.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class TaskDto {

    private Long id;

    private String title;

    private String description;

    private Boolean completed;

    private Instant createdAt;

    private Instant updatedAt;
}
