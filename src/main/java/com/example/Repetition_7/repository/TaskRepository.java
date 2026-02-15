package com.example.Repetition_7.repository;

import com.example.Repetition_7.entity.TaskEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long> {

    Page<TaskEntity> findByCompleted(boolean completed, Pageable pageable);

    Page<TaskEntity> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    Page<TaskEntity> findByTitleContainingIgnoreCaseAndCompleted(String title, boolean completed, Pageable pageable);
}
