package com.pbl.tasktoolintegration.monday.model;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetUserNumberOfChangesDto {
    private String boardName;
    private List<CountOfChangesEachUser> countOfChanges;

    @Data
    @Builder
    public static class CountOfChangesEachUser {
        private String username;
        private Integer totalChanges;
    }
}
