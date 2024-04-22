package com.pbl.tasktoolintegration.common.entity;

import com.pbl.tasktoolintegration.common.Constants;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
public class BaseEntity {
    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(name = "status", length = 16, nullable = false)
    private Constants.Status status = Constants.Status.ACTIVE;
}
