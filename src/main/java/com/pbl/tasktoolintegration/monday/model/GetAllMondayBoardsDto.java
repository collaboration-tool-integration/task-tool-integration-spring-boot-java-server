package com.pbl.tasktoolintegration.monday.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetAllMondayBoardsDto {
    private String id;

    public static GetAllMondayBoardsDto from(MondayGetAllBoardsRes.Board board) {
        return GetAllMondayBoardsDto.builder()
            .id(board.getId())
            .build();
    }
}
