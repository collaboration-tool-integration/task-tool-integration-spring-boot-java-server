package com.pbl.tasktoolintegration.monday;

import com.pbl.tasktoolintegration.monday.entity.ResponseTimeType;
import com.pbl.tasktoolintegration.monday.model.dto.ActionWebhookDto;
import com.pbl.tasktoolintegration.monday.model.request.CatchWebhookReq;
import com.pbl.tasktoolintegration.monday.model.dto.GetUserExpiredItemDto;
import com.pbl.tasktoolintegration.monday.model.response.GetUserExpiredItemRes;
import com.pbl.tasktoolintegration.monday.model.dto.GetUserNumberOfChangesDto;
import com.pbl.tasktoolintegration.monday.model.response.GetUserNumberOfChangesRes;
import com.pbl.tasktoolintegration.monday.model.response.GetUserResponseTimeRes;
import com.pbl.tasktoolintegration.monday.model.dto.GetUsersAverageResponseTimeDto;
import com.pbl.tasktoolintegration.monday.model.response.MondayWebhookRes;
import com.pbl.tasktoolintegration.monday.model.request.RegisterMondayConfigurationReq;
import com.pbl.tasktoolintegration.monday.model.response.RegisterMondayConfigurationRes;
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

@RestController("/monday")
@RequiredArgsConstructor
@Slf4j
public class MondayController {
    private final MondayService mondayService;

    @GetMapping
    public String test() {
        return "Test!";
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterMondayConfigurationRes> registerMondayConfiguration(@RequestBody RegisterMondayConfigurationReq registerMondayConfigurationReq) {
        Long savedConfigurationId = mondayService.registerMondayConfiguration(registerMondayConfigurationReq.getApiKey());
        RegisterMondayConfigurationRes response = RegisterMondayConfigurationRes.builder()
            .configurationId(savedConfigurationId)
            .build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/response-time")
    public ResponseEntity<List<GetUserResponseTimeRes>> getUserResponseTime(@RequestParam Long id, @RequestParam String type){
        if(type.toUpperCase().equals(ResponseTimeType.DAILY.name()) || type.toUpperCase().equals(ResponseTimeType.WEEKLY.name()) || type.toUpperCase().equals(ResponseTimeType.MONTHLY.name())) {
            List<GetUsersAverageResponseTimeDto> usersAverageResponseTime = mondayService.getUsersAverageResponseTimePeriod(id, ResponseTimeType.valueOf(type.toUpperCase()));
            List<GetUserResponseTimeRes> response = usersAverageResponseTime.stream()
                .map(GetUserResponseTimeRes::from)
                .toList();

            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        List<GetUsersAverageResponseTimeDto> usersAverageResponseTime = mondayService.getUsersAverageResponseTime(id);

        List<GetUserResponseTimeRes> response = usersAverageResponseTime.stream()
            .map(GetUserResponseTimeRes::from)
            .toList();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/expired-item")
    public ResponseEntity<List<GetUserExpiredItemRes>> getUserExpiredItem(@RequestParam Long id)
        {
        List<GetUserExpiredItemDto> usersExpiredItem = mondayService.getUsersExpiredItem(id);

        List<GetUserExpiredItemRes> response = usersExpiredItem.stream()
            .map(GetUserExpiredItemRes::from)
            .toList();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/number-of-changes")
    public ResponseEntity<List<GetUserNumberOfChangesRes>> getUserNumberOfChanges(@RequestParam Long id) {
        List<GetUserNumberOfChangesDto> usersNumberOfChanges = mondayService.getUsersNumberOfChanges(id);

        List<GetUserNumberOfChangesRes> response = usersNumberOfChanges.stream()
            .map(GetUserNumberOfChangesRes::from)
            .toList();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/webhook/update")
    public ResponseEntity<MondayWebhookRes> catchUpdateWebhook(@RequestBody CatchWebhookReq catchWebhookReq) {
        if (catchWebhookReq.getEvent() != null) {
            ActionWebhookDto actionWebhookDto = ActionWebhookDto.from(catchWebhookReq.getEvent());
            mondayService.actionWebhook(actionWebhookDto);
        }

        // webhook validation
        String challenge = catchWebhookReq.getChallenge();
        MondayWebhookRes response = MondayWebhookRes.builder()
            .challenge(challenge)
            .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
