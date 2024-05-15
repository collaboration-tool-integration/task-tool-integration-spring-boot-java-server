package com.pbl.tasktoolintegration.monday.model;

import java.util.Date;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetAllMondayBoardsDto {
    private String id;
    private String name;
    private Date updatedAt;
    private List<String> subscriberIds;

    public static GetAllMondayBoardsDto from(MondayGetAllBoardsRes.Board board) {
        return GetAllMondayBoardsDto.builder()
                .id(board.getId())
                .name(board.getName())
                .updatedAt(board.getUpdated_at())
                .subscriberIds(board.getSubscribers().stream().map(MondayGetAllBoardsRes.Subscriber::getId).toList())
                .build();
    }
}
