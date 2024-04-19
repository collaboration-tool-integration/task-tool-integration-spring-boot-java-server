package com.pbl.tasktoolintegration.monday.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class GetUserResponseTimeRes {
    private String username;
    private String responseTimeSec;

    public static GetUserResponseTimeRes from(GetUsersAverageResponseTimeDto dto) {
        return GetUserResponseTimeRes.builder()
            .username(dto.getUsername())
            .responseTimeSec(String.valueOf(dto.getAverageResponseTime()))
            .build();
    }
}
