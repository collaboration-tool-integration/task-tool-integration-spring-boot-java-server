package com.pbl.tasktoolintegration.monday.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class GetUserResponseTimeRes {
    private String userId;
    private String username;
    private String responseTimeSec;
}
