package com.pbl.tasktoolintegration.monday.repository;

import com.pbl.tasktoolintegration.monday.entity.MondayBoardsSubscribers;
import com.pbl.tasktoolintegration.monday.entity.MondayBoardsSubscribersId;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MondayBoardsSubscribersRepository extends
    JpaRepository<MondayBoardsSubscribers, MondayBoardsSubscribersId> {
    @Query("select mbs from monday_boards_subscribers mbs where mbs.mondayBoard.id = :id")
    List<MondayBoardsSubscribers> findByMondayBoardId(@Param("id") String mondayBoardId);
}
