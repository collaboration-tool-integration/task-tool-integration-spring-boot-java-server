package com.pbl.tasktoolintegration.monday.model;

import java.util.Date;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class GetUserExpiredItemDto {
    private String username;
    private Integer totalExpiredItems;
}
