package com.pbl.tasktoolintegration.monday.repository;

import com.pbl.tasktoolintegration.monday.entity.MondayBoards;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MondayBoardsRepository extends JpaRepository<MondayBoards, String> {

}
