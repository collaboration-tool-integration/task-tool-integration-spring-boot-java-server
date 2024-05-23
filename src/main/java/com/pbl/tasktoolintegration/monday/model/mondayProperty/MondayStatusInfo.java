package com.pbl.tasktoolintegration.monday.model.mondayProperty;

import java.util.Map;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;


@Data
@Jacksonized
public class MondayStatusInfo {
    private Map<String, String> labels;
}
