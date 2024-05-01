package com.pbl.tasktoolintegration.monday.legacy.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "monday_user")
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class MondayUser {
    @Id
    private String id;

    @Column
    private String name;

    public void updateName(String name) {
        this.name = name;
    }

    @Builder
    public MondayUser(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
