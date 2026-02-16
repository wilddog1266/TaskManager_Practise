package com.example.Repetition_7.specification;

import com.example.Repetition_7.entity.TaskEntity;
import org.springframework.data.jpa.domain.Specification;

public final class TaskSpecification {

    private TaskSpecification() {};

    public static Specification<TaskEntity> hasCompleted(Boolean completed) {
        if(completed == null) {
            return null;
        }

        return (root, queryObject, cb) ->
                cb.equal(root.get("completed"), completed);
    }

    public static Specification<TaskEntity> descriptionContains(String query) {
        if(query == null || query.isBlank()) {
            return null;
        }

        String pattern = "%" + query.toLowerCase() + "%";

        return (root, queryObject, cb) ->
                cb.like(cb.coalesce(cb.lower(root.get("description")), ""), pattern);
    }

    public static Specification<TaskEntity> titleContains(String query) {
        if(query == null || query.isBlank()) {
            return null;
        }
        String pattern = "%" + query.toLowerCase() + "%";

        return (root, queryObject, cb) ->
                cb.like(cb.lower(root.get("title")), pattern);
    }
}
