package com.pbl.tasktoolintegration.monday.model.mondayProperty;

import java.util.Date;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Jacksonized
public class MondayStatusColumnInfo {
    private Integer index;
    private String post_id;
    private Date changed_at;
}
