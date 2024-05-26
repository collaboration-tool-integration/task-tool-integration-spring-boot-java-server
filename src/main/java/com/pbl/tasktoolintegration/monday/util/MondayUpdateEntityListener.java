package com.pbl.tasktoolintegration.monday.util;

import com.pbl.tasktoolintegration.monday.entity.MondayUpdateHistory;
import com.pbl.tasktoolintegration.monday.entity.MondayUpdates;
import com.pbl.tasktoolintegration.monday.repository.MondayUpdateHistoryRepository;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.sql.Timestamp;

public class MondayUpdateEntityListener {
    @PrePersist
    @PreUpdate
    public void prePersistAndPreUpdate(Object o) {
        MondayUpdateHistoryRepository repository = BeanUtil.getBean(MondayUpdateHistoryRepository.class);

        MondayUpdates update = (MondayUpdates) o;

        MondayUpdateHistory history = MondayUpdateHistory.builder()
            .id(update.getId())
            .timestamp(new Timestamp(System.currentTimeMillis()))
            .mondayItem(update.getMondayItem())
            .mondayCreatorUser(update.getMondayCreatorUser())
            .createdAt(update.getCreatedAt())
            .content(update.getContent())
            .updatedAt(update.getUpdatedAt())
            .build();

        repository.save(history);
    }
}