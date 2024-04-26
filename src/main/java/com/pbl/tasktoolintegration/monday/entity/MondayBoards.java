package com.pbl.tasktoolintegration.monday.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "monday_boards")
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class MondayBoards {
    @Id
    private String id;

    @Builder
    public MondayBoards(String id) {
        this.id = id;
    }
}
