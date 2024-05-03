package com.pbl.tasktoolintegration.monday.repository;

import com.pbl.tasktoolintegration.monday.entity.MondayUpdates;
import com.pbl.tasktoolintegration.monday.entity.MondayUpdatesId;
import com.pbl.tasktoolintegration.monday.entity.MondayUsers;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MondayUpdatesRepository extends JpaRepository<MondayUpdates, MondayUpdatesId> {
    @Query(value = "SELECT * FROM monday_updates WHERE monday_board_id in ?1", nativeQuery = true)
    List<MondayUpdates> findByMondayBoardId(List<String> monday_board_id);
}
