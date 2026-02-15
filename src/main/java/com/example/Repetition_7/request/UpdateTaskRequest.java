package com.example.Repetition_7.request;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateTaskRequest {

    @Pattern(regexp = ".*\\S.*", message = "must not be blank")
    private String title;

    private Boolean completed;

    @AssertTrue(message = "At least one of title or completed must not be null")
    public boolean isValidRequest() {
        return (title != null && !title.isBlank()) || completed != null;
    }
}
