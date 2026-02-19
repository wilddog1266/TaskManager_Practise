package com.example.Repetition_7.security;

import com.example.Repetition_7.entity.UserEntity;
import com.example.Repetition_7.entity.roles.UserRole;
import com.example.Repetition_7.repository.TaskRepository;
import com.example.Repetition_7.service.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component("taskSecurity")
@RequiredArgsConstructor
public class TaskSecurity {

    private final CurrentUserService currentUserService;
    private final TaskRepository taskRepository;

    public boolean canAccessTask(Long taskId) {
        if(taskId == null) return false;

        if (!taskRepository.existsById(taskId)) {
            return true;
        }

        UserEntity currentUser = currentUserService.getCurrentUser();

        if(currentUser.getRole() == UserRole.ADMIN) {
            return true;
        }

        return taskRepository.existsByIdAndCreatedBy_Id(taskId, currentUser.getId());
    }

    public boolean canModifyTask(Long taskId) {
        return canAccessTask(taskId);
    }
}
