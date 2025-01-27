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
public class ArapDetailsDTO {
	private Long id;
	private String branch;
	private String finYear;
	private Long sourceTransid;
	private String refNo;
	private String accName;
	private String currency;
	private String accCurrency;
	private BigDecimal exRate;
	private BigDecimal amount;
	private BigDecimal baseAmt;
	private BigDecimal nativeAmt;
	private BigDecimal chargableAmt;
	private int gstFlag;
	private String docTypeCode;
	private String subTypeCode;
	private String subLedgerDivision;
	private String suppRefNo;
	private LocalDate refDate;
	private LocalDate supRefDate;
	private String subLedgerCode;
	private int creditDays;
	private LocalDate dueDate;
	private BigDecimal TDSAmt;
	private String hno;
	private Long orgId;
	private String canelRemarks;
	private String createdBy;
	private String ipNo;
	private String latitude;
	private String subLedgerName;
	private String branchCode;


}
