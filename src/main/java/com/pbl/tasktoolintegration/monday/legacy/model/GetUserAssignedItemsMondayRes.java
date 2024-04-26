package com.pbl.tasktoolintegration.monday.legacy.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@Builder
public class GetUserAssignedItemsMondayRes {
    private Data data;
    private String account_id;

    @lombok.Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Data {
        private ItemPageByColumnValues items_page_by_column_values;
    }

    @lombok.Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ItemPageByColumnValues {
        private List<Item> items;
    }

    @lombok.Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Item {
        private List<ColumnValue> column_values;
    }

    @lombok.Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ColumnValue {
        private String id;
        private String value;
    }
}
