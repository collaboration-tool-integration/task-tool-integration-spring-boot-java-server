package com.pbl.tasktoolintegration.monday.model;

import java.util.Date;
import java.util.List;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Jacksonized
public class MondayAssigneeInfo {
    private Date changed_at;
    private List<Person> personsAndTeams;

    @Data
    @Jacksonized
    public static class Person {
        private Integer id;
        private String kind;
    }
}
