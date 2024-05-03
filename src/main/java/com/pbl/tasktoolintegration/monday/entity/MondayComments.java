package com.pbl.tasktoolintegration.monday.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import java.util.Date;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "monday_comments")
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@IdClass(MondayCommentsId.class)
@EqualsAndHashCode
public class MondayComments implements java.io.Serializable {
    @Id
    private String id;

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
    public MondayComments(String id, MondayUpdates mondayUpdate,
            MondayUsers mondayCreatorUser, Date createdAt, Date updatedAt, String content) {
        this.id = id;
        this.mondayUpdate = mondayUpdate;
        this.mondayCreatorUser = mondayCreatorUser;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.content = content;
    }
}
