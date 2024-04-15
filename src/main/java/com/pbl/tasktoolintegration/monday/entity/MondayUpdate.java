package com.pbl.tasktoolintegration.monday.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.Date;
import lombok.Getter;

@Entity(name = "monday_update")
@Getter
public class MondayUpdate {
    @Id
    private String id;

    @Column
    private String content;

    @Column
    private Date createdAt;

    @ManyToOne
    @JoinColumn
    private MondayUser creator;
}
