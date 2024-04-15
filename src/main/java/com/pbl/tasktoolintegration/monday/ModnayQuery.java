package com.pbl.tasktoolintegration.monday;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ModnayQuery {
    GET_ALL_UPDATES("query { updates { id updated_at creator{ id name } text_body created_at replies { id updated_at creator { id name } created_at } } }"),
    GET_ALL_USERS("query { users { id name } }");
    private String query;
}
