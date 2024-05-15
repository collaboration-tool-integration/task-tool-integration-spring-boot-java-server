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
public class GetUserExpiredItemDto {
    private String boardName;
    private List<ExpiredItemsOfUser> expiredItemsOfUsers;

    @Data
    @Builder
    public static class ExpiredItemsOfUser {
        private String username;
        private Integer totalExpiredItems;
    }
}
