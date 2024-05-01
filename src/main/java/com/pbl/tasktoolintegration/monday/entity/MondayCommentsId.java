package com.pbl.tasktoolintegration.monday.entity;

import java.io.Serializable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@EqualsAndHashCode
@NoArgsConstructor
@Getter
public class MondayCommentsId implements Serializable {
    private String id;
    private MondayUpdatesId mondayUpdate;
}
