package com.pbl.tasktoolintegration.monday;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MondayScheduler {
    private final MondayService mondayService;

    @Scheduled(fixedRate = 1000 * 60 * 3)
    public void syncMondayDate() throws JsonProcessingException {
//        mondayService.syncUsers();
//
//        mondayService.syncUpdates();
//
//        mondayService.syncItems();

        List<Long> mondayConfigIds = mondayService.getMondayConfigIds();
        mondayService.syncUsers(mondayConfigIds);
        mondayService.syncBoardsWithItems(mondayConfigIds);
        mondayService.syncUpdatesAndComments(mondayConfigIds);
    }
}
