package com.pbl.tasktoolintegration.monday.model;

import java.util.List;
import lombok.Data;
import lombok.Getter;

@Data
public class GetAllItemsWithColumnMondayRes {
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
        private Item[] items;
    }

    @lombok.Data
    public static class Item {
        private String id;
        private List<ColumnValue> column_values;
    }

    @lombok.Data
    public static class ColumnValue {
        private String id;
        private String value;
    }
}
