package com.pbl.tasktoolintegration.monday.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import java.sql.Timestamp;
import java.util.Date;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

@Entity(name = "monday_comment_history")
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@IdClass(MondayCommentHistoryId.class)
public class MondayCommentHistory {
    @Id
    private String id;

    @Id
    private Timestamp timestamp;

    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "monday_item_id", referencedColumnName = "monday_item_id"),
        @JoinColumn(name = "monday_board_id", referencedColumnName = "monday_board_id"),
        @JoinColumn(name = "monday_update_id", referencedColumnName = "id")
    })
    @Id
    private MondayUpdates mondayUpdate;

    @ManyToOne
    @JoinColumn(name = "monday_creator_user_id")
    private MondayUsers mondayCreatorUser;


    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Builder
    public MondayCommentHistory(String id, Timestamp timestamp, MondayUpdates mondayUpdate, MondayUsers mondayCreatorUser, Date createdAt, String content, Date updatedAt) {
        this.id = id;
        this.timestamp = timestamp;
        this.mondayUpdate = mondayUpdate;
        this.mondayCreatorUser = mondayCreatorUser;
        this.createdAt = createdAt;
        this.content = content;
        this.updatedAt = updatedAt;
    }
}
