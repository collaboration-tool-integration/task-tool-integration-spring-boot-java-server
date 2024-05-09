package com.pbl.tasktoolintegration.monday.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@EqualsAndHashCode
@NoArgsConstructor
@Getter
public class MondayBoardsSubscribersId implements java.io.Serializable{
    private String mondayBoard;
    private String mondayUser;
}
