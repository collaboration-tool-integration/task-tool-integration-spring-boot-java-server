package com.pbl.tasktoolintegration.monday.repository;

import com.pbl.tasktoolintegration.monday.entity.MondayItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MondayItemRepository extends JpaRepository<MondayItem, String> {

}
