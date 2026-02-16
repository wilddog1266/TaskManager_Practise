package com.example.Repetition_7.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "tasks")
public class TaskEntity {

    @PrePersist
    public void onCreate() {
        Instant now = Instant.now();
        if(createdAt == null) createdAt = now;
        if(updatedAt == null) updatedAt = now;
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = Instant.now();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String description;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false, updatable = true)
    private Instant updatedAt;

    @Column(nullable = false)
    private boolean completed;

}
