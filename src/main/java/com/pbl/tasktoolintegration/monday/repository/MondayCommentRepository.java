package com.pbl.tasktoolintegration.monday.repository;

import com.pbl.tasktoolintegration.monday.entity.MondayComment;
import com.pbl.tasktoolintegration.monday.entity.MondayUpdate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MondayCommentRepository extends JpaRepository<MondayComment, String> {
    List<MondayComment> findByUpdate(MondayUpdate update);

}
