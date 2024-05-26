package com.pbl.tasktoolintegration.monday.entity;

import com.pbl.tasktoolintegration.monday.util.MondayUpdateEntityListener;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
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

@Entity(name = "monday_updates")
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@IdClass(MondayUpdatesId.class)
@EqualsAndHashCode
@EntityListeners(MondayUpdateEntityListener.class)
public class MondayUpdates implements java.io.Serializable {
    @Id
    private String id;

    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "monday_item_id", referencedColumnName = "id"),
        @JoinColumn(name = "monday_board_id", referencedColumnName = "monday_board_id")
    })
    @Id
    private MondayItems mondayItem;

    @ManyToOne
    @JoinColumn(name = "monday_creator_user_id")
    private MondayUsers mondayCreatorUser;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(name = "update_at")
    private Date updatedAt;

    @Builder
    public MondayUpdates(String id, MondayItems mondayItem, MondayUsers mondayCreatorUser, Date createdAt, String content, Date updatedAt) {
        this.id = id;
        this.mondayItem = mondayItem;
        this.mondayCreatorUser = mondayCreatorUser;
        this.createdAt = createdAt;
        this.content = content;
        this.updatedAt = updatedAt;
    }
}
