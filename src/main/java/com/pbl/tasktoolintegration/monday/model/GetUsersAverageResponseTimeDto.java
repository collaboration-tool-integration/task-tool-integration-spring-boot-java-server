package com.pbl.tasktoolintegration.monday.model;

import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class GetUsersAverageResponseTimeDto {
    private String boardId;
    private String boardName;
    private List<ResponseTimeOfUser> responseTimeOfUsers;

    @Data
    @Builder
    public static class ResponseTimeOfUser{
        private String userId;
        private String username;
        private double averageResponseTime;
    }
}
