package com.example.Repetition_7.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateTaskRequest {

    @NotBlank
    private String title;

    @Pattern(regexp = ".*\\S.*", message = "description must not be blank")
    private String description;

    private Boolean completed;
}
