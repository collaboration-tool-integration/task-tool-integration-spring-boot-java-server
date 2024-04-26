package com.pbl.tasktoolintegration.monday.legacy.repository;

import com.pbl.tasktoolintegration.monday.legacy.entity.MondayItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MondayItemRepository extends JpaRepository<MondayItem, String> {

}
