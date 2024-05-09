package com.pbl.tasktoolintegration.monday.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "monday_boards_subscribers")
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@IdClass(MondayBoardsSubscribersId.class)
public class MondayBoardsSubscribers {
    @ManyToOne
    @JoinColumn(name = "monday_board_id")
    @Id
    private MondayBoards mondayBoard;

    @ManyToOne
    @JoinColumn(name = "monday_user_id")
    @Id
    private MondayUsers mondayUser;

    @Builder
    public MondayBoardsSubscribers(MondayBoards mondayBoard, MondayUsers mondayUser) {
        this.mondayBoard = mondayBoard;
        this.mondayUser = mondayUser;
    }
}
