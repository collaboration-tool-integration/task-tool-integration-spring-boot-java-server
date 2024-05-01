package com.pbl.tasktoolintegration.monday.legacy.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.util.Date;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "monday_item")
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class MondayItem {
    @Id
    private String id;

    @Column(name = "is_complete")
    private Boolean isComplete;

    @Column(name = "deadline")
    private Date deadLine;

    @Builder
    public MondayItem(String id, Boolean isComplete, Date deadLine) {
        this.id = id;
        this.isComplete = isComplete;
        this.deadLine = deadLine;
    }
}
