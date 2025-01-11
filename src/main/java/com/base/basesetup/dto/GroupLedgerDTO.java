package com.base.basesetup.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupLedgerDTO {

	private Long id;
	private Long orgId;
	private String groupName;
	private String gstTaxFlag;
	private String coaList;
	private String accountGroupName;
	private String accountCode;
	private String type;
	private String gstType;
	private double gstPercentage;
	private boolean interBranchAc;
	private boolean controllAc;
	private String category;
	private String currency;
	private String createdBy;
	private boolean active;
	private String natureOfAccount;
	private String pBFlag;
}