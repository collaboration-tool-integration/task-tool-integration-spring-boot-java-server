package com.pbl.tasktoolintegration.monday.model;

import java.util.Date;
import java.util.List;
import lombok.Data;

@Data
public class MondayGetAllUpdatesWithCommentRes {
    private Data data;

    @lombok.Data
    public static class Data {
        private List<Update> updates;
    }

    @lombok.Data
    public static class Update {
        private String item_id;
        private String id;
        private String creator_id;
        private Date created_at;
        private String text_body;
        private Date updated_at;
        private List<Reply> replies;
    }

    @lombok.Data
    public static class Reply {
        private String id;
        private String creator_id;
        private Date created_at;
        private String text_body;
        private Date updated_at;
    }
}
