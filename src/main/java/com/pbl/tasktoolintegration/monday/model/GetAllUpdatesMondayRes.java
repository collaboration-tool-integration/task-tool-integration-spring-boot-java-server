package com.pbl.tasktoolintegration.monday.model;

import java.util.Date;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class GetAllUpdatesMondayRes {
    private Data data;
    private String account_id;

    @Getter
    public static class Data {
        private List<Update> updates;
    }

    @Getter
    public static class Update {
        private String id;
        private Creator creator;
        private String text_body;
        private Date created_at;
        private Date updated_at;
        private List<Reply> replies;
    }

    @Getter
    public static class Creator {
        private String id;
        private String name;
    }

    @Getter
    public static class Reply {
        private String id;
        private Creator creator;
        private Date created_at;
        private Date updated_at;
    }
}
