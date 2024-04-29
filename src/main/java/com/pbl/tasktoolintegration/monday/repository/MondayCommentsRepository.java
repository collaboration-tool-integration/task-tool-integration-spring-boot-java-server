package com.pbl.tasktoolintegration.monday.repository;

import com.pbl.tasktoolintegration.monday.entity.MondayComments;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MondayCommentsRepository extends JpaRepository<MondayComments, String> {

}
