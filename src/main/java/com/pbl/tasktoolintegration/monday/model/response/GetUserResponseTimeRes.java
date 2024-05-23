package com.pbl.tasktoolintegration.monday.model.response;

import com.pbl.tasktoolintegration.monday.model.dto.GetUsersAverageResponseTimeDto;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class GetUserResponseTimeRes {
    private String boardName;
    private List<ResponseTime> responseTimeOfUsers;

    @Data
    @Builder
    public static class ResponseTime {
        private String username;
        private double responseTimeSec;
    }

    public static GetUserResponseTimeRes from(GetUsersAverageResponseTimeDto dto) {
        return GetUserResponseTimeRes.builder()
            .boardName(dto.getBoardName())
            .responseTimeOfUsers(dto.getResponseTimeOfUsers().stream().map(user -> ResponseTime.builder()
                .username(user.getUsername())
                .responseTimeSec(user.getAverageResponseTime())
                .build()).toList())
            .build();
    }
}
