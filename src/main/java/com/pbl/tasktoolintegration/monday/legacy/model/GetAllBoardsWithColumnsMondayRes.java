package com.pbl.tasktoolintegration.monday.legacy.model;

import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;

@Data
public class GetAllBoardsWithColumnsMondayRes {
    private Data data;
    private String account_id;

    @lombok.Data
    public static class Data {
        private List<Board> boards;
    }

    @Getter
    public static class Board {
        private String id;
        private List<Column> columns;
    }

    @Getter
    public static class Column {
        private String id;
        private String title;
        private String type;
        private String settings_str;
    }
}
