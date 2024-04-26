package com.pbl.tasktoolintegration.monday.entity;

import java.io.Serializable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@EqualsAndHashCode
@NoArgsConstructor
@Getter
public class MondayConfigurationsBoardsId implements Serializable {
    private Long mondayConfiguration;
    private String mondayBoard;
}
