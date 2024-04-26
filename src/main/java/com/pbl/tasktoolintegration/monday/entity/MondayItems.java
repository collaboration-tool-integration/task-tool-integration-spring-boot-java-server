package com.pbl.tasktoolintegration.monday.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "monday_items")
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@IdClass(MondayItemsId.class)
public class MondayItems {
    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "monday_board_id")
    @Id
    private MondayBoards mondayBoard;

    @Builder
    public MondayItems(String id, MondayBoards mondayBoard) {
        this.id = id;
        this.mondayBoard = mondayBoard;
    }
}
