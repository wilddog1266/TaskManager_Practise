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

        return taskDto;
    }

    public TaskEntity toEntity(CreateTaskRequest r) {
        TaskEntity taskEntity = new TaskEntity();

        taskEntity.setTitle(r.getTitle().trim());
        taskEntity.setCompleted(r.getCompleted() != null ? r.getCompleted() : false);

        return taskEntity;
    }
}
