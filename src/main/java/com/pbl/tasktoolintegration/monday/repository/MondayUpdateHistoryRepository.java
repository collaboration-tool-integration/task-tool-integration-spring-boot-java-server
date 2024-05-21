package com.pbl.tasktoolintegration.monday.repository;

import com.pbl.tasktoolintegration.monday.entity.MondayUpdateHistory;
import com.pbl.tasktoolintegration.monday.entity.MondayUpdateHistoryId;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MondayUpdateHistoryRepository extends
    JpaRepository<MondayUpdateHistory, MondayUpdateHistoryId> {
    @Query(value = "SELECT * FROM monday_update_history WHERE id = ?1 order by timestamp", nativeQuery = true)
    List<MondayUpdateHistory>  findByMondayUpdateId(String mondayUpdateId);
}
