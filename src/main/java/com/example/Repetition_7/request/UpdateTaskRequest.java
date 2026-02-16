package com.example.Repetition_7.request;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateTaskRequest {

    @Pattern(regexp = ".*\\S.*", message = "title must not be blank")
    private String title;

    @Pattern(regexp = ".*\\S.*", message = "description must not be blank")
    private String description;

    private Boolean completed;

    @AssertTrue(message = "At least one of title or completed or description must not be null")
    public boolean isValidRequest() {
        return (description != null && !description.isBlank()) || (title != null && !title.isBlank()) || completed != null;
    }
}
