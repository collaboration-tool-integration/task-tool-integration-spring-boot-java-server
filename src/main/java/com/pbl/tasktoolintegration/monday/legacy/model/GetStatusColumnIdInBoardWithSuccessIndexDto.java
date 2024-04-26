package com.pbl.tasktoolintegration.monday.legacy.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class GetStatusColumnIdInBoardWithSuccessIndexDto {
    private String statusColumnId;
    private int completeStatusIndex;
}
