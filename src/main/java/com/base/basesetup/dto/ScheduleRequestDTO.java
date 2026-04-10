package com.base.basesetup.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleRequestDTO {
	private Long scheduleIds;  
    private String employeeCodes;
    private String dateTime;   
    private String bccEmails;
    private Long orgId;

}
