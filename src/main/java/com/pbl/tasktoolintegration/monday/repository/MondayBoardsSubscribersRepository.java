package com.pbl.tasktoolintegration.monday.repository;

import com.pbl.tasktoolintegration.monday.entity.MondayBoardsSubscribers;
import com.pbl.tasktoolintegration.monday.entity.MondayBoardsSubscribersId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MondayBoardsSubscribersRepository extends
    JpaRepository<MondayBoardsSubscribers, MondayBoardsSubscribersId> {

}
