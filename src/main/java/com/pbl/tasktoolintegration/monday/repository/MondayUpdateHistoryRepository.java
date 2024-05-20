package com.pbl.tasktoolintegration.monday.repository;

import com.pbl.tasktoolintegration.monday.entity.MondayUpdateHistory;
import com.pbl.tasktoolintegration.monday.entity.MondayUpdateHistoryId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MondayUpdateHistoryRepository extends
    JpaRepository<MondayUpdateHistory, MondayUpdateHistoryId> {

}
