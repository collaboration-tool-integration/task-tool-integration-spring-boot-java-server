package com.pbl.tasktoolintegration.monday.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@EqualsAndHashCode
@NoArgsConstructor
@Getter
public class MondayAssigneesId implements java.io.Serializable{
    private MondayItemsId mondayItem;
    private String mondayUser;
}
