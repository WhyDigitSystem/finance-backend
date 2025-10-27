package com.base.basesetup.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BudgetUnitWiseDTO {

	private Long orgId;
	private String client;
	private String clientCode;
	private String year;
	private String month;
	private String accountName; 
	private String accountCode;
	private String natureOfAccount;
	private BigDecimal amount;
	private String mainGroup;
	private String unit;
	private String subGroup;
	private String subGroupCode;
	private String createdBy;
	private String yearType;
	
}
