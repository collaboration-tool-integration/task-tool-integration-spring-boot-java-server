package com.pbl.tasktoolintegration.monday.repository;

import com.pbl.tasktoolintegration.monday.entity.MondayUpdates;
import com.pbl.tasktoolintegration.monday.entity.MondayUpdatesId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MondayUpdatesRepository extends JpaRepository<MondayUpdates, MondayUpdatesId> {

}
