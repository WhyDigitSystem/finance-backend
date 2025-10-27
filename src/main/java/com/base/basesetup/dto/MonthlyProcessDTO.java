package com.base.basesetup.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyProcessDTO {
	
	private Long id;
	private Long orgId;
	private String mainGroup;
	private String subGroup;
	private String subGroupCode;
	private String month; 
	private String year;
	private String clientCode;
	private String yearType;
	private String client;
	private String createdBy;
	private List<MonthlyProcessDetailsDTO>monthlyProcessDetailsDTO;

}
