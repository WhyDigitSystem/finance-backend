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
public class ReceiptDTO {
	private Long id;

	// Receipt fields
	private String type;
	private String customerName;
	private String customerCode;
	private String bankCashAcc;
	private BigDecimal receiptAmt;
	private String bankChargeAcc;
	private BigDecimal bankCharges;
	private String inCurrencyBnkChargs;
	private BigDecimal tdsAmt;
	private BigDecimal taxAmt;
	private String inCurrencyTdsAmt;
	private String chequeBank;
	private String receiptType;
	private String shortName;
	private String chequeUtiNo;
	private LocalDate chequeUtiDate;
	private String receivedFrom;
//	private BigDecimal netAmount;
	private String remarks;

	// Common Fields
	private String branch;
	private String branchCode;
	private String customer;
	private String client;
	private String createdBy;
	private boolean active;
	private boolean cancel;
	private String cancelRemarks;
	private String finYear;
	private String receiptType1;
	private String currency;
	private BigDecimal currencyAmount;
	private Long orgId;
	private String status;
	private BigDecimal currencyAmt;

	List<ReceiptInvDetailsDTO> receiptInvDetailaDTO;
}
