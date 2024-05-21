package com.pbl.tasktoolintegration.monday.repository;

import com.pbl.tasktoolintegration.monday.entity.MondayCommentHistory;
import com.pbl.tasktoolintegration.monday.entity.MondayCommentHistoryId;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MondayCommentHistoryRepository extends JpaRepository<MondayCommentHistory, MondayCommentHistoryId> {
    @Query(value = "SELECT * FROM monday_comment_history WHERE id = ?1 order by timestamp", nativeQuery = true)
    List<MondayCommentHistory> findByMondayCommentId(String mondayCommentId);
}
