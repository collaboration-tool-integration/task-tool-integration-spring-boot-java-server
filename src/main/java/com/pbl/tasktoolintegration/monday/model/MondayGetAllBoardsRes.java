package com.pbl.tasktoolintegration.monday.model;

import java.util.Date;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
public class MondayGetAllBoardsRes {
    private Data data;

    @lombok.Data
    public static class Data {
        private List<Board> boards;
    }

    @lombok.Data
    public static class Board {
        private String id;
        private String name;
        private Date updated_at;
    }
}
