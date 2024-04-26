package com.pbl.tasktoolintegration.monday;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pbl.tasktoolintegration.monday.legacy.model.GetUserExpiredItemDto;
import com.pbl.tasktoolintegration.monday.legacy.model.GetUserExpiredItemRes;
import com.pbl.tasktoolintegration.monday.legacy.model.GetUserResponseTimeRes;
import com.pbl.tasktoolintegration.monday.legacy.model.GetUsersAverageResponseTimeDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
    public ResponseEntity<List<GetUserResponseTimeRes>> getUserResponseTime(){
        List<GetUsersAverageResponseTimeDto> usersAverageResponseTime = mondayService.getUsersAverageResponseTime();

        List<GetUserResponseTimeRes> response = usersAverageResponseTime.stream()
            .map(GetUserResponseTimeRes::from)
            .toList();

        return new ResponseEntity(response, HttpStatus.OK);
    }

    @GetMapping("/batch/response-time")
    public ResponseEntity<List<GetUserResponseTimeRes>> getBatchUserResponseTime(){
        List<GetUsersAverageResponseTimeDto> usersAverageResponseTime = mondayService.getBatchUsersAverageResponseTime();

        List<GetUserResponseTimeRes> response = usersAverageResponseTime.stream()
            .map(GetUserResponseTimeRes::from)
            .toList();

        return new ResponseEntity(response, HttpStatus.OK);
    }

    @GetMapping("/expired-item")
    public ResponseEntity<List<GetUserExpiredItemRes>> getUserExpiredItem()
        throws JsonProcessingException {
        List<GetUserExpiredItemDto> usersExpiredItem = mondayService.getUsersExpiredItem();

        List<GetUserExpiredItemRes> response = usersExpiredItem.stream()
            .map(GetUserExpiredItemRes::from)
            .toList();

        return new ResponseEntity(response, HttpStatus.OK);
    }

    @GetMapping("/batch/expired-item")
    public ResponseEntity<List<GetUserExpiredItemRes>> getBatchUserExpiredItem()
        throws JsonProcessingException {
        List<GetUserExpiredItemDto> usersExpiredItem = mondayService.getBatchUsersExpiredItem();

        List<GetUserExpiredItemRes> response = usersExpiredItem.stream()
            .map(GetUserExpiredItemRes::from)
            .toList();

        return new ResponseEntity(response, HttpStatus.OK);
    }
}
