package com.pbl.tasktoolintegration.monday.repository;

import com.pbl.tasktoolintegration.monday.entity.MondayAvgResponseTimeEachUser;
import com.pbl.tasktoolintegration.monday.entity.MondayAvgResponseTimeEachUserId;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MondayAvgResponseTimeEachUserRepository extends
    JpaRepository<MondayAvgResponseTimeEachUser, MondayAvgResponseTimeEachUserId> {
    List<MondayAvgResponseTimeEachUser> findByMondayBoardIdAndType(String mondayBoardId, String type);
}
