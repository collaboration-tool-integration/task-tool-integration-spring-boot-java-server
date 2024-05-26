package com.pbl.tasktoolintegration.monday.repository;

import com.pbl.tasktoolintegration.monday.entity.MondayConfigurations;
import com.pbl.tasktoolintegration.monday.entity.MondayConfigurationsUsers;
import com.pbl.tasktoolintegration.monday.entity.MondayConfigurationsUsersId;
import com.pbl.tasktoolintegration.monday.entity.MondayUsers;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MondayConfigurationsUsersRepository extends JpaRepository<MondayConfigurationsUsers, MondayConfigurationsUsersId> {
    List<MondayConfigurationsUsers> findByMondayConfiguration(MondayConfigurations mondayConfiguration);
}
