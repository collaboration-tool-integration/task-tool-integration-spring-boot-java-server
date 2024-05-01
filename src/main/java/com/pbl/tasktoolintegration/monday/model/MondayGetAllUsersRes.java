package com.pbl.tasktoolintegration.monday.model;

import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
        private String url;
    }
}
