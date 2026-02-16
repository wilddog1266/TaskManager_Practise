package com.example.Repetition_7.mapper;

import com.example.Repetition_7.dto.TaskDto;
import com.example.Repetition_7.entity.TaskEntity;
import com.example.Repetition_7.request.CreateTaskRequest;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {

    public TaskDto toDto(TaskEntity e) {
        TaskDto taskDto = new TaskDto();

        taskDto.setId(e.getId());
        taskDto.setTitle(e.getTitle());
        taskDto.setCompleted(e.isCompleted());
        taskDto.setDescription(e.getDescription());
        taskDto.setCreatedAt(e.getCreatedAt());
        taskDto.setUpdatedAt(e.getUpdatedAt());

        return taskDto;
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
