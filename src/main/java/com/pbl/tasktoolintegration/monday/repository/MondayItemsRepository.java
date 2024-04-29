package com.pbl.tasktoolintegration.monday.repository;

import com.pbl.tasktoolintegration.monday.entity.MondayItems;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MondayItemsRepository extends JpaRepository<MondayItems, String>{
}
