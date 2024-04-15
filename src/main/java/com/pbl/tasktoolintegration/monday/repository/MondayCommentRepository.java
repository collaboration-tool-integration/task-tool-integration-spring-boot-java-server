package com.pbl.tasktoolintegration.monday.repository;

import com.pbl.tasktoolintegration.monday.entity.MondayComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MondayCommentRepository extends JpaRepository<MondayComment, String> {

}
