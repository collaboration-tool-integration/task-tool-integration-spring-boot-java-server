package com.pbl.tasktoolintegration.monday.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.Date;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "monday_update")
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class MondayUpdate {
    @Id
    private String id;

    @Column
    private String content;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    @ManyToOne
    @JoinColumn(name = "monday_creator_id")
    private MondayUser creator;

    public void updateContent(String content) {
        this.content = content;
    }

    public void updateUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Builder
    public MondayUpdate(String id, String content, Date createdAt, Date updatedAt, MondayUser creator) {
        this.id = id;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.creator = creator;
    }
}
