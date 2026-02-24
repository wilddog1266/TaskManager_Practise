package com.example.Repetition_7.specification;

import com.example.Repetition_7.entity.UserEntity;
import org.springframework.data.jpa.domain.Specification;

public final class UserSpecification {

    private UserSpecification() {
    }

    public static Specification<UserEntity> usernameContains(String query) {
        if(query == null || query.isBlank()) {
            return null;
        }

        String pattern = "%" + query.toLowerCase() + "%";

        return (root, queryObject, cb) ->
                cb.like(cb.coalesce(cb.lower(root.get("username")), ""), pattern);
    }

    public static Specification<UserEntity> idEqual(Long userId) {
        if(userId == null || userId <= 0) {
            return null;
        }

        return (root, query, cb) ->
                cb.equal(root.get("id"), userId);
    }
}
