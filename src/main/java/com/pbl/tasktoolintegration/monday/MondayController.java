package com.pbl.tasktoolintegration.monday;

import com.pbl.tasktoolintegration.monday.model.CatchWebhookReq;
import com.pbl.tasktoolintegration.monday.model.GetUserExpiredItemDto;
import com.pbl.tasktoolintegration.monday.model.GetUserExpiredItemRes;
import com.pbl.tasktoolintegration.monday.model.GetUserResponseTimeRes;
import com.pbl.tasktoolintegration.monday.model.GetUsersAverageResponseTimeDto;
import com.pbl.tasktoolintegration.monday.model.MondayWebhookRes;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MondayController {
    private final MondayService mondayService;

    @GetMapping
    public String test() {
        return "Test!";
    }

    @GetMapping("/response-time")
    public ResponseEntity<List<GetUserResponseTimeRes>> getUserResponseTime(@RequestParam Long id){
        List<GetUsersAverageResponseTimeDto> usersAverageResponseTime = mondayService.getUsersAverageResponseTime(id);

        List<GetUserResponseTimeRes> response = usersAverageResponseTime.stream()
            .map(GetUserResponseTimeRes::from)
            .toList();

        return new ResponseEntity(response, HttpStatus.OK);
    }

    @GetMapping("/expired-item")
    public ResponseEntity<List<GetUserExpiredItemRes>> getUserExpiredItem(@RequestParam Long id)
        {
        List<GetUserExpiredItemDto> usersExpiredItem = mondayService.getUsersExpiredItem(id);

        List<GetUserExpiredItemRes> response = usersExpiredItem.stream()
            .map(GetUserExpiredItemRes::from)
            .toList();

        return new ResponseEntity(response, HttpStatus.OK);
    }

    @PostMapping("/monday/webhook")
    public ResponseEntity<MondayWebhookRes> catchWebhook(@RequestBody CatchWebhookReq catchWebhookReq) {
        if (catchWebhookReq.getEvent() != null) {

        }

        // webhook validation
        String challenge = catchWebhookReq.getChallenge();
        MondayWebhookRes response = MondayWebhookRes.builder()
            .challenge(challenge)
            .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
