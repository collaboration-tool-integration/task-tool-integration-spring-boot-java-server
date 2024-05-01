package com.pbl.tasktoolintegration.monday.legacy.repository;

import com.pbl.tasktoolintegration.monday.legacy.entity.MondayItem;
import com.pbl.tasktoolintegration.monday.legacy.entity.MondayUser;
import com.pbl.tasktoolintegration.monday.legacy.entity.MondayUserItem;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MondayUserItemRepository extends JpaRepository<MondayUserItem, Long> {
    List<MondayUserItem> findByMondayItem(MondayItem mondayItem);
    boolean existsByMondayItemAndMondayUser(MondayItem mondayItem, MondayUser mondayUser);

}
