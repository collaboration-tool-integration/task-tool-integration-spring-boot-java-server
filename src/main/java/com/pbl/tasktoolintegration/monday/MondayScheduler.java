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
        // user의 경우 정보 업데이트 관련 필드가 없어 매번 새로운 정보를 가져옴
        mondayService.syncUsers();

        mondayService.syncUpdates();

        mondayService.syncItems();
    }
}
