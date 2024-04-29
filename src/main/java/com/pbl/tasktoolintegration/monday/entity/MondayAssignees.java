package com.pbl.tasktoolintegration.monday.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "monday_assignees")
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@IdClass(MondayAssigneesId.class)
public class MondayAssignees {
    @ManyToOne
    @JoinColumn(name = "monday_item_id")
    @Id
    private MondayItems mondayItem;

    @ManyToOne
    @JoinColumn(name = "monday_user_id")
    @Id
    private MondayUsers mondayUser;

    @Builder
    public MondayAssignees(MondayItems mondayItem, MondayUsers mondayUser) {
        this.mondayItem = mondayItem;
        this.mondayUser = mondayUser;
    }
}
