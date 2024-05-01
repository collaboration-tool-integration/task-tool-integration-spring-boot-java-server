package com.pbl.tasktoolintegration.monday.entity;

import java.io.Serializable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@EqualsAndHashCode
@NoArgsConstructor
@Getter
public class MondayUpdatesId implements Serializable {
    private String id;
    private MondayItemsId mondayItem;
}
