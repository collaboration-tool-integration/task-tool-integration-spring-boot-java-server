package com.pbl.tasktoolintegration.monday.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@EqualsAndHashCode
@NoArgsConstructor
@Getter
public class MondayConfigurationsUsersId implements java.io.Serializable {
    private Long mondayConfiguration;
    private String mondayUser;
}
