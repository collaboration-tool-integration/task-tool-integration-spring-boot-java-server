package com.pbl.tasktoolintegration.monday.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.util.Date;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "monday_boards")
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class MondayBoards implements java.io.Serializable {
    @Id
    private String id;

    @Column
    private String name;

    @Column(name = "updated_at")
    private Date updatedAt;

    @Builder
    public MondayBoards(String id, String name, Date updatedAt) {
        this.id = id;
        this.name = name;
        this.updatedAt = updatedAt;
    }
}
