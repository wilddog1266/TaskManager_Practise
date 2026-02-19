package com.example.Repetition_7.service;

import com.example.Repetition_7.response.TaskResponse;
import com.example.Repetition_7.entity.TaskEntity;
import com.example.Repetition_7.entity.UserEntity;
import com.example.Repetition_7.entity.roles.UserRole;
import com.example.Repetition_7.exception.TaskNotFoundException;
import com.example.Repetition_7.mapper.TaskMapper;
import com.example.Repetition_7.repository.TaskRepository;
import com.example.Repetition_7.request.CreateTaskRequest;
import com.example.Repetition_7.request.UpdateTaskRequest;
import com.example.Repetition_7.specification.TaskSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final CurrentUserService currentUserService;

    public TaskResponse create(CreateTaskRequest request) {
        TaskEntity entity = taskMapper.toEntity(request);

        UserEntity currentUser = currentUserService.getCurrentUser();
        entity.setCreatedBy(currentUser);

        TaskEntity saved = taskRepository.save(entity);

        return taskMapper.toResponse(saved);
    }

    @PreAuthorize("@taskSecurity.canAccessTask(#id)")
    public TaskResponse getById(Long id) {

        TaskEntity task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));

        return taskMapper.toResponse(task);
    }

    @PreAuthorize("@taskSecurity.canModifyTask(#id)")
    public TaskResponse updateTask(Long id, UpdateTaskRequest request) {

        TaskEntity taskEntity = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));

        if(request.getCompleted() != null) {
            taskEntity.setCompleted(request.getCompleted());
        }

        if(request.getDescription() != null && !request.getDescription().trim().isEmpty()) {
            taskEntity.setDescription(request.getDescription().trim());
        }

        if(request.getTitle() != null && !request.getTitle().trim().isEmpty()) {
            taskEntity.setTitle(request.getTitle().trim());
        }

        TaskEntity saved = taskRepository.save(taskEntity);

        return taskMapper.toResponse(saved);
    }

    @PreAuthorize("@taskSecurity.canModifyTask(#id)")
    public void delete(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new TaskNotFoundException(id);
        }
        taskRepository.deleteById(id);
    }

    public Page<TaskResponse> search(Pageable pageable, Boolean completed, String query, String description) {
        UserEntity currentUser = currentUserService.getCurrentUser();

        String q = (query == null || query.isBlank()) ? null : query.trim();
        String d = (description == null || description.isBlank()) ? null : description.trim();

        Specification<TaskEntity> spec = (root, queryObj, cb) -> cb.conjunction();

        if (currentUser.getRole() != UserRole.ADMIN) {
            spec = spec.and(TaskSpecification.createdByUserId(currentUser.getId()));
        }

        var byTitle = TaskSpecification.titleContains(q);
        if (byTitle != null) spec = spec.and(byTitle);

        var byCompleted = TaskSpecification.hasCompleted(completed);
        if (byCompleted != null) spec = spec.and(byCompleted);

        var byDescription = TaskSpecification.descriptionContains(d);
        if (byDescription != null) spec = spec.and(byDescription);

        return taskRepository.findAll(spec, pageable).map(taskMapper::toResponse);
    }
}
