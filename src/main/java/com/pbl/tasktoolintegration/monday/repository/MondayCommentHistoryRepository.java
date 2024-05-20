package com.pbl.tasktoolintegration.monday.repository;

import com.pbl.tasktoolintegration.monday.entity.MondayCommentHistory;
import com.pbl.tasktoolintegration.monday.entity.MondayCommentHistoryId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MondayCommentHistoryRepository extends JpaRepository<MondayCommentHistory, MondayCommentHistoryId> {

}
