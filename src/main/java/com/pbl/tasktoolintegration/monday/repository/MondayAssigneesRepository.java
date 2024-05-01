package com.pbl.tasktoolintegration.monday.repository;

import com.pbl.tasktoolintegration.monday.entity.MondayAssignees;
import com.pbl.tasktoolintegration.monday.entity.MondayAssigneesId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MondayAssigneesRepository extends
    JpaRepository<MondayAssignees, MondayAssigneesId> {

}
