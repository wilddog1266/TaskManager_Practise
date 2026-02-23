package com.example.Repetition_7.repository;

import com.example.Repetition_7.entity.TaskEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long>, JpaSpecificationExecutor<TaskEntity> {

    boolean existsByIdAndCreatedBy_Id(Long id, Long createdByUserId);

    Optional<TaskEntity> findByIdAndCreatedBy_Id(Long taskId, Long userId);

}
