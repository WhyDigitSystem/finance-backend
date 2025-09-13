package com.base.basesetup.dto;

import lombok.Data;
import java.util.List;

@Data
public class ScheduleRequest {
    private List<String> employeeCodes;
    private List<String> dateTime;   
    private List<String> bccEmails;

}
