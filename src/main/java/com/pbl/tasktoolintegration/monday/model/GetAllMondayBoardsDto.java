package com.pbl.tasktoolintegration.monday.model;

import java.util.Date;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetAllMondayBoardsDto {
    private String id;
    private String name;
    private Date updatedAt;

    public static GetAllMondayBoardsDto from(MondayGetAllBoardsRes.Board board) {
        return GetAllMondayBoardsDto.builder()
                .id(board.getId())
                .name(board.getName())
                .updatedAt(board.getUpdated_at())
                .build();
    }
}
