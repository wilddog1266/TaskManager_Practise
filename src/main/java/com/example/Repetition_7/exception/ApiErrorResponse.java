package com.example.Repetition_7.exception;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class ApiErrorResponse {

    private String message;

    private Map<String, String> errors;
}
