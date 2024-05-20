package com.pbl.tasktoolintegration.monday.repository;

import com.pbl.tasktoolintegration.monday.entity.MondayUpdates;
import com.pbl.tasktoolintegration.monday.entity.MondayUpdatesId;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MondayUpdatesRepository extends JpaRepository<MondayUpdates, MondayUpdatesId> {
    @Query(value = "SELECT * FROM monday_updates WHERE monday_board_id in ?1", nativeQuery = true)
    List<MondayUpdates> findByMondayBoardIds(List<String> monday_board_id);

    @Query(value = "select * from monday_updates  where monday_board_id = ?1", nativeQuery = true)
    List<MondayUpdates> findByMondayBoardId(String id);

    List<MondayUpdates> findByMondayItem_MondayBoardIdAndCreatedAtAfter(String id, Date createdAt);
}
