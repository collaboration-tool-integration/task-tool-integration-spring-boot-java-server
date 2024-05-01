package com.pbl.tasktoolintegration.monday.repository;

import com.pbl.tasktoolintegration.monday.entity.MondayItems;
import com.pbl.tasktoolintegration.monday.entity.MondayItemsId;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MondayItemsRepository extends JpaRepository<MondayItems, MondayItemsId>{
    @Query("SELECT m FROM monday_items m WHERE m.id = :uid")
    List<MondayItems> findByUniqueId(@Param("uid") String uniqueId);
}
