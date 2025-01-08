package com.base.basesetup.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApBillBalanceDTO {

	private Long id;
	private String accName;
	private String partyName;
	private String partyCode;
	private int creditDays;
	private String docType;
	private String currency;
	private BigDecimal yearEndExRate;
	private BigDecimal billExRate;
	private boolean postBillExRate;
	private Long billNo;
	private LocalDate billDate;
	private String suppRefNo;
	private LocalDate suppRefDate;
	private LocalDate dueDate;
	private BigDecimal debitAmt;
	private BigDecimal creditAmt;
	private String voucherNo;
	private boolean adjustmentDone;

	// Common Fields
	private Long orgId;
	private String branch;
	private String branchCode;
	private String createdBy;
	private boolean active;
	private boolean cancel;
	private String cancelRemarks;
	private String finYear;
	
}
