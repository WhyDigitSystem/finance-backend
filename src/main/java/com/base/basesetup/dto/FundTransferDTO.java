package com.base.basesetup.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FundTransferDTO {
	private Long id;
	private String branch;
	private String currency;
	private BigDecimal exRate;
	private BigDecimal amount;
	private Long orgId;
	private String createdBy;
	private String cancelRemarks;
	private String branchCode;
	private String finYear;
	private String ipNo;
	private String latitude;
	private String mode;
	private String docNo;
	private String corpAccount;
	private String transferTo;
	private String branchAcc;
//	private BigDecimal amtBase;
	private String narration;
}
