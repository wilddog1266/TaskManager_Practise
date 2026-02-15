package com.example.Repetition_7.service;

import com.example.Repetition_7.dto.TaskDto;
import com.example.Repetition_7.entity.TaskEntity;
import com.example.Repetition_7.exception.TaskNotFoundException;
import com.example.Repetition_7.mapper.TaskMapper;
import com.example.Repetition_7.repository.TaskRepository;
import com.example.Repetition_7.request.CreateTaskRequest;
import com.example.Repetition_7.request.UpdateTaskRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    public TaskDto create(CreateTaskRequest request) {
        TaskEntity entity = taskMapper.toEntity(request);

        TaskEntity saved = taskRepository.save(entity);

        return taskMapper.toDto(saved);
    }

    public TaskDto getById(Long id) {
        return taskRepository.findById(id).map(taskMapper::toDto).orElseThrow(() -> new TaskNotFoundException(id));
    }

    public TaskDto updateTask(Long id, UpdateTaskRequest request) {

        TaskEntity taskEntity = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));

        if(request.getCompleted() != null) {
            taskEntity.setCompleted(request.getCompleted());
        }

        if(request.getTitle() != null && !request.getTitle().trim().isEmpty()) {
            taskEntity.setTitle(request.getTitle().trim());
        }

        TaskEntity saved = taskRepository.save(taskEntity);

        return taskMapper.toDto(saved);
    }

    public void delete(Long id) {
        if(taskRepository.existsById(id)) {
            taskRepository.deleteById(id);
            return;
        }

        throw new TaskNotFoundException(id);
    }

    public Page<TaskDto> search(Pageable pageable, Boolean completed, String query) {
        String q = (query == null || query.trim().isEmpty()) ? null : query.trim();

        if(q == null && completed == null) {
            return taskRepository.findAll(pageable).map(taskMapper::toDto);
        }

        if(q != null && completed == null) {
            return taskRepository.findByTitleContainingIgnoreCase(q, pageable).map(taskMapper::toDto);
        }

        if(q == null) {
            return taskRepository.findByCompleted(completed, pageable).map(taskMapper::toDto);
        }

        return taskRepository.findByTitleContainingIgnoreCaseAndCompleted(q, completed, pageable).map(taskMapper::toDto);
    }
}
