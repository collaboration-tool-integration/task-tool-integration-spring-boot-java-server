package com.pbl.tasktoolintegration.monday.repository;

import com.pbl.tasktoolintegration.monday.entity.MondayConfigurations;
import com.pbl.tasktoolintegration.monday.entity.MondayConfigurationsBoards;
import com.pbl.tasktoolintegration.monday.entity.MondayConfigurationsBoardsId;
import com.pbl.tasktoolintegration.monday.entity.MondayConfigurationsUsers;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MondayConfigurationsBoardsRepository extends JpaRepository<MondayConfigurationsBoards, MondayConfigurationsBoardsId> {
    List<MondayConfigurationsBoards> findByMondayConfiguration(
        MondayConfigurations mondayConfiguration);
}
