package com.pbl.tasktoolintegration.monday.model.response;

import java.util.Date;
import java.util.List;
import lombok.Data;

@Data
public class MondayGetAllUsersRes {
    private Data data;
    private Long account_id;

    @lombok.Data
    public static class Data {
        private List<User> users;
    }

    @lombok.Data
    public static class User {
        private String id;
        private Date created_at;
        private String email;
        private String name;
        private String phone;
        private String title;
    }
}
