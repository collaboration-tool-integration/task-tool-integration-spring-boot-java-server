package com.pbl.tasktoolintegration.monday.model;

import com.pbl.tasktoolintegration.monday.model.CatchWebhookReq.Event;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ActionWebhookDto {
    private String type;
    private String boardId;
    private String pulseId;
    private String userId;
    private String updateId;
    private String replyId;
    private String triggerUuid;

    public static ActionWebhookDto from(Event event) {
        return ActionWebhookDto.builder()
            .type(event.getType())
            .boardId(event.getBoardId())
            .pulseId(event.getPulseId())
            .userId(event.getUserId())
            .updateId(event.getUpdateId())
            .replyId(event.getReplyId())
            .triggerUuid(event.getTriggerUuid())
            .build();
    }
}
