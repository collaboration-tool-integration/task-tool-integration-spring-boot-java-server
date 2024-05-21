package com.pbl.tasktoolintegration.monday;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ModnayQuery {
    MONDAY_GET_UPDATE_BY_ID("query { updates(ids: %s) { item_id creator_id created_at text_body updated_at replies { id creator_id created_at text_body updated_at } } }"),
    MODNAY_GET_ALL_UPDDATES_WITH_COMMENTS_PAGE("query { updates (page : %s) { item_id id creator_id created_at text_body updated_at replies { id creator_id created_at text_body updated_at } } }"),
    MONDAY_GET_ALL_ITEMS_BY_BOARDS_WITHOUT_CURSOR("query { boards (ids: %s) { items_page { cursor items { id creator_id name updated_at created_at column_values { value column { title settings_str } } } } } }"),
    MONDAY_GET_ALL_ITEMS_BY_BOARDS_WITH_CURSOR("query { boards (ids: %s) { items_page (cursor : \"%s\") { cursor items { id creator_id name updated_at created_at column_values { value column { title settings_str } } } } } }"),
    MONDAY_GET_ALL_BOARDS("query { boards (page: %s) { id name updated_at subscribers { id } } }"),
    MONDAY_GET_ALL_USERS("query { users { id created_at email name phone title } }");
    private String query;
}
