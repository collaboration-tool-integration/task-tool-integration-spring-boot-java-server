package com.pbl.tasktoolintegration.monday.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class GetUserExpiredItemRes {
    private String username;
    private Integer totalExpiredItems;


    public static GetUserExpiredItemRes from(GetUserExpiredItemDto dto) {
        return GetUserExpiredItemRes.builder()
            .username(dto.getUsername())
            .totalExpiredItems(dto.getTotalExpiredItems())
            .build();
    }
}
