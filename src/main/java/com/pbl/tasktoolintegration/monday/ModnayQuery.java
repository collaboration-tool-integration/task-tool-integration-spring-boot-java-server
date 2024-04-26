package com.pbl.tasktoolintegration.monday;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ModnayQuery {
    MONDAY_GET_ALL_ITEMS_BY_BOARDS_WITHOUT_CURSOR("query { boards (ids: %s) { items_page { cursor items { id } } } }"),
    MONDAY_GET_ALL_ITEMS_BY_BOARDS_WITH_CURSOR("query { boards (ids: %s) { items_page (cursor : \"%s\") { cursor items { id } } } }"),
    MONDAY_GET_ALL_BOARDS("query { boards (page: %s) { id } }"),
    MONDAY_GET_ALL_USERS("query { users { id } }"),
    GET_ALL_UPDATES("query { updates { id updated_at creator{ id name } text_body created_at replies { id updated_at creator { id name } created_at } } }"),
    GET_ALL_BOARDS_WITH_COLUMNS("query { boards { id columns { id title type settings_str } } }"),
    // board id, 담당자 column id, 대상 담당자명, 타임라인 column id, 상태 column id
    // text block -> string.formatted("string")
    GET_USER_ASSIGNED_ITEMS("query { items_page_by_column_values (board_id :%s,  columns :[{column_id :\"%s\", column_values : [\"%s\"]}]) { items { column_values (ids: [\"%s\", \"%s\"]) { id value } } } }"),
    GET_ALL_ITEMS_WITH_COLUMN("query { boards (ids: %s) { items_page { items  { id column_values (ids: [\"%s\", \"%s\", \"%s\"]) { id value } } } } }"),
    GET_ALL_USERS("query { users { id name } }");
    private String query;
}
