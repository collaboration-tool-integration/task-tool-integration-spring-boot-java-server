package com.pbl.tasktoolintegration.monday.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "monday_avg_response_time_each_user")
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@IdClass(MondayAvgResponseTimeEachUserId.class)
public class MondayAvgResponseTimeEachUser {
    @Id
    @ManyToOne
    @JoinColumn(name = "monday_user_id")
    private MondayUsers mondayUser;

    @Id
    @ManyToOne
    @JoinColumn(name = "monday_board_id")
    private MondayBoards mondayBoard;

    @Column
    private Double avgResponseTime;

    @Column
    @Id
    private String type;

    @Builder
    public MondayAvgResponseTimeEachUser(MondayUsers mondayUser, MondayBoards mondayBoard, Double avgResponseTime, String type) {
        this.mondayUser = mondayUser;
        this.mondayBoard = mondayBoard;
        this.avgResponseTime = avgResponseTime;
        this.type = type;
    }
}
