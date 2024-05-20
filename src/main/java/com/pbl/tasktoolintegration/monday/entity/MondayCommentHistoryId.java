package com.pbl.tasktoolintegration.monday.entity;

import java.sql.Timestamp;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@EqualsAndHashCode
@Getter
@NoArgsConstructor
public class MondayCommentHistoryId {
    private String id;
    private Timestamp timestamp;
    private MondayUpdatesId mondayUpdate;
}
