package com.example.Repetition_7.validation;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class PageableValidator {

    private static final int MAX_PAGE_SIZE = 50;

    private static final Set<String> ALLOWED_SORT_FIELDS = Set.of("id", "title", "completed", "createdAt", "updatedAt");

    public void validate(Pageable pageable) {
        if(pageable.getPageSize() > MAX_PAGE_SIZE) {
            throw new IllegalArgumentException("Page size must be <= 50");
        }

        pageable.getSort().forEach(order -> {
            String field = order.getProperty();
            if(!ALLOWED_SORT_FIELDS.contains(field)) {
                throw new IllegalArgumentException("Sorting by '" + field + "' is not allowed");
            }
        });
    }

}
