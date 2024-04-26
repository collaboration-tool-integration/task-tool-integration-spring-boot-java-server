package com.pbl.tasktoolintegration.monday.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetAllMondayUsersDto {
    private String id;

    public static GetAllMondayUsersDto from(MondayGetAllUsersRes.User user) {
        return GetAllMondayUsersDto.builder()
            .id(user.getId())
            .build();
    }
}
