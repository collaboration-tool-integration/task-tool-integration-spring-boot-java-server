package com.pbl.tasktoolintegration.monday.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@EqualsAndHashCode
@NoArgsConstructor
@Getter
public class MondayItemsId implements java.io.Serializable{
    private String id;
    private MondayBoards mondayBoard;
}
