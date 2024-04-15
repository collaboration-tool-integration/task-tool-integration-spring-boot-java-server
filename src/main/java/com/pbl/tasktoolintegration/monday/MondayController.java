package com.pbl.tasktoolintegration.monday;

import com.pbl.tasktoolintegration.monday.model.GetUserResponseTimeRes;
import com.pbl.tasktoolintegration.monday.model.GetUsersAverageResponseTimeDto;
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
}
