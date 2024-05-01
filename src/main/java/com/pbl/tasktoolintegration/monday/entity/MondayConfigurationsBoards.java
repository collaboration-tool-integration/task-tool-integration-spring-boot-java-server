package com.pbl.tasktoolintegration.monday.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@IdClass(MondayConfigurationsBoardsId.class)
@Entity(name = "monday_configurations_boards")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Getter
public class MondayConfigurationsBoards {
    @Id
    @ManyToOne
    @JoinColumn(name = "monday_configurations_id")
    private MondayConfigurations mondayConfiguration;

    @Id
    @ManyToOne
    @JoinColumn(name = "monday_boards_id")
    private MondayBoards mondayBoard;

    @Builder
    public MondayConfigurationsBoards(MondayConfigurations mondayConfiguration, MondayBoards mondayBoard) {
        this.mondayConfiguration = mondayConfiguration;
        this.mondayBoard = mondayBoard;
    }
}
