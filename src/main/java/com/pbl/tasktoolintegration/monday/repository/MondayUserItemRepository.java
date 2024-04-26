package com.pbl.tasktoolintegration.monday.repository;

import com.pbl.tasktoolintegration.monday.entity.MondayItem;
import com.pbl.tasktoolintegration.monday.entity.MondayUser;
import com.pbl.tasktoolintegration.monday.entity.MondayUserItem;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MondayUserItemRepository extends JpaRepository<MondayUserItem, Long> {
    List<MondayUserItem> findByMondayItem(MondayItem mondayItem);
    boolean existsByMondayItemAndMondayUser(MondayItem mondayItem, MondayUser mondayUser);

}
