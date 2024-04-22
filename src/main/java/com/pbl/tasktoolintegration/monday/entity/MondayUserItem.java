package com.pbl.tasktoolintegration.monday.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "monday_user_item")
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class MondayUserItem {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "monday_user_id")
    private MondayUser mondayUser;

    @ManyToOne
    @JoinColumn(name = "monday_item_id")
    private MondayItem mondayItem;

    @Builder
    public MondayUserItem(MondayUser mondayUser, MondayItem mondayItem) {
        this.mondayUser = mondayUser;
        this.mondayItem = mondayItem;
    }
}
