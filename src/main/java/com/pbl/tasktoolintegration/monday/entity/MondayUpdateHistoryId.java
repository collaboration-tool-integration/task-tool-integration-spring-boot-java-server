package com.pbl.tasktoolintegration.monday.entity;

import java.sql.Timestamp;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@EqualsAndHashCode
@NoArgsConstructor
@Getter
public class MondayUpdateHistoryId implements java.io.Serializable{
    private String id;
    private Timestamp timestamp;
    private MondayItemsId mondayItem;
}
