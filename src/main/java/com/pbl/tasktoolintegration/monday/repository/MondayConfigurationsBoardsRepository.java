package com.pbl.tasktoolintegration.monday.repository;

import com.pbl.tasktoolintegration.monday.entity.MondayConfigurationsBoards;
import com.pbl.tasktoolintegration.monday.entity.MondayConfigurationsBoardsId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MondayConfigurationsBoardsRepository extends JpaRepository<MondayConfigurationsBoards, MondayConfigurationsBoardsId> {

}
