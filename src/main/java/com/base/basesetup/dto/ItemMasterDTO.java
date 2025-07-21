package com.base.basesetup.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemMasterDTO {

	private Long id;
	private Long orgId;
	private String branch;
	private String branchCode;
	private String finYear;
	private String createdBy;
	private String modifiedBy;
	private boolean active=true;
	private boolean cancel=false;
	private String cancelRemarks;
	
	private String custPartNo;
	private String dupChk;
	private String hsnCode;
	
	private String itemType;
	private String partDesc;
	private String partNo;
	private String unit;
	private String customer;
	private BigDecimal weight;
	
	
}
