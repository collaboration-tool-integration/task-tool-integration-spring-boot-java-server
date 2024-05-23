package com.pbl.tasktoolintegration.monday.model.response;

import java.util.Date;
import java.util.List;
import lombok.Data;

@Data
public class GetAllMondayItemsWIthCursorRes {
    private Data data;
    private String account_id;

    @lombok.Data
    public static class Data {
        private List<Board> boards;
    }

    @lombok.Data
    public static class Board {
        private ItemPage items_page;
    }

    @lombok.Data
    public static class ItemPage {
        private String cursor;
        private List<Item> items;
    }

    @lombok.Data
    public static class Item {
        private String id;
        private String creator_id;
        private String name;
        private Date updated_at;
        private Date created_at;
        private List<ColumnValue> column_values;
    }

    @lombok.Data
    public static class ColumnValue {
        private String value;
        private Column column;
    }

    @lombok.Data
    public static class Column {
        private String title;
        private String settings_str;
    }
}
