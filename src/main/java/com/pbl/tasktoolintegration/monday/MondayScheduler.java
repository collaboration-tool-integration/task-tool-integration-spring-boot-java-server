package com.pbl.tasktoolintegration.monday;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pbl.tasktoolintegration.monday.entity.ResponseTimeType;
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
        List<Long> mondayConfigIds = mondayService.getMondayConfigIds();
        mondayService.syncUsers(mondayConfigIds);
        mondayService.syncBoardsWithItems(mondayConfigIds);
        mondayService.syncUpdatesAndComments(mondayConfigIds);
    }

//    @Scheduled(fixedRate = 1000 * 60 * 5)
    @Scheduled(fixedRate = 1000 * 60 * 60 * 24 * 30)
    public void syncMondayAvgResponseTimeEachUserMonthly() {
        List<Long> mondayConfigIds = mondayService.getMondayConfigIds();
        mondayService.syncAvgResponseTimeEachUser(mondayConfigIds, ResponseTimeType.MONTHLY);
    }

//    @Scheduled(fixedRate = 1000 * 60 * 5)
    @Scheduled(fixedRate = 1000 * 60 * 60 * 24 * 7)
    public void syncMondayAvgResponseTimeEachUserWeekly() {
        List<Long> mondayConfigIds = mondayService.getMondayConfigIds();
        mondayService.syncAvgResponseTimeEachUser(mondayConfigIds, ResponseTimeType.WEEKLY);
    }

    // 매일로 변경 필요
//    @Scheduled(fixedRate = 1000 * 60 * 5)
    @Scheduled(fixedRate = 1000 * 60 * 60 * 24)
    public void syncMondayAvgResponseTimeEachUserDaily() {
        List<Long> mondayConfigIds = mondayService.getMondayConfigIds();
        mondayService.syncAvgResponseTimeEachUser(mondayConfigIds, ResponseTimeType.DAILY);
    }
}
