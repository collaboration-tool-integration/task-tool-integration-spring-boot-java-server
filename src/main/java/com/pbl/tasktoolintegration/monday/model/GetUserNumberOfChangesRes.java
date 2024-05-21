package com.pbl.tasktoolintegration.monday.model;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetUserNumberOfChangesRes {
    private String boardName;
    private List<CountOfChangesEachUser> countOfChanges;

    @Data
    @Builder
    public static class CountOfChangesEachUser {
        private String username;
        private Integer totalChanges;
    }

    public static GetUserNumberOfChangesRes from(GetUserNumberOfChangesDto dto) {
        return GetUserNumberOfChangesRes.builder()
            .boardName(dto.getBoardName())
            .countOfChanges(dto.getCountOfChanges().stream()
                .map(countOfChangesEachUser -> CountOfChangesEachUser.builder()
                    .username(countOfChangesEachUser.getUsername())
                    .totalChanges(countOfChangesEachUser.getTotalChanges())
                    .build())
                .toList())
            .build();
    }
}
