package com.pbl.tasktoolintegration.monday;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MondayScheduler {
    private final MondayService mondayService;

    @Scheduled(fixedRate = 1000 * 60)
    public void syncMondayDate() throws JsonProcessingException {
//        mondayService.syncUsers();
//
//        mondayService.syncUpdates();
//
//        mondayService.syncItems();
    }
}
