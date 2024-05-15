package com.pbl.tasktoolintegration.monday.repository;

import com.pbl.tasktoolintegration.monday.entity.MondayAssignees;
import com.pbl.tasktoolintegration.monday.entity.MondayAssigneesId;
import com.pbl.tasktoolintegration.monday.entity.MondayBoards;
import com.pbl.tasktoolintegration.monday.entity.MondayUsers;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MondayAssigneesRepository extends JpaRepository<MondayAssignees, MondayAssigneesId> {
    List<MondayAssignees> findByMondayUserAndMondayItem_StatusAndMondayItem_DeadlineToLessThanAndMondayItem_MondayBoard (
        MondayUsers mondayUser, String status, Date deadline_to, MondayBoards mondayBoard);
}
