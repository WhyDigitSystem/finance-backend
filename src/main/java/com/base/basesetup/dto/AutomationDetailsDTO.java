package com.base.basesetup.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AutomationDetailsDTO {
	
	private String mainGroup;
	private String subGroup;
	private String accountCode;
	private String accountName;

}
