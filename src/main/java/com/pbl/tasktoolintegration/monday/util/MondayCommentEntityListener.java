package com.pbl.tasktoolintegration.monday.util;

import com.pbl.tasktoolintegration.monday.entity.MondayCommentHistory;
import com.pbl.tasktoolintegration.monday.entity.MondayComments;
import com.pbl.tasktoolintegration.monday.entity.MondayUpdateHistory;
import com.pbl.tasktoolintegration.monday.entity.MondayUpdates;
import com.pbl.tasktoolintegration.monday.repository.MondayCommentHistoryRepository;
import com.pbl.tasktoolintegration.monday.repository.MondayUpdateHistoryRepository;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.sql.Timestamp;

public class MondayCommentEntityListener {
    @PrePersist
    @PreUpdate
    public void prePersistAndPreUpdate(Object o) {
        MondayCommentHistoryRepository repository = BeanUtil.getBean(MondayCommentHistoryRepository.class);

        MondayComments comment = (MondayComments) o;

        MondayCommentHistory history = MondayCommentHistory.builder()
            .id(comment.getId())
            .timestamp(new Timestamp(System.currentTimeMillis()))
            .mondayUpdate(comment.getMondayUpdate())
            .mondayCreatorUser(comment.getMondayCreatorUser())
            .createdAt(comment.getCreatedAt())
            .content(comment.getContent())
            .updatedAt(comment.getUpdatedAt())
            .build();

        repository.save(history);
    }
}