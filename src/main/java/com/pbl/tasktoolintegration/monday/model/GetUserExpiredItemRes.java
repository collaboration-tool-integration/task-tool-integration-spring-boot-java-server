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
public class GetUserExpiredItemRes {
    private String boardName;
    private List<CountOfExpiredItems> countOfExpiredItems;

    @Data
    @Builder
    public static class CountOfExpiredItems {
        private String username;
        private Integer totalExpiredItems;
    }

    public static GetUserExpiredItemRes from(GetUserExpiredItemDto dto) {
        return GetUserExpiredItemRes.builder()
            .boardName(dto.getBoardName())
            .countOfExpiredItems(dto.getExpiredItemsOfUsers().stream()
                .map(expiredItemsOfUser -> CountOfExpiredItems.builder()
                    .username(expiredItemsOfUser.getUsername())
                    .totalExpiredItems(expiredItemsOfUser.getTotalExpiredItems())
                    .build())
                .toList())
            .build();
    }
}
