package com.pbl.tasktoolintegration.monday.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@EqualsAndHashCode
@Getter
@NoArgsConstructor
public class MondayAvgResponseTimeEachUserId implements java.io.Serializable{
    private String mondayUser;
    private String mondayBoard;
    private String type;
}
