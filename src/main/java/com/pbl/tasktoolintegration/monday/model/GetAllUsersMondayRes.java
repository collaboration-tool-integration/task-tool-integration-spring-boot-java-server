package com.pbl.tasktoolintegration.monday.model;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class GetAllUsersMondayRes {
    private Data data;
    private String account_id;

    @Getter
    public static class Data {
        private List<User> users;
    }

    @Getter
    public static class User {
        private String id;
        private String name;
    }
}
