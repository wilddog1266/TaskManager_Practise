package com.example.Repetition_7.mapper;

import com.example.Repetition_7.response.TaskResponse;
import com.example.Repetition_7.entity.TaskEntity;
import com.example.Repetition_7.request.CreateTaskRequest;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class TaskMapper {

    public TaskResponse toResponse(TaskEntity e) {
        TaskResponse taskResponse = new TaskResponse();

        taskResponse.setId(e.getId());
        taskResponse.setTitle(e.getTitle());
        taskResponse.setCompleted(e.isCompleted());
        taskResponse.setDescription(e.getDescription());
        taskResponse.setCreatedAt(e.getCreatedAt());
        taskResponse.setUpdatedAt(e.getUpdatedAt());
        var createdBy = Objects.requireNonNull(e.getCreatedBy(), "Task.createdBy is null");
        taskResponse.setCreatedByUserId(Objects.requireNonNull(createdBy.getId(), "Task.createdBy.id is null"));

        return taskResponse;
    }

    public TaskEntity toEntity(CreateTaskRequest r) {
        TaskEntity taskEntity = new TaskEntity();

        taskEntity.setTitle(r.getTitle().trim());
        taskEntity.setDescription(normalise(r.getDescription()));
        taskEntity.setCompleted(r.getCompleted() != null ? r.getCompleted() : false);

        return taskEntity;
    }

    private String normalise(String description) {
        return description == null ? null : description.trim();
    }
}
