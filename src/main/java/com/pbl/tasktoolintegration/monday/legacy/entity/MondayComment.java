package com.pbl.tasktoolintegration.monday.legacy.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.Date;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "monday_comment")
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class MondayComment {
    @Id
    private String id;

    @Column(name = "created_at")
    private Date createdAt;

    @ManyToOne
    @JoinColumn(name = "monday_creator_id")
    private MondayUser creator;

    @ManyToOne
    @JoinColumn(name = "monday_update_id")
    private MondayUpdate update;

    @Builder
    public MondayComment(String id, Date createdAt, MondayUser creator, MondayUpdate update) {
        this.id = id;
        this.createdAt = createdAt;
        this.creator = creator;
        this.update = update;
    }
}