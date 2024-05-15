package com.pbl.tasktoolintegration.monday.model;

import lombok.Data;

@Data
public class CatchWebhookReq {
    private String challenge;
    private Event event;

    @Data
    public static class Event {
        private String type;
        private String boardId;
        private String pulseId;
        private String userId;
        private String updateId;
        private String replyId;
        private String triggerUuid;
    }
}
