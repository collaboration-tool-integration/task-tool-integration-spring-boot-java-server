package com.pbl.tasktoolintegration.monday.model.mondayProperty;

import java.util.Date;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Jacksonized
public class MondayTimelineColumnInfo {
    private Date to;
    private Date from;
    private Date changed_at;
}
