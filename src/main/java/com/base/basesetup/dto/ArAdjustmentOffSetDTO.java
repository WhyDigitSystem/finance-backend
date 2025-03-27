package com.base.basesetup.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArAdjustmentOffSetDTO {
	
	private Long id;
	private String receiptDocId;
	private LocalDate receiptDocDate;
	private String subLedgerType;
	private String subLedgerName;
	private String subLedgerCode;

	private String currency;
	private BigDecimal exRate;
	private BigDecimal amount;
	private String supplierRefNo;

	// SUMMARY
//	private BigDecimal forexGainOrLoss;
//	private BigDecimal totalSettled;
//	private BigDecimal roundOffAmount;
//	private BigDecimal onAccount;
	private String narration;

	// Default Fields
	private String branch;
	private String branchCode;
	private String finYear;
	private Long orgId;
	private boolean active;
	private String createdBy;
	
	List<ArOffSetInvoiceDetailsDTO> arOffSetInvoiceDetailsDTO;

}
