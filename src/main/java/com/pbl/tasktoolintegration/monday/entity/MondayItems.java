package com.pbl.tasktoolintegration.monday.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import java.util.Date;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "monday_items")
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@IdClass(MondayItemsId.class)
@EqualsAndHashCode
public class MondayItems implements java.io.Serializable {
    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "monday_board_id")
    @Id
    private MondayBoards mondayBoard;

    @OneToOne
    @JoinColumns({
        @JoinColumn(name = "monday_parent_item_id", referencedColumnName = "id"),
        @JoinColumn(name = "monday_parent_item_board_id", referencedColumnName = "monday_board_id")
    })
    private MondayItems mondayParentItem;

    @Column
    private String status;

    @Column(name = "deadline_from")
    private Date deadlineFrom;

    @Column(name = "deadline_to")
    private Date deadlineTo;

    @Column
    private String priority;

    @Column
    private String name;

    @Column(name = "updated_at")
    private Date updatedAt;

    @Column(name = "created_at")
    private Date createdAt;


    @Builder
    public MondayItems(String id, MondayBoards mondayBoard, MondayItems mondayParentItem, String status, Date deadlineFrom, Date deadlineTo, String priority, String name, Date updatedAt, Date createdAt) {
        this.id = id;
        this.mondayBoard = mondayBoard;
        this.mondayParentItem = mondayParentItem;
        this.status = status;
        this.deadlineFrom = deadlineFrom;
        this.deadlineTo = deadlineTo;
        this.priority = priority;
        this.name = name;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
    }

    public MondayItems updateDeadline(Date deadlineFrom, Date deadlineTo) {
        this.deadlineFrom = deadlineFrom;
        this.deadlineTo = deadlineTo;

        return this;
    }

    public MondayItems updateStatus(String status) {
        this.status = status;

        return this;
    }
}
