package com.pbl.tasktoolintegration.monday.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "monday_comments")
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class MondayComments {
    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "monday_update_id", nullable = false)
    private MondayUpdates mondayUpdate;

    @ManyToOne
    @JoinColumn(name = "monday_creator_user_id")
    private MondayUsers mondayCreatorUser;

    @Builder
    public MondayComments(String id, MondayUpdates mondayUpdate, MondayUsers mondayCreatorUser) {
        this.id = id;
        this.mondayUpdate = mondayUpdate;
        this.mondayCreatorUser = mondayCreatorUser;
    }
}
