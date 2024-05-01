package com.pbl.tasktoolintegration.monday.repository;

import com.pbl.tasktoolintegration.monday.entity.MondayConfigurationsUsers;
import com.pbl.tasktoolintegration.monday.entity.MondayConfigurationsUsersId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MondayConfigurationsUsersRepository extends JpaRepository<MondayConfigurationsUsers, MondayConfigurationsUsersId> {

}
