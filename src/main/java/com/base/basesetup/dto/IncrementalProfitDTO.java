package com.base.basesetup.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IncrementalProfitDTO {
	

	private String year;
	private String month;
	private Long orgId;
	private String client;
	private String clientCode;
	private String createdBy;
	private String mainGroup;
	private String subGroup;
	private String accountName;
	private String accountCode;
	private BigDecimal amount;
	
	private String yearType;


}
