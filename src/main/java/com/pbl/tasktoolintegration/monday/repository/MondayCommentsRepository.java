package com.pbl.tasktoolintegration.monday.repository;

import com.pbl.tasktoolintegration.monday.entity.MondayComments;
import com.pbl.tasktoolintegration.monday.entity.MondayCommentsId;
import com.pbl.tasktoolintegration.monday.entity.MondayUpdates;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MondayCommentsRepository extends JpaRepository<MondayComments, MondayCommentsId> {
    List<MondayComments> findByMondayUpdate(MondayUpdates update);
}
